package com.kotoar.gaitanasis.Magnet;

import com.kotoar.gaitanasis.R;

public class MagnetData implements MagnetDataType{

    public String aLabel;
    public String aContent;
    public Integer aIcon;

    public MagnetData(String label, String content, Integer icon){
        aLabel = label;
        aContent = content;
        aIcon = icon;
    }

    public String getLabel(){
        return aLabel;
    }

    public String getContent(){
        return aContent;
    }

    public Integer getIcon(){
        return aIcon;
    }

}
