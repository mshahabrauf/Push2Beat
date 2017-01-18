package com.attribes.push2beat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.FragmentStatsBinding;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.attribes.push2beat.models.StatsData;

/**
 * Created by android on 12/20/16.
 */

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;

    private StatsData data;
    public StatsFragment() {}

    @SuppressLint("ValidFragment")
    public StatsFragment(StatsData model) {
        data = model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_stats,container,false);
        View view = binding.getRoot();
        initMapFragment();
        initUi();
        initListener();
        return view;

    }

    private void initListener() {
        binding.layoutGudjob.goodJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }

    private void initUi() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        binding.layoutUserStats.statsCalburntTv.setText(""+data.getCalories());
        binding.layoutUserStats.statsDistancetravelledTv.setText(String.valueOf(data.getTraveledDistance())+"km");
        binding.layoutUserStats.statsAveragespeedTv.setText(String.valueOf(data.getAverageSpeed())+"km/h");
        binding.layoutUserStats.statsTopspeedTv.setText(String.valueOf(data.getTopSpeed()+"km/h"));
        binding.layoutUser.trackNameTv.setText(data.getTrackname());
        binding.statsProfileName.setText(DevicePreferences.getInstance().getuser().getFirstName());


    }

    private void initMapFragment() {
        MapFragment fragment = new MapFragment(DevicePreferences.getInstance().getLocation(), new MapListener() {
            @Override
            public void onMapReady() {
                getMapFragment().showRoute(data.getPath());
                getMapFragment().hideHeader();
            }
        });
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.stats_map_view,fragment, Constants.STATS_MAP_TAG).commit();
    }


    private MapFragment getMapFragment()
    {
        FragmentManager fm = getFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentByTag(Constants.STATS_MAP_TAG);
        return fragment;
    }






    public interface MapListener
    {
         void onMapReady();
    }
}
