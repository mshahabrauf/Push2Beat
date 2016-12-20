package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentSpeedometerBinding;
import com.github.anastr.speedviewlib.Speedometer;
import com.github.anastr.speedviewlib.util.OnSpeedChangeListener;

import java.util.Locale;

/**
 * Created by Maaz on 12/9/2016.
 */
public class SpeedoMeterFragment extends android.support.v4.app.Fragment {

    FragmentSpeedometerBinding smBinding;

    public SpeedoMeterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        smBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_speedometer,container,false);
        View view = smBinding.getRoot();

        smBinding.pointerSpeedometer.setMinSpeed(0);
        smBinding.pointerSpeedometer.setMaxSpeed(100);
        smBinding.pointerSpeedometer.setOnSpeedChangeListener(new SpeedChangeListner());
        return view;
    }

    private class SpeedChangeListner implements OnSpeedChangeListener {
        @Override
        public void onSpeedChange(Speedometer speedometer, boolean isSpeedUp, boolean isByTremble) {
            smBinding.textSpeed.setText(String.format(Locale.getDefault(), " %d", speedometer.getCorrectIntSpeed()));
        }
    }
}
