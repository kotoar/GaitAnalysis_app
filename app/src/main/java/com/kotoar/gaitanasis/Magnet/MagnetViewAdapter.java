package com.kotoar.gaitanasis.Magnet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kotoar.gaitanasis.NewDevices.DeviceData;
import com.kotoar.gaitanasis.R;

import java.util.ArrayList;
import java.util.List;

public class MagnetViewAdapter extends BaseAdapter {
    public List<MagnetDataType> mData;
    public LayoutInflater mInflater;

    public void addMagnet(MagnetDataType magnetData){
        mData.add(magnetData);
    }

    public MagnetViewAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    public MagnetViewAdapter(Context context, List<MagnetDataType> data){
        mData = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.magnet, null);
        if(convertView != null){
            LinearLayout linearLayout = convertView.findViewById(R.id.magnet_layout);
            TextView magnetLabel = convertView.findViewById(R.id.magnet_label);
            TextView magnetContent = convertView.findViewById(R.id.magnet_content);
            ImageView imageIcon = convertView.findViewById(R.id.image_icon);
            magnetLabel.setText(mData.get(position).getLabel());
            magnetContent.setText(mData.get(position).getContent());
            imageIcon.setImageResource(mData.get(position).getIcon());
        }
        return convertView;
    }



}
