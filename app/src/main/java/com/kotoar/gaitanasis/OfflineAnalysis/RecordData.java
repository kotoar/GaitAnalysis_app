package com.kotoar.gaitanasis.OfflineAnalysis;

import com.kotoar.gaitanasis.Magnet.MagnetDataType;
import com.kotoar.gaitanasis.R;

public class RecordData implements MagnetDataType {
    private String mFilename;
    private String mRecordTitle;
    private String mSubtitle;
    private boolean isProcessed;
    private boolean isRead;

    public RecordData(){
        isRead = false;
    }

    public RecordData(String filename, String record_title, String subtitle, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = subtitle;
        isProcessed = is_processed;
        isRead = true;
    }

    public RecordData(String filename, String record_title, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = "Unprocessed";
        isProcessed = is_processed;
        isRead = true;
    }

    public void reset_info(String filename, String record_title, String subtitle, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = subtitle;
        isProcessed = is_processed;
        isRead = true;
    }

    public void reset_info(String filename, String record_title, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = "Unprocessed";
        isProcessed = is_processed;
        isRead = true;
    }

    public String getFilename(){
        return mFilename;
    }

    public String getLabel(){
        return mRecordTitle;
    }

    public String getContent(){
        return mSubtitle;
    }

    public Integer getIcon(){
        return R.drawable.ic_sports_handball_24px;
    }

    public boolean getIsProcessed(){
        return isProcessed;
    }
}
