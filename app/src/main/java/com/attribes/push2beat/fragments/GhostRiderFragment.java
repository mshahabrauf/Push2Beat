package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentGhostRiderBinding;

/**
 * Created by android on 12/17/16.
 */

public class GhostRiderFragment extends Fragment {

   private FragmentGhostRiderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ghost_rider,container,false);
        View view = binding.getRoot();
        return view;
    }
}
