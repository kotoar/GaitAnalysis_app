package com.kotoar.gaitanasis;

public class ControlParameters {

    private static ControlParameters instance = new ControlParameters();

    private ControlParameters(){}

    public static ControlParameters getInstance(){
        return instance;
    }

    private boolean is_data_recording;
    private boolean is_data_analyzing;
    private boolean device1_exportable;
    private boolean device2_exportable;

    public boolean is_device1_connected;
    public boolean is_device2_connected;

    public void init(){
        is_data_recording = false;
        is_data_analyzing = false;
        device1_exportable = false;
        device2_exportable = false;
        is_device1_connected = false;
        is_device2_connected = false;
    }

    public void device1Connect(){
        is_device1_connected = true;
    }

    public void device2Connect(){
        is_device2_connected = true;
    }

    public void device1Disconnect(){
        is_device1_connected = false;
    }

    public void device2Disconnect(){
        is_device2_connected = false;
    }

    public void recordClick(){
        if(is_data_recording){
            is_data_recording = false;
            if(is_device1_connected){
                device1_exportable = true;
            }
            if(is_device2_connected){
                device2_exportable = true;
            }
        }
        else{
            is_data_recording = true;
            device1_exportable = false;
            device2_exportable = false;
        }
    }

    public void analyzeClick(){
        if(is_data_analyzing){
            is_data_analyzing = false;
        }
        else{
            is_data_analyzing = true;
        }
    }

    public boolean isRecording(){
        return is_data_recording;
    }

    public void stopRecording(){
        is_data_recording = false;
    }

    public void startRocording(){
        is_data_recording = true;
    }

    public boolean recordClickable(){
        if(is_device1_connected || is_device1_connected){
            return true;
        }
        return false;
    }

    public boolean transmitClickable(){
        if(is_device1_connected || is_device2_connected){
            return true;
        }
        return false;
    }

    public boolean device1CanTransmit(){
        return is_device1_connected;
    }

    public boolean device2CanTransmit(){
        return is_device2_connected;
    }

    public boolean exportClickable(){
        if(device1_exportable || device2_exportable){
            return true;
        }
        return false;
    }

    public boolean imuCaliChlickable(){
        return false;
    }

    public boolean magCaliClickable(){
        return false;
    }

    public boolean device1CanExport(){
        return device1_exportable;
    }

    public boolean device2CanExport(){
        return device2_exportable;
    }

}
