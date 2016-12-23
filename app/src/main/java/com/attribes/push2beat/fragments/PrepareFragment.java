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
        init();
        return view;
    }

    private void init() {

        binding.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GpsFragment gpsFragment = new GpsFragment();
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.replace(R.id.prepare_container,gpsFragment,Constants.GPS_TAG).commit();
                binding.root.setVisibility(View.GONE);
            }
        });
    }
}
