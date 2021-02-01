package com.kotoar.gaitanasis.NewDevices;

import com.kotoar.gaitanasis.Magnet.MagnetData;
import com.kotoar.gaitanasis.R;

public class DeviceData extends MagnetData {

    public DeviceData(String label, String content){
        super(label, content,R.drawable.ic_bluetooth_connected_24px);
    }

    public DeviceData(String label, String content, Integer icon) {
        super(label, content, icon);
    }

}
