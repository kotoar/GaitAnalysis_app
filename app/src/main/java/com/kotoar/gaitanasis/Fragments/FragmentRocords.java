package com.kotoar.gaitanasis.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kotoar.gaitanasis.Magnet.MagnetDataType;
import com.kotoar.gaitanasis.Magnet.MagnetViewAdapter;
import com.kotoar.gaitanasis.OfflineAnalysis.RecordsViewAdapter;
import com.kotoar.gaitanasis.R;
import com.kotoar.gaitanasis.SystemConfig;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentRocords extends Fragment {

    ListView recordsList;
    SystemConfig systemConfig;
    RecordsViewAdapter recordsViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        systemConfig = SystemConfig.getInstance();

    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        recordsList = view.findViewById(R.id.records_list);
        recordsViewAdapter = new RecordsViewAdapter(getContext(),systemConfig.getRecords());
        initView();
        return view;
    }

    private void initView(){
        recordsList.setAdapter(recordsViewAdapter);
        recordsViewAdapter.notifyDataSetChanged();
    }

}
