package com.kotoar.gaitanasis.NewDevices;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kotoar.gaitanasis.Magnet.MagnetDataType;
import com.kotoar.gaitanasis.Magnet.MagnetViewAdapter;
import com.kotoar.gaitanasis.R;

import java.util.List;

public class DevicesViewAdapter extends MagnetViewAdapter {

    public DevicesViewAdapter(Context context) {
        super(context);
    }

    public DevicesViewAdapter(Context context, List<MagnetDataType> data) {
        super(context, data);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.magnet, null);
        if(convertView != null){
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
                    Intent intent = new Intent();
                    intent.setAction("bluetooth.select");
                    intent.putExtra("mac_address", mData.get(position).getContent());
                    Context context = v.getContext();
                    context.sendBroadcast(intent);
                }
            });
        }
        return convertView;
    }

}
