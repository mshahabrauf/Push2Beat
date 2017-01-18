package com.attribes.push2beat.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.LayoutProgressLoaderBinding;

/**
 * Created by android on 12/29/16.
 */


@SuppressLint("ValidFragment")
public class LoaderFragment extends Fragment {
    private String loadingText;
    private LayoutProgressLoaderBinding binding;

    public LoaderFragment(String text)
    {
                loadingText = text;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.layout_progress_loader,container,false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View view = binding.getRoot();
        binding.loaderText.setText(loadingText);
        return view;
    }



}
