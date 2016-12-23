package com.attribes.push2beat.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.databinding.FragmentSpeedometerBinding;
import com.attribes.push2beat.speedometer.SpeedPreference;
import com.attribes.push2beat.speedometer.SpeedometerView;


/**
 * Created by android on 12/14/16.
 */
public class SpeedoMeterFragment extends android.support.v4.app.Fragment {

    FragmentSpeedometerBinding binding;

    private long delay = 10000;
    SpeedometerView mView;
    ImageView arrowImag;
    static Handler handler;
    String result;
    int startAngle;
    String priceStr, speedValueStr;
    Thread threadMainMeter;
    SpeedPreference speedPreference;
    Button changeNeedleValueBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speedometer, container, false);
//        binding.speedometer = DataBindingUtil.inflate(inflater,R.layout.fragment_speedometer,container,false);
//        View view = binding.getRoot();
        init(rootView);
        speedPreference = new SpeedPreference(getActivity());
        speedPreference.setPreviousNeedleValue("0");
        moveNeedle();
        return rootView;
    }

    private void init(View rootView) {
        mView = (SpeedometerView)rootView.findViewById(R.id.speedometer_view);
    }

    private void moveNeedle() {

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {

                Bundle b = msg.getData();
                int key = b.getInt("angle_in_degrees", 0);

                if (key == 0) {

                } else {
                    mView.calculateAngleOfDeviation(key);
                }

            };
        };
        handler.postDelayed(null, delay);

        threadMainMeter = new Thread(new Runnable() {

            @Override
            public void run() {

                startAngle = Integer.parseInt(speedPreference.getPreviousNeedleValue());

                obtainSpeedValue();

                if (Integer.parseInt(speedValueStr) > 100) {
                    speedValueStr = "100";
                }
                if (startAngle > Integer.parseInt(speedValueStr)) {
                    for (int i = startAngle; i >= Integer
                            .parseInt(speedValueStr); i = i - 1) {

                        try {
                            Thread.sleep(15);
                            Message msg = new Message();
                            Bundle b = new Bundle();
                            b.putInt("angle_in_degrees", i);
                            msg.setData(b);
                            handler.sendMessage(msg);

                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }

                } else {
                    for (int i = startAngle; i <= Integer
                            .parseInt(speedValueStr); i = i + 1) {

                        try {
                            Thread.sleep(15);
                            Message msg = new Message();
                            Bundle b = new Bundle();
                            b.putInt("angle_in_degrees", i);
                            msg.setData(b);
                            handler.sendMessage(msg);

                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        threadMainMeter.start();
    }

    private void obtainSpeedValue() {
//        Random r = new Random();
//        speedValueStr = String.valueOf(r.nextInt(100 - 10) + 10);
        speedValueStr = String.valueOf(Common.getInstance().getSpeedValue());
        speedPreference.setPreviousNeedleValue(speedValueStr);
    }
}
