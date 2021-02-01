/*
* singleton: global params sync
* */


package com.kotoar.gaitanasis;

import android.os.Environment;

import com.kotoar.gaitanasis.Magnet.MagnetDataType;
import com.kotoar.gaitanasis.OfflineAnalysis.RecordData;

import org.json.JSONArray;
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
import java.util.ArrayList;

public class SystemConfig {
    private final String defaultAddress04 = "98:D3:41:FD:BB:06";
    private final String defaultAddress05 = "98:D3:C1:FD:93:F8";

    private final String config_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gaitanalysis/config.txt";
    private final String records_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gaitanalysis/records.txt";

    private final String config_json_default = "{"
            + "  \"device1\": \"98:D3:41:FD:BB:06\", "
            + "  \"device2\": \"98:D3:C1:FD:93:F8\", "
            + "  \"record_num\": 1 "
            + "}";

    private final String records_json_default = "{"
            + "  \"data\":[ "
            + "{\"filename\":\"test1\",\"record_title\":\"Test Record\",\"record_subtitle\":\"test subtitle\",\"processed\":True}"
            + "]}";

    private String AddressDevice1;
    private String AddressDevice2;
    private Integer RecordNum;
    private String config_json_data;

    private String records_json_data;

    private ArrayList<RecordData> mRecords;

    private File config_json_file;
    private File records_json_file;

    private static SystemConfig instance = new SystemConfig();

    private SystemConfig(){}

    public static SystemConfig getInstance(){
        return instance;
    }

    public void init_work(){
        config_json_file = new File(config_path);
        if(!config_json_file.getParentFile().exists()){
            config_json_file.getParentFile().mkdirs();
        }
        if(!config_json_file.exists()){
            config_json_data = config_json_default;
        }
        else{
            config_json_data = readFile(config_path);
        }

        try{
            JSONObject object = (JSONObject) new JSONTokener(config_json_data).nextValue();
            AddressDevice1 = object.getString("device1");
            AddressDevice2 = object.getString("device2");
            RecordNum = object.getInt("record_num");
        }catch(JSONException e){
            e.printStackTrace();
        }
        mRecords = new ArrayList<>();
        records_json_file = new File(records_path);
        if(!records_json_file.getParentFile().exists()){
            records_json_file.getParentFile().mkdirs();
        }
        if(!records_json_file.exists()){
            records_json_data = records_json_default;
        }
        else{
            records_json_data = readFile(config_path);
        }
        try{
            JSONObject object = (JSONObject) new JSONTokener(records_json_data).nextValue();
            JSONArray json_records_array = object.getJSONArray("data");
            for(int i=0;i<json_records_array.length();i++){
                String filename = json_records_array.getJSONObject(i).getString("filename");
                String record_title = json_records_array.getJSONObject(i).getString("record_title");
                String record_subtitle = json_records_array.getJSONObject(i).getString("record_subtitle");
                boolean is_processed = json_records_array.getJSONObject(i).getBoolean("processed");
                mRecords.add(new RecordData(filename, record_title, record_subtitle,is_processed));
            }
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
            config_json_data = config.toString();
            saveFile(config_json_file, config_json_data);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void resetResords(){
        JSONObject records = new JSONObject();
        try{
            records.put("device1", AddressDevice1);
            JSONArray json_records_data = new JSONArray();
            for(int i=0;i<mRecords.size();i++){
                JSONObject each_record = new JSONObject();
                each_record.put("filename", mRecords.get(i).getFilename());
                each_record.put("record_title", mRecords.get(i).getLabel());
                each_record.put("record_subtitle", mRecords.get(i).getContent());
                each_record.put("processed", mRecords.get(i).getIsProcessed());
                json_records_data.put(each_record);
            }
            records.put("data", json_records_data);
            records_json_data = records.toString();
            saveFile(records_json_file, records_json_data);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void addRecord(RecordData ri){
        mRecords.add(ri);
    }


    private String readFile(String file_path){
        String data = "";
        try{
            InputStream inputStream = new FileInputStream(file_path);
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            data = br.readLine();
        } catch (java.io.IOException e){
            e.printStackTrace();
        }
        return data;
    }

    private void saveFile(File output, String data){
        try{
            if(!config_json_file.exists()){
                config_json_file.createNewFile();
            }
        }catch(java.io.IOException e){
            e.printStackTrace();
        }

        PrintStream mOut = null;
        try {
            mOut = new PrintStream(new FileOutputStream(output));
            mOut.print(data);
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

    public void increaseRecordNum(){
        RecordNum += 1;
    }

    public Integer getRecordNum(){
        return RecordNum;
    }

    public ArrayList<RecordData> getRecords(){
        return mRecords;
    }


}
