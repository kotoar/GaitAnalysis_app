package com.kotoar.gaitanasis.Magnet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kotoar.gaitanasis.R;

public class MagnetView extends LinearLayout {

    LayoutInflater layoutInflater;
    LinearLayout linearLayout;
    TextView textViewLabel;
    TextView textViewContent;
    ImageView imageView;

    String label;
    String content;
    Integer icon;

    private boolean is_clickable;

    public MagnetView(Context context){
        super(context);
        initView(context);
    }

    public MagnetView(Context context, AttributeSet attrs){
        super(context,attrs);
        initMagnetView(context, attrs);
        initView(context);
    }

    public MagnetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMagnetView(context, attrs);
        initView(context);
    }

    private void initMagnetView(Context context, AttributeSet attrs){
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MagnetView);
        label = mTypedArray.getString(R.styleable.MagnetView_Label);
        content = mTypedArray.getString(R.styleable.MagnetView_Content);
        icon = mTypedArray.getResourceId(R.styleable.MagnetView_Icon, 0);
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet, this, true);
        linearLayout = findViewById(R.id.magnet_layout);
        textViewLabel = findViewById(R.id.magnet_label);
        textViewContent = findViewById(R.id.magnet_content);
        imageView = findViewById(R.id.image_icon);

        if(label!=null){
            textViewLabel.setText(label);
        }
        if(content!=null){
            textViewContent.setText(content);
        }
        if(icon!=null){
            imageView.setImageResource(icon);
        }

        is_clickable = false;
    }

    public void setIsClickable(boolean in_is_clickable){
        is_clickable = in_is_clickable;
        if(in_is_clickable){
            textViewLabel.setTextColor(getResources().getColor(R.color.colorMagnetEnabled));
        }
        else{
            textViewLabel.setTextColor(getResources().getColor(R.color.colorMagnetDisable));
        }
    }

    public boolean getIsClickable(){
        return is_clickable;
    }

}
