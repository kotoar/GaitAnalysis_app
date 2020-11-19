package com.kotoar.gaitanasis;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class BluetoothProcess {

    private final String MACAddr04 = "98:D3:41:FD:BB:06";
    static final UUID SerialPort_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    boolean isBlueToothConnected;
    public BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    public BluetoothSocket mSocket;

    BluetoothProcess(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevice = mBluetoothAdapter.getRemoteDevice(MACAddr04);
        isBlueToothConnected = false;
    }

    public void connect() {
        // Start the thread to connect with the given device
        BluetoothSocket tmp = null;
        try {
            tmp = bluetoothDevice.createRfcommSocketToServiceRecord(SerialPort_UUID);
        } catch (IOException e) {
            Log.e("myTAG", "create() failed" + e);
        }
        mSocket = tmp;
        mBluetoothAdapter.cancelDiscovery();
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            isBlueToothConnected = true;
            mSocket.connect();
        } catch (IOException e) {
            Log.v("myTAG", "unable to connect() socket " + e);
            //handler.sendEmptyMessage(NOT_CONNECT);
            isBlueToothConnected = false;
            // Close the socket
            try {
                mSocket.close();
            } catch (IOException e2) {
                Log.v("myTAG", "unable to close() socket during connection failure" + e2);
            }
            return;
        }
        isBlueToothConnected = true;
    }

    public void disconnect(){
        try {
            mSocket.close();
        } catch (IOException e) {
            Log.v("myTAG", "close() of connect socket failed" + e);
        }
    }
}
