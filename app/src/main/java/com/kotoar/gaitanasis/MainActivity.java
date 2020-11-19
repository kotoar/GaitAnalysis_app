package com.kotoar.gaitanasis;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    BluetoothProcess mBluetoothProcess;

    private IntentFilter intentFilter;
    //private UIUpdateBroadcastReceiver mUIUpdateBroadcastReceiver;

    TextView textview1;
    TextView textview2;
    TextView textview3;
    Button mButtonReconnect;

    private int[] values = {0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview1 = (TextView) findViewById(R.id.textView1);
        textview2 = (TextView) findViewById(R.id.textView2);
        textview3 = (TextView) findViewById(R.id.textView3);
        mButtonReconnect = (Button) findViewById(R.id.btn_reconnect);

        mBluetoothProcess = new BluetoothProcess();

        if (!mBluetoothProcess.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
        }
        mBluetoothProcess.connect();
        ConnectedThread mConnectionThread = new ConnectedThread(mBluetoothProcess.mSocket);
        mConnectionThread.start();

        updateTextView();

        mButtonReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothProcess.connect();
                ConnectedThread mConnectionThread = new ConnectedThread(mBluetoothProcess.mSocket);
                mConnectionThread.start();
            }
        });
    }

    public void onDestroy(){
        super.onDestroy();
        //unregisterReceiver(mUIUpdateBroadcastReceiver);
        mBluetoothProcess.disconnect();
    }

    private void updateTextView(){
        textview1.setText("x: " + String.valueOf(values[0]));
        textview2.setText("y: " + String.valueOf(values[1]));
        textview3.setText("z: " + String.valueOf(values[2]));
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // 通过msg传递过来的信息，吐司一下收到的信息
            // Toast.makeText(BuletoothClientActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            if((int)msg.obj ==1){
                updateTextView();
            }
        }
    };

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private int reading_statue;
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
            reading_statue = 1;
            reading_value = 0;
            reading_sign = 1;
        }

        public void run() {
            if (Thread.interrupted()) {
                return;
            }
            Log.e("myTAG", "BEGIN mConnectedThread");
            byte[] buffer = new byte[18];
            int bytes;
            Log.e("myTAG", String.valueOf(mBluetoothProcess.isBlueToothConnected));
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    for(int i=0;i<bytes;i++){
                        if(reading_statue%10==1){
                            if((char)buffer[i] == 'x'){
                                reading_statue = 12;
                            }
                            if((char)buffer[i] == 'y'){
                                reading_statue = 22;
                            }
                            if((char)buffer[i] == 'z'){
                                reading_statue = 32;
                            }
                            continue;
                        }
                        if(reading_statue%10==2){
                            if((char)buffer[i] == '-'){
                                reading_sign = -1;
                            }
                            else{
                                reading_sign = 1;
                            }
                            reading_statue += 1;
                            continue;
                        }
                        if(reading_statue%10==3){
                            reading_value = 256*buffer[i];
                            reading_statue += 1;
                            continue;
                        }
                        if(reading_statue%10==4){
                            reading_value += buffer[i];
                            reading_value *= reading_sign;
                            values[reading_statue/10-1] = reading_value;
                            Log.e("myTAG", String.valueOf(reading_value)+','+String.valueOf(reading_statue));
                            reading_value = 0;
                            reading_statue = 1;
                        }
                    }
                    Message msg = new Message();
                    msg.obj = 1;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    Log.e("myTAG","disconnected " + e);
                    //join();
                    break;
                }
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
