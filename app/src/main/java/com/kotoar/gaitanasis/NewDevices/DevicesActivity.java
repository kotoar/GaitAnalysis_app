package com.kotoar.gaitanasis.NewDevices;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kotoar.gaitanasis.Magnet.MagnetViewAdapter;
import com.kotoar.gaitanasis.R;
import com.kotoar.gaitanasis.SystemConfig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DevicesActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    MagnetViewAdapter mMagnetViewAdapter;
    ListView devicesList;

    RadioButton radioButton_device1;
    RadioButton radioButton_device2;
    TextView textview_devices;

    SystemConfig mSystemConfig;

    private Integer device_selection;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        devicesList = findViewById(R.id.devices_list);
        radioButton_device1 = findViewById(R.id.btn_device1);
        radioButton_device2 = findViewById(R.id.btn_device2);
        textview_devices = findViewById(R.id.textview_devices);
        textview_devices.setText(R.string.devices_text_end_seraching);
        device_selection = 1;
        radioButton_device1.setChecked(true);
        radioButton_device2.setChecked(false);
        radioButton_device1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device_selection = 1;
            }
        });
        radioButton_device2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                device_selection = 2;
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction("bluetooth.select");
        registerReceiver(mDevicesBroadcastReceiver, intentFilter);

        requestBluetoothPermission();

        mSystemConfig = SystemConfig.getInstance();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothAdapter.startDiscovery();

        mMagnetViewAdapter = new MagnetViewAdapter(this);
        initView();

    }

    private void initView(){
        devicesList.setAdapter(mMagnetViewAdapter);
        mMagnetViewAdapter.notifyDataSetChanged();
    }

    public void requestBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    private BroadcastReceiver mDevicesBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceMacAddress = device.getAddress();
                if(deviceName!=null && deviceName.equals("HC-06")){
                    mMagnetViewAdapter.addDevice(new DevicesInformation(deviceName,deviceMacAddress));
                }
                initView();
            }
            if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                textview_devices.setText(R.string.devices_text_end_finished);
            }
            if (action.equals("bluetooth.select")){
                String address = intent.getStringExtra("mac_address");
                if(device_selection==1){
                    mSystemConfig.setAddressDevice1(address);
                    mSystemConfig.resetConfig();
                }
                else{

                    mSystemConfig.setAddressDevice2(address);
                    mSystemConfig.resetConfig();
                }
                Toast.makeText(getApplicationContext(),"Set Device " + device_selection + " to " + address,Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy(){
        unregisterReceiver(mDevicesBroadcastReceiver);
        super.onDestroy();
    }

}
