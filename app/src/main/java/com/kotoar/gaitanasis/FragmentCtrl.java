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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentCtrl extends Fragment {

    MagnetView mMagnetDevices;
    MagnetView mMagnetExport;
    MagnetView mMagnetMagcali;
    MagnetSwitchView mMagnetLED;

    private BroadcastReceiver mCtrlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("transmission.bluetooth_connected")) {
                mMagnetMagcali.setIsClickable(true);
                mMagnetLED.setIsClickable(true);
            }
            if (action.equals("transmission.bluetooth_disconnected")) {
                mMagnetMagcali.setIsClickable(false);
                mMagnetLED.setIsClickable(false);
                mMagnetLED.setChecked(false);
            }
            if (action.equals("transmission.export_enable")) {
                mMagnetExport.setIsClickable(true);
            }
            if (action.equals("transmission.export_disable")) {
                mMagnetExport.setIsClickable(false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("transmission.bluetooth_connected");
        filter.addAction("transmission.bluetooth_disconnected");
        filter.addAction("transmission.export_enable");
        filter.addAction("transmission.export_disable");
        getActivity().getApplicationContext().registerReceiver(mCtrlReceiver, filter);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ctrl, container, false);

        mMagnetDevices = view.findViewById(R.id.magnet_bluetoothsetup);
        mMagnetExport = view.findViewById(R.id.magnet_dataexport);
        mMagnetMagcali = view.findViewById(R.id.magnet_magcalibration);
        mMagnetLED = view.findViewById(R.id.magnet_ledtest);

        mMagnetExport.setIsClickable(false);
        mMagnetMagcali.setIsClickable(false);
        mMagnetLED.setIsClickable(false);


        mMagnetDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("control.start_devices_activity");
                getActivity().sendBroadcast(intent);
            }
        });

        mMagnetExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMagnetExport.getIsClickable()){
                    Message msg = new Message();
                    msg.obj = "Only feasible when data ready";
                    handler.sendMessage(msg);
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("transmission.data_export");
                getActivity().sendBroadcast(intent);
            }
        });

        mMagnetMagcali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mMagnetMagcali.getIsClickable()){
                    Message msg = new Message();
                    msg.obj = "Only feasible when Bluetooth connected";
                    handler.sendMessage(msg);
                    return;
                }
            }
        });

        mMagnetLED.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!mMagnetLED.getIsClickable()){
                    Message msg = new Message();
                    msg.obj = "Only feasible when Bluetooth connected";
                    handler.sendMessage(msg);
                    return;
                }
                if(mMagnetLED.getChecked()){
                    mMagnetLED.turn();
                    transmit_data('d',0,0);
                }
                else{
                    mMagnetLED.turn();
                    transmit_data('l',0,0);
                }

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

    void transmit_data(char type, int value, int device){
        Intent intent = new Intent();
        intent.setAction("transmission.send_data");
        intent.putExtra("ctrl", type);
        intent.putExtra("value", value);
        intent.putExtra("device",device);
        getActivity().sendBroadcast(intent);
    }

}
