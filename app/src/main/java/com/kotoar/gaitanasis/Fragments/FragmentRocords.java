package com.kotoar.gaitanasis.Fragments;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kotoar.gaitanasis.ControlParameters;
import com.kotoar.gaitanasis.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentRocords extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_records, container, false);
        return view;
    }
}
