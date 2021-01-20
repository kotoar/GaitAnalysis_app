package com.kotoar.gaitanasis;

public class RecordsInformation {
    private String mFilename;
    private String mRecordTitle;
    private String mSubtitle;
    private boolean isProcessed;
    private boolean isRead;

    public RecordsInformation(){
        isRead = false;
    }

    public RecordsInformation(String filename, String record_title, String subtitle, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = subtitle;
        isProcessed = is_processed;
        isRead = true;
    }

    public RecordsInformation(String filename, String record_title, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = "Unprocessed";
        isProcessed = is_processed;
        isRead = true;
    }

    public void read_data(String filename, String record_title, String subtitle, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = subtitle;
        isProcessed = is_processed;
        isRead = true;
    }

    public void read_data(String filename, String record_title, boolean is_processed){
        mFilename = filename;
        mRecordTitle = record_title;
        mSubtitle = "Unprocessed";
        isProcessed = is_processed;
        isRead = true;
    }

    public String getFilename(){
        return mFilename;
    }

    public String getRecordTitle(){
        return mRecordTitle;
    }

    public String getSubtitle(){
        return mSubtitle;
    }

    public boolean getIsProcessed(){
        return isProcessed;
    }
}
