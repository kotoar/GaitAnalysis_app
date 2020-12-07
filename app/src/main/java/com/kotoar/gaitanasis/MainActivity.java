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

import static java.lang.String.format;

public class MainActivity extends AppCompatActivity {

    public final double imu_freq = 12.5;
    public final double mag_freq = 10;
    public boolean is_data_record;
    public boolean is_data_analyze;
    public boolean is_data_exportable;

    TimestampSync mTimestampSync;
    BluetoothProcess mBluetoothProcess1;
    DataStorage mDataStorage1;
    ConnectedThread mConnectionThread1;

    ViewPager viewPager;
    TabLayout tablayout;

    SectionPagerAdapter mSectionPagerAdapter;

    private BroadcastReceiver mMainBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("transmission.send_data")){
                char ctrl_type = intent.getCharExtra("ctrl", '0');
                int value = intent.getIntExtra("value", 0);
                int device = intent.getIntExtra("device", 0);
                byte[] data = {(byte)ctrl_type, (byte)value};
                transmitdata(data, device);
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
            if (action.equals("transmission.bluetooth_connect")){
                if(!mBluetoothProcess1.isBlueToothConnected){
                    bluetooth_connect();
                }
                else{
                    bluetooth_disconnect();
                }
            }
        }
    };



    public Map<Character, Double> values = new HashMap<Character, Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();

        init_map_value();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("transmission.send_data");
        intentFilter.addAction("transmission.data_export");
        intentFilter.addAction("transmission.bluetooth_connect");
        intentFilter.addAction("transmission.data_record");
        intentFilter.addAction("transmission.data_analyze");
        registerReceiver(mMainBroadcastReceiver, intentFilter);

        is_data_record = false;
        is_data_analyze = false;
        is_data_exportable = false;
        mBluetoothProcess1 = new BluetoothProcess();
        mDataStorage1 = new DataStorage();
        mTimestampSync = new TimestampSync();

        initViewPager();


        if (!mBluetoothProcess1.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }

        updateTextView();
    }

    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mMainBroadcastReceiver);
        mBluetoothProcess1.disconnect();
    }

    private void init_map_value(){
        values.put('x',0.0);
        values.put('y',0.0);
        values.put('z',0.0);
        values.put('a',0.0);
        values.put('b',0.0);
        values.put('c',0.0);
        values.put('t',0.0);
        values.put('l',0.0);
        values.put('m',0.0);
        values.put('n',0.0);
        values.put('r',0.0);
        //todo: strain sensors
        values.put('s',0.0);
    }

    public void transmitdata(byte[] data, int num){
        mConnectionThread1.write(data);
    }

    public void data_export(){
        is_data_record = false;
        Toast.makeText(this,"exporting",Toast.LENGTH_SHORT).show();
        mDataStorage1.SaveAsJSON("test");
        mDataStorage1.cleardata();
        is_data_record = true;
        Toast.makeText(this,"exported",Toast.LENGTH_SHORT).show();
    }

    private void updateTextView(){
        Intent intent = new Intent();
        intent.setAction("action.update_showing");
        intent.putExtra("x", values.get('x'));
        intent.putExtra("y", values.get('y'));
        intent.putExtra("z", values.get('z'));
        intent.putExtra("a", values.get('a'));
        intent.putExtra("b", values.get('b'));
        intent.putExtra("c", values.get('c'));
        intent.putExtra("l", values.get('l'));
        intent.putExtra("m", values.get('m'));
        intent.putExtra("n", values.get('n'));
        sendBroadcast(intent);
    }

    private void bluetooth_connect(){
        mBluetoothProcess1.connect();
        mConnectionThread1 = new ConnectedThread(mBluetoothProcess1.mSocket);
        if(mBluetoothProcess1.isBlueToothConnected){
            Toast.makeText(this,"connected",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("transmission.bluetooth_connected");
            sendBroadcast(intent);
        }
        else{
            Toast.makeText(this,"Not connect",Toast.LENGTH_SHORT).show();
        }
        mConnectionThread1.start();
    }

    private void bluetooth_disconnect(){
        if(mBluetoothProcess1.isBlueToothConnected){
            mBluetoothProcess1.disconnect();
            if(!mBluetoothProcess1.isBlueToothConnected){
                Toast.makeText(this,"disconnected",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("transmission.bluetooth_disconnected");
                sendBroadcast(intent);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(((String)msg.obj).equals("update")){
                updateTextView();
            }
        }
    };

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

        public ConnectedThread(BluetoothSocket socket) {
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
            Log.e("myTAG", "BEGIN mConnectedThread");
            byte[] buffer = new byte[16];
            int bytes;
            Log.e("myTAG", String.valueOf(mBluetoothProcess1.isBlueToothConnected));
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
                            values.put(reading_type,unit_exchange(reading_type,reading_value));
                            reading_value = 0;
                            reading_step = 1;
                            if(is_data_record && reading_type=='t' || reading_type=='r' || reading_type=='s'){
                                if(reading_type=='t'){
                                    mDataStorage1.addimu3(values.get('x'),values.get('y'),values.get('z'),values.get('t'));
                                }
                                if(reading_type=='r'){
                                    mDataStorage1.addmag(values.get('l'),values.get('m'),values.get('n'),values.get('r'));
                                }
                                if(reading_type=='s'){
                                    //todo: strain sensors output
                                }
                            }
                        }
                    }
                    Message msg = new Message();
                    msg.obj = "update";
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

}
