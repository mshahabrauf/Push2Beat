package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentSpeedometerBinding;

/**
 * Created by android on 12/14/16.
 */
public class SpeedoMeterFragment extends android.support.v4.app.Fragment {

    FragmentSpeedometerBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_speedometer,container,false);
        View view = binding.getRoot();
        return view;
    }



    public void setSpeed(int speed)
    {
        binding.pointerSpeedometer.speedTo(speed);

    }


}
