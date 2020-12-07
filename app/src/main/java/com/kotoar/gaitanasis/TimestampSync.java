package com.kotoar.gaitanasis;

public class TimestampSync {

    private long referenceTime;

    public TimestampSync(){
        referenceTime = System.currentTimeMillis();
    }

    public void setTimestamp(){
        referenceTime = System.currentTimeMillis();
    }

    public long getTimestamp(){
        return System.currentTimeMillis() - referenceTime;
    }

}
