package com.kotoar.gaitanasis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public boolean is_data_record;
    public boolean is_data_analyze;
    public boolean is_data_exportable;
    public Map<Character, Double> values1 = new HashMap<Character, Double>();
    public Map<Character, Double> values2 = new HashMap<Character, Double>();

    TimestampSync mTimestampSync;
    BluetoothProcess mBluetoothProcess1;
    DataStorage mDataStorage1;
    ConnectedThread mConnectionThread1;
    BluetoothProcess mBluetoothProcess2;
    DataStorage mDataStorage2;
    ConnectedThread mConnectionThread2;
    SystemConfig mSystemConfig;

    ViewPager viewPager;
    TabLayout tablayout;

    SectionPagerAdapter mSectionPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();

        init_map_value();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("transmission.send_data");
        intentFilter.addAction("transmission.data_export");
        intentFilter.addAction("transmission.bluetooth_device1_connect");
        intentFilter.addAction("transmission.bluetooth_device2_connect");
        intentFilter.addAction("transmission.data_record");
        intentFilter.addAction("transmission.data_analyze");
        intentFilter.addAction("control.start_devices_activity");
        registerReceiver(mMainBroadcastReceiver, intentFilter);

        is_data_record = false;
        is_data_analyze = false;
        is_data_exportable = false;

        mSystemConfig = SystemConfig.getInstance();
        mSystemConfig.init_work();

        mBluetoothProcess1 = new BluetoothProcess(mSystemConfig.getAddressDevice1());
        mBluetoothProcess2 = new BluetoothProcess(mSystemConfig.getAddressDevice2());
        mDataStorage1 = new DataStorage();
        mDataStorage2 = new DataStorage();
        mTimestampSync = new TimestampSync();


        //MacAddress2 = mSystemConfig.getAddressDevice2();

        initViewPager();

        if (!mBluetoothProcess1.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }

        updateTextView(1);
        updateTextView(2);
    }

    public void onDestroy(){
        unregisterReceiver(mMainBroadcastReceiver);
        //mBluetoothProcess1.disconnect();
        super.onDestroy();

    }

    private void init_map_value(){
        values1.put('x',0.0);
        values1.put('y',0.0);
        values1.put('z',0.0);
        values1.put('a',0.0);
        values1.put('b',0.0);
        values1.put('c',0.0);
        values1.put('t',0.0);
        values1.put('l',0.0);
        values1.put('m',0.0);
        values1.put('n',0.0);
        values1.put('r',0.0);
        //todo: strain sensors
        values1.put('s',0.0);

        values2.put('x',0.0);
        values2.put('y',0.0);
        values2.put('z',0.0);
        values2.put('a',0.0);
        values2.put('b',0.0);
        values2.put('c',0.0);
        values2.put('t',0.0);
        values2.put('l',0.0);
        values2.put('m',0.0);
        values2.put('n',0.0);
        values2.put('r',0.0);
        //todo: strain sensors
        values2.put('s',0.0);
    }

    public void transmitdata_device1(byte[] data, int num){
        mConnectionThread1.write(data);
    }

    public void transmitdata_device2(byte[] data, int num){
        mConnectionThread2.write(data);
    }

    public void data_export(){
        is_data_record = false;
        Toast.makeText(this,"exporting",Toast.LENGTH_SHORT).show();
        mDataStorage1.SaveAsJSON("test");
        mDataStorage1.cleardata();
        is_data_record = true;
        Toast.makeText(this,"exported",Toast.LENGTH_SHORT).show();
    }

    private void updateTextView(int index){
        Intent intent = new Intent();
        if(index==1){
            intent.setAction("action.update_showing1");
            intent.putExtra("x", values1.get('x'));
            intent.putExtra("y", values1.get('y'));
            intent.putExtra("z", values1.get('z'));
            intent.putExtra("a", values1.get('a'));
            intent.putExtra("b", values1.get('b'));
            intent.putExtra("c", values1.get('c'));
            intent.putExtra("l", values1.get('l'));
            intent.putExtra("m", values1.get('m'));
            intent.putExtra("n", values1.get('n'));
        }
        else{
            intent.setAction("action.update_showing2");
            intent.putExtra("x", values2.get('x'));
            intent.putExtra("y", values2.get('y'));
            intent.putExtra("z", values2.get('z'));
            intent.putExtra("a", values2.get('a'));
            intent.putExtra("b", values2.get('b'));
            intent.putExtra("c", values2.get('c'));
            intent.putExtra("l", values2.get('l'));
            intent.putExtra("m", values2.get('m'));
            intent.putExtra("n", values2.get('n'));
        }

        sendBroadcast(intent);
    }

    private void bluetooth_device1_connect(){
        mBluetoothProcess1.connect();
        mConnectionThread1 = new ConnectedThread(mBluetoothProcess1.mSocket, 1);
        if(mBluetoothProcess1.isBlueToothConnected){
            Toast.makeText(this,"Device 1 connected",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("transmission.bluetooth_device1_connected");
            sendBroadcast(intent);
        }
        else{
            Toast.makeText(this,"Device 1 not connected",Toast.LENGTH_SHORT).show();
        }
        mConnectionThread1.start();
    }

    private void bluetooth_device1_disconnect(){
        if(mBluetoothProcess1.isBlueToothConnected){
            mBluetoothProcess1.disconnect();
            if(!mBluetoothProcess1.isBlueToothConnected){
                Toast.makeText(this,"Device 1 disconnected",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("transmission.bluetooth_device1_disconnected");
                sendBroadcast(intent);
            }
        }
    }

    private void bluetooth_device2_connect(){
        mBluetoothProcess2.connect();
        mConnectionThread2 = new ConnectedThread(mBluetoothProcess2.mSocket, 2);
        if(mBluetoothProcess2.isBlueToothConnected){
            Toast.makeText(this,"Device 2 connected",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("transmission.bluetooth_device2_connected");
            sendBroadcast(intent);
        }
        else{
            Toast.makeText(this,"Device 2 not connected",Toast.LENGTH_SHORT).show();
        }
        mConnectionThread2.start();
    }

    private void bluetooth_device2_disconnect(){
        if(mBluetoothProcess2.isBlueToothConnected){
            mBluetoothProcess2.disconnect();
            if(!mBluetoothProcess2.isBlueToothConnected){
                Toast.makeText(this,"Device 2 disconnected",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("transmission.bluetooth_device2_disconnected");
                sendBroadcast(intent);
            }
        }
    }

    public void startDeviceActivity(){
        Intent intent = new Intent(MainActivity.this ,DevicesActivity.class);
        startActivity(intent);
    }

    private void initViewPager(){
        tablayout = (TabLayout)findViewById(R.id.tabs_layout);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        tablayout.setupWithViewPager(viewPager);

        mSectionPagerAdapter = new SectionPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(mSectionPagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        //viewPager.setOffscreenPageLimit(2);
    }

    private void requestStoragePermission(){
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        try {
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private char reading_type;
        private int reading_step;
        private int reading_value;
        private int reading_sign;
        private int deviceIndex;

        public ConnectedThread(BluetoothSocket socket, int device_index) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e("myTAG", "temp sockets not created" + e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            reading_type = '0';
            reading_step = 1;
            reading_value = 0;
            reading_sign = 1;
            deviceIndex = device_index;
        }

        private double unit_exchange(char datatype, int input_data){
            if(datatype=='x' || datatype=='y' || datatype=='z'){
                return (double)input_data/4200;
            }
            if(datatype=='a' || datatype=='b' || datatype=='c'){
                return 0.0;
            }
            if(datatype=='t'){
                return (double)mTimestampSync.getTimestamp()/1000.0;
            }
            if(datatype=='l' || datatype=='m' || datatype=='n'){
                return (double)input_data/5000.0;
            }
            if(datatype=='r'){
                return (double)mTimestampSync.getTimestamp()/1000.0;
            }
            //todo: strain sensors unit exchange
            return 0.0;
        }

        public void run() {
            if (Thread.interrupted()) {
                return;
            }
            byte[] buffer = new byte[16];
            int bytes;
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    for(int i=0;i<bytes;i++){
                        if(reading_step==1){
                            char dataget = (char)buffer[i];
                            if(dataget=='x' || dataget=='y' || dataget=='z'
                            || dataget=='a' || dataget=='b' || dataget=='c' || dataget=='t'
                            || dataget=='l' || dataget=='m' || dataget=='n' || dataget=='r'
                            || dataget=='s'){
                                reading_type = (char)buffer[i];
                                reading_step += 1;
                                continue;
                            }
                            continue;
                        }
                        if(reading_step==2){
                            if((char)buffer[i] == '-'){
                                reading_sign = -1;
                            }
                            else{
                                reading_sign = 1;
                            }
                            reading_step += 1;
                            continue;
                        }
                        if(reading_step==3){
                            reading_value = 256*buffer[i];
                            reading_step += 1;
                            continue;
                        }
                        if(reading_step==4){
                            reading_value += buffer[i];
                            reading_value *= reading_sign;
                            if(deviceIndex==1){
                                values1.put(reading_type,unit_exchange(reading_type,reading_value));
                            }
                            else{
                                values2.put(reading_type,unit_exchange(reading_type,reading_value));
                            }

                            reading_value = 0;
                            reading_step = 1;
                            if(is_data_record && reading_type=='t' || reading_type=='r' || reading_type=='s'){
                                if(reading_type=='t'){
                                    if(deviceIndex==1){
                                        mDataStorage1.addimu3(values1.get('x'), values1.get('y'), values1.get('z'), values1.get('t'));
                                    }
                                    else{
                                        mDataStorage2.addimu3(values2.get('x'), values2.get('y'), values2.get('z'), values2.get('t'));
                                    }
                                }
                                if(reading_type=='r'){
                                    if(deviceIndex==1){
                                        mDataStorage1.addmag(values1.get('l'), values1.get('m'), values1.get('n'), values1.get('r'));
                                    }
                                    else{
                                        mDataStorage2.addmag(values2.get('l'), values2.get('m'), values2.get('n'), values2.get('r'));
                                    }
                                }
                                if(reading_type=='s'){
                                    //todo: strain sensors output
                                }
                            }
                        }
                    }
                    Message msg = new Message();
                    if(deviceIndex==1){
                        msg.obj = "update1";
                    }
                    else{
                        msg.obj = "update2";
                    }
                    Log.e("myTAG","updated");

                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e("myTAG","disconnected " + e);
                    //join();
                    break;
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("myTAG","Write Error " + e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("myTAG", "close() of connect socket failed", e);
            }
        }
    }

    private BroadcastReceiver mMainBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("transmission.send_data")){
                char ctrl_type = intent.getCharExtra("ctrl", '0');
                int value = intent.getIntExtra("value", 0);
                int device = intent.getIntExtra("device", 0);
                byte[] data = {(byte)ctrl_type, (byte)value};
                transmitdata_device1(data, device);
            }
            if (action.equals("transmission.data_export")){
                data_export();
            }
            if (action.equals("transmission.data_record")){
                if(is_data_record){     //stop record and enable export
                    is_data_record = false;
                    is_data_exportable = true;
                    Intent sendintent = new Intent();
                    sendintent.setAction("transmission.export_enable");
                    sendBroadcast(sendintent);
                }
                else{       //begin record and start timer
                    is_data_record = true;
                    is_data_exportable = false;
                    Intent sendintent = new Intent();
                    sendintent.setAction("transmission.export_disable");
                    sendBroadcast(sendintent);
                    mDataStorage1.cleardata();
                    mTimestampSync.setTimestamp();
                }
            }
            if (action.equals("transmission.data_analyze")){
                if(is_data_analyze){
                    is_data_analyze = false;
                }
                else{
                    is_data_analyze = true;
                }
            }
            if (action.equals("transmission.bluetooth_device1_connect")){
                if(!mBluetoothProcess1.isBlueToothConnected){
                    bluetooth_device1_connect();
                }
                else{
                    bluetooth_device1_disconnect();
                }
            }
            if (action.equals("transmission.bluetooth_device2_connect")){
                if(!mBluetoothProcess2.isBlueToothConnected){
                    bluetooth_device2_connect();
                }
                else{
                    bluetooth_device2_disconnect();
                }
            }
            if (action.equals("control.start_devices_activity")){
                startDeviceActivity();
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(((String)msg.obj).equals("update1")){
                updateTextView(1);
            }
            if(((String)msg.obj).equals("update2")){
                updateTextView(2);
            }
        }
    };
}
