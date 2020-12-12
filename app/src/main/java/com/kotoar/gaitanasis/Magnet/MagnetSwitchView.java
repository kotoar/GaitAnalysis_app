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

public class MagnetSwitchView extends LinearLayout {

    LayoutInflater layoutInflater;
    LinearLayout linearLayout;
    Switch textViewLabel;
    TextView textViewContent;
    ImageView imageView;

    String label;
    String content;
    Integer icon;

    private boolean is_on;
    private boolean is_clickable;

    public MagnetSwitchView(Context context){
        super(context);
        initView(context);
    }

    public MagnetSwitchView(Context context, AttributeSet attrs){
        super(context,attrs);
        initMagnetView(context, attrs);
        initView(context);
    }

    public MagnetSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMagnetView(context, attrs);
        initView(context);
    }

    private void initMagnetView(Context context, AttributeSet attrs){
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MagnetSwitchView);
        label = mTypedArray.getString(R.styleable.MagnetSwitchView_SwitchLabel);
        content = mTypedArray.getString(R.styleable.MagnetSwitchView_SwitchContent);
        icon = mTypedArray.getResourceId(R.styleable.MagnetSwitchView_SwitchIcon, 0);
    }

    private void initView(Context context){
        layoutInflater.from(context).inflate(R.layout.magnet_switch, this, true);
        linearLayout = findViewById(R.id.magnet_switch_layout);
        textViewLabel = findViewById(R.id.magnet_switch_label);
        textViewContent = findViewById(R.id.magnet_switch_content);
        imageView = findViewById(R.id.magnet_switch_image_icon);

        if(label!=null){
            textViewLabel.setText(label);
        }
        if(content!=null){
            textViewContent.setText(content);
        }
        if(icon!=null){
            imageView.setImageResource(icon);
        }
        is_on = false;
        is_clickable = false;
    }

    public void turn(){
        if(is_on){
            is_on = false;
        }
        else{
            is_on = true;
        }
        textViewLabel.setChecked(is_on);
    }

    public void setChecked(boolean is_checked){
        is_on = is_checked;
        textViewLabel.setChecked(is_checked);
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

    public boolean getChecked(){
        return is_on;
    }

    public boolean getIsClickable(){
        return is_clickable;
    }

    public void setIcon(Integer resid){
        imageView.setImageResource(resid);
    }

}
