package com.attribes.push2beat.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.databinding.FragmentMystatsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Maaz on 12/21/2016.
 */
public class MyStatsFragment extends android.support.v4.app.Fragment  {


    private FragmentMystatsBinding mtBinding;
    private MapView mapView;
    private GoogleMap map;
    private Location startLocation;

    public MyStatsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mtBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mystats,container,false);
        View view = mtBinding.getRoot();

        return view;
    }

}