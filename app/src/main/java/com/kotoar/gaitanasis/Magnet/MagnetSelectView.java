package com.kotoar.gaitanasis.Magnet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kotoar.gaitanasis.R;

public class MagnetSelectView extends LinearLayout {

    LayoutInflater layoutInflater;
    LinearLayout linearLayout;
    TextView textViewLabel;
    TextView textViewContent;
    ImageView imageView;
    ImageView imageViewSelected;

    String label;
    String content;
    Integer icon;
    Integer selected_icon;

    private boolean selected;   //false: 1, true: 2
    private boolean is_clickable;

    public MagnetSelectView(Context context){
        super(context);
        initView(context);
    }

    public MagnetSelectView(Context context, AttributeSet attrs){
        super(context,attrs);
        initMagnetView(context, attrs);
        initView(context);
    }

    public MagnetSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMagnetView(context, attrs);
        initView(context);
    }

    public void initMagnetView(Context context, AttributeSet attrs){
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MagnetSelectView);
        label = mTypedArray.getString(R.styleable.MagnetSelectView_SelectLabel);
        content = mTypedArray.getString(R.styleable.MagnetSelectView_SelectContent);
        icon = mTypedArray.getResourceId(R.styleable.MagnetSelectView_SelectIcon, 0);
        selected_icon = mTypedArray.getResourceId(R.styleable.MagnetSelectView_SelectSelectedIcon, 0);
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet_select, this, true);
        linearLayout = findViewById(R.id.magnet_select_layout);
        textViewLabel = findViewById(R.id.magnet_select_label);
        textViewContent = findViewById(R.id.magnet_select_content);
        imageView = findViewById(R.id.magnet_select_image_icon);
        imageViewSelected = findViewById(R.id.image_select_selected);

        if(label!=null){
            textViewLabel.setText(label);
        }
        if(content!=null){
            textViewContent.setText(content);
        }
        if(icon!=null){
            imageView.setImageResource(icon);
        }
        if(selected_icon!=null){
            imageViewSelected.setImageResource(selected_icon);
        }
        selected = false;
        is_clickable = false;

    }

    public ImageView getViewSelected(){
        return imageViewSelected;
    }

    public void turn(){
        if(selected){
            selected = false;
            imageViewSelected.setImageResource(R.drawable.ic_looks_one_24px);
        }
        else{
            selected = true;
            imageViewSelected.setImageResource(R.drawable.ic_looks_two_24px);
        }
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

    public boolean getSelect(){
        return selected;
    }

    public boolean getIsClickable(){
        return is_clickable;
    }

}
