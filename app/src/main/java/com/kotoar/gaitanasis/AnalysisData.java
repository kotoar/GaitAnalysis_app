/*
* Realtime analysis & data output
* */

package com.kotoar.gaitanasis;

import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

/** json data structure
 * > imu
 * >> [x,y,z,a,b,c,t]
 * > mag
 * >> [x(l),y(m),z(n),t(r)]
 * > strain
 * >> [1,2,3,4,5,6,7,8,t(s)]
 */


public class AnalysisData {

    private final String json_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gaitanalysis/data";

    ArrayList<ArrayList<Double>> dataIMU;
    ArrayList<ArrayList<Double>> dataMag;
    ArrayList<ArrayList<Double>> dataStrain;

    public AnalysisData(){
        dataIMU = new ArrayList<>();
        dataMag = new ArrayList<>();
        dataStrain = new ArrayList<>();
    }

    public void addimu3(double x, double y, double z, double t){
        dataIMU.add(new ArrayList<Double>(Arrays.asList(x,y,z,t)));
    }
    public void addimu6(double x, double y, double z, double a, double b, double c, double t){
        dataIMU.add(new ArrayList<Double>(Arrays.asList(x,y,z,a,b,c,t)));
    }
    public void addmag(double x, double y, double z,double t){
        dataMag.add(new ArrayList<Double>(Arrays.asList(x,y,z,t)));
    }

    public void SaveAsJSON(String filename){
        String project = "Gait Analysis, single device, debugging";
        JSONObject alldata = new JSONObject();
        try{
            alldata.put("project", project);
            JSONArray json_imu = new JSONArray();
            for(int i = 0; i< dataIMU.size(); i++){
                JSONObject each_json_imu = new JSONObject();
                each_json_imu.put("x", dataIMU.get(i).get(0));
                each_json_imu.put("y", dataIMU.get(i).get(1));
                each_json_imu.put("z", dataIMU.get(i).get(2));
                each_json_imu.put("t", dataIMU.get(i).get(3));
                json_imu.put(each_json_imu);
            }
            alldata.put("imu",json_imu);
            JSONArray json_mag = new JSONArray();
            for(int i = 0; i< dataMag.size(); i++){
                JSONObject each_json_mag = new JSONObject();
                each_json_mag.put("x", dataMag.get(i).get(0));
                each_json_mag.put("y", dataMag.get(i).get(1));
                each_json_mag.put("z", dataMag.get(i).get(2));
                each_json_mag.put("t", dataMag.get(i).get(3));
                json_mag.put(each_json_mag);
            }
            alldata.put("mag",json_mag);
            //todo: strain sensors
        }
        catch(JSONException e){
            return;
        }
        File json_output = new File(json_path + '/' + filename + ".txt");
        File parentpath = new File(json_path);
        if(!parentpath.exists()){
            parentpath.mkdirs();
        }
        try{
            if(!json_output.exists()){
                json_output.createNewFile();
            }
        }catch(java.io.IOException e){
            e.printStackTrace();
            Log.e("MyTAG","not created");
        }

        PrintStream mOut = null;
        try {
            mOut = new PrintStream(new FileOutputStream(json_output));
            mOut.print(alldata.toString());
            //todo: broadcast
        } catch(FileNotFoundException e){
            //todo: broadcast
        } finally{
            if (mOut!=null) {
                mOut.close();
            }
        }
        if (json_output.exists()){
            Log.e("MyTAG","successful");
        }
    }

    public void cleardata(){
        dataIMU.clear();
        dataMag.clear();
        dataStrain.clear();
    }


}
