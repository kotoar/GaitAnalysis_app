package com.kotoar.gaitanasis;

public class DevicesInformation {

    private String mDeviceName;
    private String mDeviceMacAddress;
    private Integer mDeviceIcon;

    public DevicesInformation(String deviceName, String deviceMacAddress){
        mDeviceName = deviceName;
        mDeviceMacAddress = deviceMacAddress;
        mDeviceIcon = R.drawable.ic_bluetooth_connected_24px;
    }

    public DevicesInformation(String deviceName, String deviceMacAddress, Integer deviceIcon){
        mDeviceName = deviceName;
        mDeviceMacAddress = deviceMacAddress;
        mDeviceIcon = deviceIcon;
    }

    public String getName(){
        return mDeviceName;
    }

    public String getMacAddress(){
        return mDeviceMacAddress;
    }

    public Integer getDeviceIcon(){
        return mDeviceIcon;
    }

}
