package com.kotoar.gaitanasis.OnlineAnalysis;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import com.kotoar.gaitanasis.ControlParameters;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class DrawView extends View {

    private Paint redPaint; //paint object for drawing the lines
    private Paint greenPaint; //paint object for drawing the lines
    private Paint bluePaint; //paint object for drawing the lines
    private Paint blackPaint; //paint object for drawing the lines
    private Coordinate[]cube_vertices;//the vertices of a 3D cube
    private Integer[] mPosition;
    private int mIndex;

    ControlParameters params;

    public DrawView(Context context, Integer[] position, int index) {
        super(context, null);
        final DrawView thisview=this;
        params = ControlParameters.getInstance();
        redPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        redPaint.setStyle(Paint.Style.FILL);//Stroke
        redPaint.setColor(Color.RED);
        redPaint.setAlpha(70);
        greenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        greenPaint.setStyle(Paint.Style.FILL);//Stroke
        greenPaint.setColor(Color.GREEN);
        greenPaint.setAlpha(70);
        bluePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bluePaint.setStyle(Paint.Style.FILL);//Stroke
        bluePaint.setColor(Color.BLUE);
        bluePaint.setAlpha(70);
        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackPaint.setStyle(Paint.Style.STROKE);//Stroke
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStrokeWidth(2);
        //create a 3D cube
        cube_vertices = new Coordinate[8];
        setCube(cube_vertices, 100);
        cube_vertices=translate(cube_vertices,position[0],position[1],position[2]);
        mPosition = position;
        mIndex = index;
        //thisview.invalidate();//update the view
    }

    private void setCube(Coordinate[] cube, Integer size){
        cube[0] = new Coordinate(-size, -size, -size, 1);
        cube[1] = new Coordinate(-size, -size, size, 1);
        cube[2] = new Coordinate(-size, size, -size, 1);
        cube[3] = new Coordinate(-size, size, size, 1);
        cube[4] = new Coordinate(size, -size, -size, 1);
        cube[5] = new Coordinate(size, -size, size, 1);
        cube[6] = new Coordinate(size, size, -size, 1);
        cube[7] = new Coordinate(size, size, size, 1);
    }

    public void updateview(double[] gravity){
        setCube(cube_vertices, 100);
        double[] vertical_gravity = {0.0, 0.0, -1};
        cube_vertices = rodrigues(cube_vertices, gravity, vertical_gravity);
        cube_vertices=translate(cube_vertices,mPosition[0],mPosition[1],mPosition[2]);
        this.invalidate();//update the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw objects on the screen
        super.onDraw(canvas);
        if(params.cube_drawable(mIndex)){
            DrawCubes(canvas);//draw a cube onto the screen
        }
    }

    public class Coordinate {
        public double x,y,z,w;
        public Coordinate()
        {//create a coordinate with 0,0,0
            x=0;y=0;z=0;w=1;
        }
        public Coordinate(double x,double y,double z, double w)
        {//create a Coordinate object
            this.x=x;this.y=y;this.z=z; this.w=w;
        }
        public void Normalise()
        {//to keep it as a homogeneous coordinate -> divide the coordinate with w and set w=1
            if (w!=0)
            {//ensure that w!=0
                x/=w;
                y/=w;
                z/=w;
                w=1;
            }else w=1;
        }
    }

    private  void DrawLinePairs(Canvas canvas, Coordinate[] vertices, int start, int end, Paint paint){
        //draw a line connecting 2 points
        //canvas - canvas of the view
        //points - array of points
        //start - index of the starting point
        //end - index of the ending point
        //paint - the paint of the line
        canvas.drawLine((int)vertices[start].x,(int)vertices[start].y,(int)vertices[end].x,(int)vertices[end].y,paint);
    }

    private void DrawPathCube(Canvas canvas, Coordinate[] vertices, int point1, int point2, int point3, int point4, Paint paint){
        Path cubepath = new Path();
        cubepath.lineTo((float)vertices[point1].x, (float)vertices[point1].y);
        cubepath.lineTo((float)vertices[point2].x, (float)vertices[point2].y);
        cubepath.lineTo((float)vertices[point3].x, (float)vertices[point3].y);
        cubepath.lineTo((float)vertices[point4].x, (float)vertices[point4].y);
        cubepath.lineTo((float)vertices[point1].x, (float)vertices[point1].y);
        canvas.drawPath(cubepath,paint);
    }

    private void DrawCubes(Canvas canvas) {//draw a cube on the screen
        DrawLinePairs(canvas, cube_vertices, 0, 1, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 1, 3, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 3, 2, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 2, 0, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 4, 5, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 5, 7, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 7, 6, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 6, 4, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 0, 4, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 1, 5, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 2, 6, blackPaint);
        DrawLinePairs(canvas, cube_vertices, 3, 7, blackPaint);

        DrawPathCube(canvas,cube_vertices,1,3,7,5,greenPaint);
        DrawPathCube(canvas,cube_vertices,0,1,3,2,redPaint);
        DrawPathCube(canvas,cube_vertices,2,3,7,6,bluePaint);
        DrawPathCube(canvas,cube_vertices,4,5,7,6,redPaint);
        DrawPathCube(canvas,cube_vertices,1,0,4,5,bluePaint);
        DrawPathCube(canvas,cube_vertices,0,2,6,4,greenPaint);

    }

    public double []GetIdentityMatrix(){//return an 4x4 identity matrix
        double []matrix=new double[16];
        matrix[0]=1;matrix[1]=0;matrix[2]=0;matrix[3]=0;
        matrix[4]=0;matrix[5]=1;matrix[6]=0;matrix[7]=0;
        matrix[8]=0;matrix[9]=0;matrix[10]=1;matrix[11]=0;
        matrix[12]=0;matrix[13]=0;matrix[14]=0;matrix[15]=1;
        return matrix;
    }
    public double []GetIdentityMatrix3(){//return an 3x3 identity matrix
        double []matrix=new double[9];
        matrix[0]=1;matrix[1]=0;matrix[2]=0;
        matrix[3]=0;matrix[4]=1;matrix[5]=0;
        matrix[6]=0;matrix[7]=0;matrix[8]=1;
        return matrix;
    }
    private double[] Transformation4(double[] matrix1, double[] matrix2){
        double[] result = new double[16];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                for(int k=0;k<4;k++){
                    result[4*i+j] += matrix1[4*i+k]*matrix2[4*k+j];
                }
            }
        }
        return result;
    }
    private double[] Matrix3to4(double[] matrix){
        double[] result = new double[16];
        result[0]=matrix[0];    result[1]=matrix[1];    result[2]=matrix[2];    result[3]=0;
        result[4]=matrix[3];    result[5]=matrix[4];    result[6]=matrix[5];    result[7]=0;
        result[8]=matrix[6];    result[9]=matrix[7];    result[10]=matrix[8];   result[11]=0;
        result[12]=0;           result[13]=0;           result[14]=0;           result[15]=1;
        return result;
    }
    private double[] Transformation3(double[] matrix1, double[] matrix2){
        double[] result = new double[9];
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                for(int k=0;k<3;k++){
                    result[3*i+j] += matrix1[3*i+k]*matrix2[3*k+j];
                }
            }
        }
        return result;
    }
    private double[] Addition3(double[] matrix1, double[] matrix2){
        double[] result = new double[9];
        for(int i=0;i<9;i++){
            result[i] = matrix1[i] + matrix2[i];
        }
        return result;
    }
    private double[] Scale3(double[] matrix, double scale){
        double[] result = new double[9];
        for(int i=0;i<9;i++){
            result[i] = scale*matrix[i];
        }
        return result;
    }
    public Coordinate Transformation(Coordinate vertex,double []matrix){//affine transformation with homogeneous coordinates
        //i.e. a vector (vertex) multiply with the transformation matrix
        // vertex - vector in 3D
        // matrix - transformation matrix
        Coordinate result=new Coordinate();
        result.x=matrix[0]*vertex.x+matrix[1]*vertex.y+matrix[2]*vertex.z+matrix[3];
        result.y=matrix[4]*vertex.x+matrix[5]*vertex.y+matrix[6]*vertex.z+matrix[7];
        result.z=matrix[8]*vertex.x+matrix[9]*vertex.y+matrix[10]*vertex.z+matrix[11];
        result.w=matrix[12]*vertex.x+matrix[13]*vertex.y+matrix[14]*vertex.z+matrix[15];
        return result;
    }
    public Coordinate[]Transformation(Coordinate []vertices,double []matrix){
        Coordinate []result=new Coordinate[vertices.length];
        for (int i=0;i<vertices.length;i++)
        {
            result[i]=Transformation(vertices[i],matrix);
            result[i].Normalise();
        }
        return result;
    }

    public Coordinate []translate(Coordinate []vertices,double tx,double ty,double tz){
        double []matrix=GetIdentityMatrix();
        matrix[3]=tx;
        matrix[7]=ty;
        matrix[11]=tz;
        return Transformation(vertices,matrix);
    }
    private Coordinate[]scale(Coordinate []vertices,double sx,double sy,double sz){
        double []matrix=GetIdentityMatrix();
        matrix[0]=sx;
        matrix[5]=sy;
        matrix[10]=sz;
        return Transformation(vertices,matrix);
    }
    private Coordinate[]rotate(Coordinate []vertices,double degrees, int axis, double[] pos){
        double []matrix=GetIdentityMatrix();
        degrees = Math.toRadians(degrees);
        if(axis==0){//x-axis
            matrix[5] =cos(degrees);
            matrix[6] =-sin(degrees);
            matrix[9] =sin(degrees);
            matrix[10] =cos(degrees);
        }
        else if(axis==1){//y-axis
            matrix[0]=cos(degrees);
            matrix[2]=sin(degrees);
            matrix[8]=-sin(degrees);
            matrix[10]=cos(degrees);
        }
        else if(axis==2){//z-axis
            matrix[0]=cos(degrees);
            matrix[1]=-sin(degrees);
            matrix[4]=sin(degrees);
            matrix[5]=cos(degrees);
        }
        vertices = translate(vertices,-pos[0],-pos[1],-pos[2]);
        vertices = Transformation(vertices,matrix);
        vertices = translate(vertices,pos[0],pos[1],pos[2]);
        return vertices;
    }
    private Coordinate[] rodrigues(Coordinate []vertices, double[] vrot, double[] v){
        double scale=0;
        double kx = vrot[1]*v[2]-vrot[2]*v[1];
        double ky = vrot[2]*v[0]-vrot[0]*v[2];
        double kz = vrot[0]*v[1]-vrot[1]*v[0];
        scale = sqrt(kx*kx+ky*ky+kz*kz);
        double lengthvrot = sqrt(vrot[0]*vrot[0]+vrot[1]*vrot[1]+vrot[2]*vrot[2]);
        double lengthv = sqrt(v[0]*v[0]+v[1]*v[1]+v[2]*v[2]);
        kx/=scale;
        ky/=scale;
        kz/=scale;
        double []matrixK=new double[9];
        double sintheta = scale/(lengthv*lengthvrot);
        double costheta = sqrt(1-sintheta*sintheta);

        matrixK[0] = 0;  matrixK[1] = -kz;  matrixK[2] = ky;
        matrixK[3] = kz;  matrixK[4] = 0;    matrixK[5] = -kx;
        matrixK[6] = -ky;  matrixK[7] = kx;    matrixK[8] = 0;
        double[] ret_matrix3 = GetIdentityMatrix3();

        double[] matrix_R2 = Scale3(matrixK,sintheta);
        double[] matrix_R3 = Transformation3(matrixK,matrixK);
        matrix_R3 = Scale3(matrix_R3, 1-costheta);
        ret_matrix3 = Addition3(ret_matrix3, matrix_R2);
        ret_matrix3 = Addition3(ret_matrix3, matrix_R3);
        double[] ret_matrix4 = Matrix3to4(ret_matrix3);
        return Transformation(vertices,ret_matrix4);

    }
}
