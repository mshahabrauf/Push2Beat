package com.attribes.push2beat.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.MyStatsAdapter;
import com.attribes.push2beat.databinding.FragmentMystatsBinding;
import com.attribes.push2beat.models.Response.MyStatsList.Datum;
import com.attribes.push2beat.network.DAL.MyStatsDAL;
import com.attribes.push2beat.network.interfaces.MyStatsDataArrivalListner;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maaz on 12/21/2016.
 */
public class MyStatsFragment extends android.support.v4.app.Fragment  {


    private FragmentMystatsBinding mtBinding;
    private RecyclerView mRecycle;

    public MyStatsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mtBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mystats,container,false);
        View view = mtBinding.getRoot();
        initView();
        fetchMyStats();
        return view;
    }

    private void initView() {
        mRecycle = mtBinding.mystatsRecyclerView;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(mLayoutManager);
    }

    private void fetchMyStats() {
        MyStatsDAL.getMyStatsList("69", new MyStatsDataArrivalListner() {
            @Override
            public void onDataRecieved(List<Datum> data) {

                setProfileImage(data.get(0).getProfile_image());
                mtBinding.mystatsProfileName.setText(data.get(0).getFirst_name());
                mRecycle.setAdapter(new MyStatsAdapter(data, new RecyclerAdapterInterface() {
                    @Override
                    public void onstartCallback(int position) {

                    }
                }));
            }

            @Override
            public void onEmptyData(String msg) {

            }
        });
    }

    private void setProfileImage(String profile_image) {
        if(profile_image == null) {
            mtBinding.mystatsProfileImage.setBackgroundResource(R.drawable.mz);
        }else{
            Picasso.with(getActivity()).load(profile_image).placeholder(R.drawable.mz).into( mtBinding.mystatsProfileImage);
        }
    }

}