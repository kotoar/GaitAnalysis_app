package com.kotoar.gaitanasis.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kotoar.gaitanasis.ControlParameters;
import com.kotoar.gaitanasis.DrawView;
import com.kotoar.gaitanasis.Magnet.MagnetSwitchView;
import com.kotoar.gaitanasis.MainActivity;
import com.kotoar.gaitanasis.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentGait extends Fragment {

    TextView textview_device1_x;
    TextView textview_device1_y;
    TextView textview_device1_z;
    TextView textview_device1_a;
    TextView textview_device1_b;
    TextView textview_device1_c;
    TextView textview_device1_l;
    TextView textview_device1_m;
    TextView textview_device1_n;

    TextView textview_device2_x;
    TextView textview_device2_y;
    TextView textview_device2_z;
    TextView textview_device2_a;
    TextView textview_device2_b;
    TextView textview_device2_c;
    TextView textview_device2_l;
    TextView textview_device2_m;
    TextView textview_device2_n;

    MagnetSwitchView mMagnetConnect1;
    MagnetSwitchView mMagnetConnect2;
    MagnetSwitchView mMagnetRecord;
    MagnetSwitchView mMagnetDetect;

    LinearLayout layoutdrawing1;
    LinearLayout layoutdrawing2;

    private DrawView draw_view1;
    private DrawView draw_view2;

    ControlParameters params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.update_showing1");
        filter.addAction("action.update_showing2");
        filter.addAction("transmission.bluetooth_status");
        getActivity().getApplicationContext().registerReceiver(mGaitReceiver, filter);
        params = ControlParameters.getInstance();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gait, container, false);
        textview_device1_x = view.findViewById(R.id.textView_device1_x);
        textview_device1_y = view.findViewById(R.id.textView_device1_y);
        textview_device1_z = view.findViewById(R.id.textView_device1_z);
        textview_device1_a = view.findViewById(R.id.textView_device1_a);
        textview_device1_b = view.findViewById(R.id.textView_device1_b);
        textview_device1_c = view.findViewById(R.id.textView_device1_c);
        textview_device1_l = view.findViewById(R.id.textView_device1_l);
        textview_device1_m = view.findViewById(R.id.textView_device1_m);
        textview_device1_n = view.findViewById(R.id.textView_device1_n);
        textview_device2_x = view.findViewById(R.id.textView_device2_x);
        textview_device2_y = view.findViewById(R.id.textView_device2_y);
        textview_device2_z = view.findViewById(R.id.textView_device2_z);
        textview_device2_a = view.findViewById(R.id.textView_device2_a);
        textview_device2_b = view.findViewById(R.id.textView_device2_b);
        textview_device2_c = view.findViewById(R.id.textView_device2_c);
        textview_device2_l = view.findViewById(R.id.textView_device2_l);
        textview_device2_m = view.findViewById(R.id.textView_device2_m);
        textview_device2_n = view.findViewById(R.id.textView_device2_n);
        mMagnetConnect1 = view.findViewById(R.id.magnet_switch_connect_device1);
        mMagnetConnect2 = view.findViewById(R.id.magnet_switch_connect_device2);
        mMagnetRecord = view.findViewById(R.id.magnet_switch_record);
        mMagnetDetect = view.findViewById(R.id.magnet_switch_detect);

        Integer[] pos1 = {200,180,0};
        Integer[] pos2 = {200,180,0};
        draw_view1 = new DrawView(getContext(),pos1,1);
        draw_view2 = new DrawView(getContext(),pos2,2);

        layoutdrawing1 = view.findViewById(R.id.layout_drawview1);
        layoutdrawing2 = view.findViewById(R.id.layout_drawview2);
        layoutdrawing1.addView(draw_view1);
        layoutdrawing2.addView(draw_view2);

        mMagnetRecord.setIsClickable(false);
        mMagnetDetect.setIsClickable(false);

        mMagnetConnect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(params.is_device1_connected){
                    ((MainActivity)getActivity()).bluetooth_device1_disconnect();
                }
                else{
                    ((MainActivity)getActivity()).bluetooth_device1_connect();
                }

            }
        });

        mMagnetConnect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(params.is_device2_connected){
                    ((MainActivity)getActivity()).bluetooth_device2_disconnect();
                }
                else{
                    ((MainActivity)getActivity()).bluetooth_device2_connect();
                }
            }
        });

        mMagnetRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMagnetRecord.getIsClickable()){
                    Message msg = new Message();
                    msg.obj = "Only feasible when Bluetooth connected";
                    handler.sendMessage(msg);
                    return;
                }
                mMagnetRecord.turn();
                params.recordClick();
                Intent intent = new Intent();
                intent.setAction("transmission.record_status");
                getActivity().sendBroadcast(intent);
            }
        });

        mMagnetDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMagnetDetect.getIsClickable()){
                    Message msg = new Message();
                    msg.obj = "Only feasible when Bluetooth connected";
                    handler.sendMessage(msg);
                    return;
                }
                mMagnetDetect.turn();
                params.analyzeClick();
            }
        });
        return view;
    }

    private BroadcastReceiver mGaitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.update_showing1")) {
                double[] gravity = {intent.getDoubleExtra("x", 0.0),
                        intent.getDoubleExtra("y", 0.0),
                        intent.getDoubleExtra("z", 0.0)};
                textview_device1_x.setText("x: " + String.valueOf(gravity[0]));
                textview_device1_y.setText("y: " + String.valueOf(gravity[1]));
                textview_device1_z.setText("z: " + String.valueOf(gravity[2]));
                textview_device1_a.setText("a: " + String.valueOf(intent.getDoubleExtra("a", 0.0)));
                textview_device1_b.setText("b: " + String.valueOf(intent.getDoubleExtra("b", 0.0)));
                textview_device1_c.setText("c: " + String.valueOf(intent.getDoubleExtra("c", 0.0)));
                textview_device1_l.setText("l: " + String.valueOf(intent.getDoubleExtra("l", 0.0)));
                textview_device1_m.setText("m: " + String.valueOf(intent.getDoubleExtra("m", 0.0)));
                textview_device1_n.setText("n: " + String.valueOf(intent.getDoubleExtra("n", 0.0)));
                draw_view1.updateview(gravity);
            }
            if (action.equals("action.update_showing2")) {
                double[] gravity = {intent.getDoubleExtra("x", 0.0),
                        intent.getDoubleExtra("y", 0.0),
                        intent.getDoubleExtra("z", 0.0)};
                textview_device2_x.setText("x: " + String.valueOf(gravity[0]));
                textview_device2_y.setText("y: " + String.valueOf(gravity[1]));
                textview_device2_z.setText("z: " + String.valueOf(gravity[2]));
                textview_device2_a.setText("a: " + String.valueOf(intent.getDoubleExtra("a", 0.0)));
                textview_device2_b.setText("b: " + String.valueOf(intent.getDoubleExtra("b", 0.0)));
                textview_device2_c.setText("c: " + String.valueOf(intent.getDoubleExtra("c", 0.0)));
                textview_device2_l.setText("l: " + String.valueOf(intent.getDoubleExtra("l", 0.0)));
                textview_device2_m.setText("m: " + String.valueOf(intent.getDoubleExtra("m", 0.0)));
                textview_device2_n.setText("n: " + String.valueOf(intent.getDoubleExtra("n", 0.0)));
                draw_view2.updateview(gravity);
            }
            if (action.equals("transmission.bluetooth_status")) {
                mMagnetConnect1.setChecked(params.is_device1_connected);
                mMagnetConnect2.setChecked(params.is_device2_connected);
                if(params.is_device1_connected){
                    mMagnetConnect1.setIcon(R.drawable.ic_bluetooth_connected_24px);
                }
                else{
                    mMagnetConnect1.setIcon(R.drawable.ic_bluetooth_disabled_24px);
                }
                if(params.is_device2_connected){
                    mMagnetConnect2.setIcon(R.drawable.ic_bluetooth_connected_24px);
                }
                else{
                    mMagnetConnect2.setIcon(R.drawable.ic_bluetooth_disabled_24px);
                }
                if(params.is_device1_connected || params.is_device2_connected){
                    mMagnetRecord.setIsClickable(true);
                    mMagnetDetect.setIsClickable(true);
                }
                else{
                    mMagnetRecord.setIsClickable(false);
                    mMagnetRecord.setChecked(false);
                    mMagnetDetect.setIsClickable(false);
                    mMagnetDetect.setChecked(false);
                }
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
        }
    };
}
