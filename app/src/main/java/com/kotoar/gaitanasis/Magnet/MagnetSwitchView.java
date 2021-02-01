package com.kotoar.gaitanasis.Magnet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.kotoar.gaitanasis.ControlParameters;
import com.kotoar.gaitanasis.R;
import com.kotoar.gaitanasis.SystemConfig;

public class MagnetSwitchView extends LinearLayout {

    LayoutInflater layoutInflater;
    LinearLayout linearLayout;
    Switch textViewLabel;
    TextView textViewContent;
    ImageView imageView;

    String label;
    String content;
    Integer icon;

    ControlParameters params;

    private boolean is_on;
    private boolean is_clickable;
    private boolean is_content_changable;

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

    protected void initMagnetView(Context context, AttributeSet attrs){
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MagnetSwitchView);
        label = mTypedArray.getString(R.styleable.MagnetSwitchView_SwitchLabel);
        content = mTypedArray.getString(R.styleable.MagnetSwitchView_SwitchContent);
        icon = mTypedArray.getResourceId(R.styleable.MagnetSwitchView_SwitchIcon, 0);
    }

    protected void initView(Context context){
        params = ControlParameters.getInstance();
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
        is_content_changable = false;
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

    public void setChangable(){
        is_content_changable = true;
        textViewContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                content_change(textViewLabel);
            }
        });
    }

    private void content_change(View view){
        final EditText input_content = new EditText(getContext());
        new AlertDialog.Builder(getContext()).setTitle("Set Record Name")
                //.setIcon(android.R.drawable.sym_def_app_icon)
                .setView(input_content)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = input_content.getText().toString();
                        content = "Record: " + input;
                        textViewContent.setText(content);
                        params.now_recording_name = input;

                    }
                }).setNegativeButton("cancel",null).show();

    }

}
