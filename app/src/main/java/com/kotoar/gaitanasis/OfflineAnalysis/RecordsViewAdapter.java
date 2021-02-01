package com.kotoar.gaitanasis.OfflineAnalysis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kotoar.gaitanasis.Magnet.MagnetData;
import com.kotoar.gaitanasis.Magnet.MagnetDataType;
import com.kotoar.gaitanasis.Magnet.MagnetViewAdapter;
import com.kotoar.gaitanasis.R;

import java.util.ArrayList;
import java.util.List;

public class RecordsViewAdapter extends MagnetViewAdapter {

    public List<RecordData> mData;

    public RecordsViewAdapter(Context context) {
        super(context);
    }

    public void addMagnet(RecordData magnetData){
        mData.add(magnetData);
    }

    public RecordsViewAdapter(Context context, List<RecordData> data) {
        super(context);
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.magnet, null);
        if (convertView != null) {
            LinearLayout linearLayout = convertView.findViewById(R.id.magnet_layout);
            TextView textDeviceName = convertView.findViewById(R.id.magnet_label);
            TextView textMacAddress = convertView.findViewById(R.id.magnet_content);
            ImageView imageIcon = convertView.findViewById(R.id.image_icon);

            textDeviceName.setText(mData.get(position).getLabel());
            textMacAddress.setText(mData.get(position).getContent());
            imageIcon.setImageResource(mData.get(position).getIcon());

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo: offline analysis
                }
            });
        }
        return convertView;
    }
}
