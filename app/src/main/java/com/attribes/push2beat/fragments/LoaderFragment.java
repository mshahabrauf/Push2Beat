package com.attribes.push2beat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by android on 12/29/16.
 */


public class LoaderFragment extends Fragment {

  private View view;
    AVLoadingIndicatorView progress;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_progress_loader, container,false);
        return view;
    }
}
