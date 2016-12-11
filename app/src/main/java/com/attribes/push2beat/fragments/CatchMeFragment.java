package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentCatchMeBinding;

/**
 * Created by android on 12/11/16.
 */
public class CatchMeFragment extends Fragment {




    public CatchMeFragment() {
    }

    FragmentCatchMeBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catch_me,container,false);
        View view = binding.getRoot();
        return view;
    }
}
