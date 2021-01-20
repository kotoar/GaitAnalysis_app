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

import com.kotoar.gaitanasis.NewDevices.DevicesInformation;
import com.kotoar.gaitanasis.R;

import java.util.ArrayList;
import java.util.List;

public class MagnetViewAdapter extends BaseAdapter {
    private List<DevicesInformation> mData;
    private LayoutInflater mInflater;

    public void addDevice(DevicesInformation device_information){
        mData.add(device_information);
    }

    public MagnetViewAdapter(Context context){
        mData = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public MagnetViewAdapter(Context context, List<DevicesInformation> data){
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
            TextView textDeviceName = convertView.findViewById(R.id.magnet_label);
            TextView textMacAddress = convertView.findViewById(R.id.magnet_content);
            ImageView imageIcon = convertView.findViewById(R.id.image_icon);

            textDeviceName.setText(mData.get(position).getName());
            textMacAddress.setText(mData.get(position).getMacAddress());
            imageIcon.setImageResource(mData.get(position).getDeviceIcon());

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("bluetooth.select");
                    intent.putExtra("mac_address", mData.get(position).getMacAddress());
                    Context context = v.getContext();
                    context.sendBroadcast(intent);
                }
            });
        }
        return convertView;
    }



}
