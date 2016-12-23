package com.attribes.push2beat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.databinding.FragmentMyStatsBinding;
import com.attribes.push2beat.models.StatsData;

/**
 * Created by android on 12/20/16.
 */

public class StatsFragment extends Fragment {

    private FragmentMyStatsBinding binding;

    private StatsData data;
    public StatsFragment() {}

    @SuppressLint("ValidFragment")
    public StatsFragment(StatsData model) {
        data = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_stats,container,false);
        View view = binding.getRoot();
        initMapFragment();
        initUi();
        return view;

    }

    private void initUi() {
        binding.layoutUserStats.statsCalburntTv.setText(""+data.getCalories());
        binding.layoutUserStats.statsDistancetravelledTv.setText(String.valueOf(data.getTraveledDistance())+"km");
        binding.layoutUserStats.statsAveragespeedTv.setText(String.valueOf(data.getAverageSpeed())+"km/h");
        binding.layoutUserStats.statsTopspeedTv.setText(String.valueOf(data.getTopSpeed()+"km/h"));
        getMapFragment().showRoute(data.getPath());


    }

    private void initMapFragment() {
        MapFragment fragment = getMapFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.stats_map_view,fragment, Constants.MAP_TAG).commit();
    }


    private MapFragment getMapFragment()
    {
        FragmentManager fm = getFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentByTag(Constants.MAP_TAG);
        return fragment;
    }
}
