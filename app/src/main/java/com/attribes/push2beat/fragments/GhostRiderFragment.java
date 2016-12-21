package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.RecyclerAdapterInterface;
import com.attribes.push2beat.adapter.TrackListAdapter;
import com.attribes.push2beat.databinding.FragmentGhostRiderBinding;
import com.attribes.push2beat.models.BodyParams.GetListRequestParams;
import com.attribes.push2beat.models.Response.TrackList.Datum;
import com.attribes.push2beat.network.DAL.ListOfTrackDAL;
import com.attribes.push2beat.network.interfaces.TracksArrivalListener;

import java.util.List;

/**
 * Created by android on 12/17/16.
 */




public class GhostRiderFragment extends Fragment {

   private FragmentGhostRiderBinding binding;
    private OnStartButtonListener listener;
    private RecyclerView mRecycle;


    @Override
    public void onAttachFragment(Fragment childFragment) {
        listener = (OnStartButtonListener) childFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ghost_rider,container,false);
        View view = binding.getRoot();
        init();
        fetchTracks();
        return view;
    }

    private void init() {
        onAttachFragment(getParentFragment());

        binding.getRoot().setOnKeyListener(new onBackKeyListenerHandler());

        mRecycle = binding.ghostRecyclerView;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(mLayoutManager);
    }



    private void fetchTracks() {
        GetListRequestParams params = new GetListRequestParams();
        //Todo: change the UserId after SignUp procedure completed

        params.setUser_id(48);
        params.setLat(Common.getInstance().getLocation().getLatitude());
        params.setLng(Common.getInstance().getLocation().getLongitude());

        ListOfTrackDAL.getTrackList(params, new TracksArrivalListener() {
            @Override
            public void onDataRecieved(final List<Datum> data) {
               mRecycle.setAdapter(new TrackListAdapter(data, new RecyclerAdapterInterface() {
                   @Override
                   public void onstartCallback(int position) {
                       Datum datum = data.get(position);
                       listener.onStartGhostRider(datum);
                   }
               }));
            }

            @Override
            public void onEmptyData(String msg) {

            }
        });
    }


    public void onViewClicked(View view)
    {
        Fragment fragment = getFragmentManager().findFragmentByTag(Constants.GHOST_TAG);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(fragment).commit();
    }




    public interface OnStartButtonListener
    {
        void onStartGhostRider(Datum datum);
    }

    private class onBackKeyListenerHandler implements View.OnKeyListener {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

            if( i == KeyEvent.KEYCODE_BACK )
            {
                Fragment fragment = getFragmentManager().findFragmentByTag(Constants.GHOST_TAG);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.remove(fragment);
                return true;
            }

            return false;
        }
    }
}
