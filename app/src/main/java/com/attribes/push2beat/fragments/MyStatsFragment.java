package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Alerts;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.adapter.MyStatsAdapter;
import com.attribes.push2beat.databinding.FragmentMystatsBinding;
import com.attribes.push2beat.models.Response.MyStatsList.Datum;
import com.attribes.push2beat.models.Response.MyStatsList.Track;
import com.attribes.push2beat.network.DAL.MyStatsDAL;
import com.attribes.push2beat.network.interfaces.MyStatsDataArrivalListner;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maaz on 12/21/2016.
 */
public class MyStatsFragment extends android.support.v4.app.Fragment  {


    private FragmentMystatsBinding mtBinding;
    private RecyclerView mRecycle;
    private RecyclerView trackRecycle;

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
       // trackRecycle = mtBinding.mystatsTrackRecyclerView;
        mtBinding.mystatsProfileName.setText(DevicePreferences.getInstance().getuser().getFirst_name());
        setProfileImage(DevicePreferences.getInstance().getuser().getProfile_image());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext())
        {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        RecyclerView.LayoutManager mLayoutManage = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(mLayoutManage);
        //trackRecycle.setLayoutManager(mLayoutManage);


    }

    private void fetchMyStats()
    {
        mtBinding.loaderLayout.progressWheel.setVisibility(View.VISIBLE);
        MyStatsDAL.getMyStatsList(DevicePreferences.getInstance().getuser().getId(), new MyStatsDataArrivalListner() {
            @Override
            public void onDataRecieved(List<Datum> data, List<Track> track) {
                mtBinding.loaderLayout.progressWheel.setVisibility(View.GONE);
                setProfileImage(data.get(0).getProfile_image());
                mtBinding.mystatsProfileName.setText(data.get(0).getFirst_name());

               // trackRecycle.setAdapter(new TracKStatsAdapter(track));
                mRecycle.setAdapter(new MyStatsAdapter(data,track));
            }

            @Override
            public void onEmptyData(String msg) {
                mtBinding.loaderLayout.progressWheel.setVisibility(View.GONE);
                Toast.makeText(getContext(), "No stats found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message)
            {
                mtBinding.loaderLayout.progressWheel.setVisibility(View.GONE);
                Alerts.showError(getActivity(),message);

            }
        });
    }

    private void setProfileImage(String profile_image) {
        if(profile_image ==null)
        {

        }
        else {

            Picasso.with(getActivity()).load(profile_image).placeholder(R.drawable.placeholder).into(mtBinding.mystatsProfileImage);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }
}