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

    ControlParameters params;
    MagnetView mMagnetDevices;
    MagnetView mMagnetExport;
    MagnetSelectView mMagnetImucali;
    MagnetSelectView mMagnetMagcali;
    MagnetSwitchView mMagnetLED;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("transmission.bluetooth_status");
        filter.addAction("transmission.record_status");
        getActivity().getApplicationContext().registerReceiver(mCtrlReceiver, filter);
        params = ControlParameters.getInstance();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ctrl, container, false);

        mMagnetDevices = view.findViewById(R.id.magnet_bluetoothsetup);
        mMagnetExport = view.findViewById(R.id.magnet_dataexport);
        mMagnetImucali = view.findViewById(R.id.magnet_imucalibration);
        mMagnetMagcali = view.findViewById(R.id.magnet_magcalibration);
        mMagnetLED = view.findViewById(R.id.magnet_ledtest);

        mMagnetExport.setIsClickable(false);
        mMagnetMagcali.setIsClickable(false);
        mMagnetLED.setIsClickable(false);

        mMagnetDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),DevicesActivity.class);
                startActivity(intent);
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
                ((MainActivity)getActivity()).data_export();
            }
        });

        mMagnetImucali.getViewSelected().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMagnetImucali.turn();
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

        mMagnetMagcali.getViewSelected().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMagnetMagcali.turn();
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
                    byte[] data = {'d', 0};
                    if(params.is_device1_connected){
                        ((MainActivity)getActivity()).transmitdata_device1(data);
                    }
                    if(params.is_device2_connected){
                        ((MainActivity)getActivity()).transmitdata_device2(data);
                    }
                }
                else{
                    mMagnetLED.turn();
                    byte[] data = {'l', 0};
                    if(params.is_device1_connected){
                        ((MainActivity)getActivity()).transmitdata_device1(data);
                    }
                    if(params.is_device2_connected){
                        ((MainActivity)getActivity()).transmitdata_device2(data);
                    }
                }
            }
        });

        return view;
    }

    private BroadcastReceiver mCtrlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("transmission.bluetooth_status")) {
                mMagnetMagcali.setIsClickable(params.magCaliClickable());
                mMagnetLED.setIsClickable(params.transmitClickable());
            }
            if (action.equals("transmission.record_status")) {
                mMagnetExport.setIsClickable(params.exportClickable());
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
