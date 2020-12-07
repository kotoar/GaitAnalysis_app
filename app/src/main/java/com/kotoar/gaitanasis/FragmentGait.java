package com.kotoar.gaitanasis;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentGait extends Fragment {

    TextView textview_x;
    TextView textview_y;
    TextView textview_z;
    TextView textview_a;
    TextView textview_b;
    TextView textview_c;
    TextView textview_l;
    TextView textview_m;
    TextView textview_n;

    MagnetSwitchView mMagnetConnect;
    MagnetSwitchView mMagnetRecord;
    MagnetSwitchView mMagnetDetect;

    private BroadcastReceiver mGaitReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.update_showing")) {
                textview_x.setText("x: " + String.valueOf(intent.getDoubleExtra("x", 0.0)));
                textview_y.setText("y: " + String.valueOf(intent.getDoubleExtra("y", 0.0)));
                textview_z.setText("z: " + String.valueOf(intent.getDoubleExtra("z", 0.0)));
                textview_a.setText("a: " + String.valueOf(intent.getDoubleExtra("a", 0.0)));
                textview_b.setText("b: " + String.valueOf(intent.getDoubleExtra("b", 0.0)));
                textview_c.setText("c: " + String.valueOf(intent.getDoubleExtra("c", 0.0)));
                textview_l.setText("l: " + String.valueOf(intent.getDoubleExtra("l", 0.0)));
                textview_m.setText("m: " + String.valueOf(intent.getDoubleExtra("m", 0.0)));
                textview_n.setText("n: " + String.valueOf(intent.getDoubleExtra("n", 0.0)));
            }
            if (action.equals("transmission.bluetooth_connected")) {
                mMagnetConnect.setChecked(true);
                mMagnetConnect.setIcon(R.drawable.ic_bluetooth_connected_24px);
                mMagnetRecord.setIsClickable(true);
                mMagnetDetect.setIsClickable(true);
            }
            if (action.equals("transmission.bluetooth_disconnected")) {
                mMagnetConnect.setChecked(false);
                mMagnetConnect.setIcon(R.drawable.ic_bluetooth_disabled_24px);
                mMagnetRecord.setIsClickable(false);
                mMagnetRecord.setChecked(false);
                mMagnetDetect.setIsClickable(false);
                mMagnetDetect.setChecked(false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.update_showing");
        filter.addAction("action.show_mag");
        filter.addAction("transmission.bluetooth_connected");
        filter.addAction("transmission.bluetooth_disconnected");
        getActivity().getApplicationContext().registerReceiver(mGaitReceiver, filter);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gait, container, false);
        textview_x = view.findViewById(R.id.textView_x);
        textview_y = view.findViewById(R.id.textView_y);
        textview_z = view.findViewById(R.id.textView_z);
        textview_a = view.findViewById(R.id.textView_a);
        textview_b = view.findViewById(R.id.textView_b);
        textview_c = view.findViewById(R.id.textView_c);
        textview_l = view.findViewById(R.id.textView_l);
        textview_m = view.findViewById(R.id.textView_m);
        textview_n = view.findViewById(R.id.textView_n);
        mMagnetConnect = view.findViewById(R.id.magnet_switch_connect);
        mMagnetRecord = view.findViewById(R.id.magnet_switch_record);
        mMagnetDetect = view.findViewById(R.id.magnet_switch_detect);

        mMagnetRecord.setIsClickable(false);
        mMagnetDetect.setIsClickable(false);

        mMagnetConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("transmission.bluetooth_connect");
                getActivity().sendBroadcast(intent);
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
                Intent intent = new Intent();
                intent.setAction("transmission.data_record");
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
                Intent intent = new Intent();
                intent.setAction("transmission.data_analyze");
                getActivity().sendBroadcast(intent);
            }
        });

        return view;

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Toast.makeText(getActivity(),(String)msg.obj,Toast.LENGTH_SHORT).show();
        }
    };



}
