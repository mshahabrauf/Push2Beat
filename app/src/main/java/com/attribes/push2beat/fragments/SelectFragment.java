package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.databinding.ActivitySelectBinding;
import com.attribes.push2beat.mainnavigation.MainActivity;

/**
 * Created by android on 1/19/17.
 */

public class SelectFragment extends Fragment{

    ActivitySelectBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_select,container,false);
        View view = binding.getRoot();

        initButtons();
        return view;

    }





    private void initButtons() {
        ((MainActivity)getActivity()).changeTitle("Select Activity");

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.getInstance().setRunType(1); // 1 : normal run
                MusicFragment gpsFragment = new MusicFragment(true);
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.main_container,gpsFragment);
                ft.commit();

//                Intent intent = new Intent(SelectActivity.this,MainActivity.class);
//                 startActivity(intent);
            }
        });

        binding.timerGhost.ghostGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.getInstance().setRunType(2);  // 2 : ghost Rider
                GpsFragment gpsFragment = new GpsFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.main_container,gpsFragment);
                ft.commit();

            }
        });


        binding.timerCmiyc.cmGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Common.getInstance().setRunType(3); // 3 : Catch me if you can
                GpsFragment gpsFragment = new GpsFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.main_container,gpsFragment);
                ft.commit();
            }
        });


        binding.moveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.getInstance().setRunType(0);
                GpsFragment gpsFragment = new GpsFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.add(R.id.main_container,gpsFragment);
                ft.commit();

            }
        });

    }
}
