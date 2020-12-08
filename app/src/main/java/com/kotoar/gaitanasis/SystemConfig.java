package com.kotoar.gaitanasis;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class SystemConfig {
    private final String defaultAddress04 = "98:D3:41:FD:BB:06";
    private final String defaultAddress05 = "98:D3:C1:FD:93:F8";

    private final String config_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gaitanalysis/config.txt";

    private final String json_default = "{"
            + "  \"device1\": \"98:D3:41:FD:BB:06\", "
            + "  \"device2\": \"98:D3:C1:FD:93:F8\", "
            + "  \"record_num\": 1 "
            + "}";

    private String AddressDevice1;
    private String AddressDevice2;
    private Integer RecordNum;
    private String json_data;

    File config_json_file;
    File parentpath_file;

    private static SystemConfig instance = new SystemConfig();

    private SystemConfig(){

    }

    public static SystemConfig getInstance(){
        return instance;
    }

    public void init_work(){
        config_json_file = new File(config_path);
        parentpath_file = new File(config_json_file.getParent());
        if(!parentpath_file.exists()){
            parentpath_file.mkdirs();
        }

        if(!config_json_file.exists()){
            json_data = json_default;
        }
        else{
            readFile();
        }

        try{
            JSONObject object = (JSONObject) new JSONTokener(json_data).nextValue();
            AddressDevice1 = object.getString("device1");
            AddressDevice2 = object.getString("device2");
            RecordNum = object.getInt("record_num");
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void resetConfig(){
        JSONObject config = new JSONObject();
        try{
            config.put("device1", AddressDevice1);
            config.put("device2", AddressDevice2);
            config.put("record_num", RecordNum);
            json_data = config.toString();
            saveFile();
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    private void readFile(){
        try{
            InputStream inputStream = new FileInputStream(config_path);
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            json_data = br.readLine();
        } catch (java.io.IOException e){
            e.printStackTrace();
        }

    }

    private void saveFile(){
        try{
            if(!config_json_file.exists()){
                config_json_file.createNewFile();
            }
        }catch(java.io.IOException e){
            e.printStackTrace();
        }

        PrintStream mOut = null;
        try {
            mOut = new PrintStream(new FileOutputStream(config_json_file));
            mOut.print(json_data);
            //todo: broadcast
        } catch(FileNotFoundException e){
            //todo: broadcast
        } finally{
            if (mOut!=null) {
                mOut.close();
            }
        }
    }

    public void setAddressDevice1(String device1){
        AddressDevice1 = device1;
    }

    public String getAddressDevice1(){
        return AddressDevice1;
    }

    public void setAddressDevice2(String device2){
        AddressDevice2 = device2;
    }

    public String getAddressDevice2(){
        return AddressDevice2;
    }

    public void setRecordNum(Integer record_num){
        RecordNum = record_num;
    }

    public Integer getRecordNum(){
        return RecordNum;
    }



}
