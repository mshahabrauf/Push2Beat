package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.databinding.FragmentPrepareYourselfBinding;

/**
 * Created by android on 12/23/16.
 */

public class PrepareFragment extends Fragment {
   FragmentPrepareYourselfBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_prepare_yourself,container,false);
        View view = binding.getRoot();
        colorHARD_Texts();
        colorHIT_Texts();
        init();
        return view;
    }

    private void colorHARD_Texts() {

        SpannableString text = new SpannableString("A countdown will let you know when to begin... so GO HARD!");   // this is the text we'll be operating on
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_pink)), 49, 57, 0);      // make "GO HARD!" (characters 49 to 57) red
        binding.hardTv.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void colorHIT_Texts() {
        SpannableString text = new SpannableString("The HIT workout is great for Indoor/Outdoor cycling (GPS)Rowing (GPS)");
        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary_pink)), 4, 15, 1);
        binding.hitTv.setText(text, TextView.BufferType.SPANNABLE);
    }

    private void init() {

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsFragment gpsFragment = new GpsFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.replace(R.id.prepare_container,gpsFragment,Constants.GPS_TAG).commit();
            }
        });
    }
}
