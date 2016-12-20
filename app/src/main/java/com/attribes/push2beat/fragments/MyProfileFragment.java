package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentMyProfileBinding;

/**
 * Created by Maaz on 12/11/2016.
 */
public class MyProfileFragment extends android.support.v4.app.Fragment {

     FragmentMyProfileBinding mpBinding;

    public MyProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile,container,false);
        View view = mpBinding.getRoot();
        return view;
    }
}
