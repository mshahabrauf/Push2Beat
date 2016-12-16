package com.attribes.push2beat.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.attribes.push2beat.R;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.Utils.CatchMeAdapterInterface;
import com.attribes.push2beat.adapter.UserListAdapter;
import com.attribes.push2beat.databinding.FragmentCatchMeBinding;
import com.attribes.push2beat.models.BodyParams.GetListRequestParams;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.attribes.push2beat.network.DAL.ListOfUserDAL;
import com.attribes.push2beat.network.interfaces.ArrivalListener;

import java.util.List;

/**
 * Created by android on 12/11/16.
 */
public class CatchMeFragment extends Fragment {




    public CatchMeFragment() {
    }

    private FragmentCatchMeBinding binding;
    private RecyclerView mRecycle;

    private CatchMeFragment.OnStartButtonListener listener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catch_me,container,false);
        View view = binding.getRoot();
        init();
        fetchUsers();



        return view;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        listener = (CatchMeFragment.OnStartButtonListener) childFragment;
    }


    private void init() {
        onAttachFragment(getParentFragment());

        mRecycle = binding.myRecyclerView;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mRecycle.setLayoutManager(mLayoutManager);
        getMapFragment().moveMapCamera(Common.getInstance().getLocation().getLatitude(),Common.getInstance().getLocation().getLongitude());


    }

    private void fetchUsers() {
        GetListRequestParams params = new GetListRequestParams();
        //Todo: change the UserId after SignUp procedure completed

        params.setUser_id(1);
        params.setLat(Common.getInstance().getLocation().getLatitude());
        params.setLng(Common.getInstance().getLocation().getLongitude());

        ListOfUserDAL.getUserList(params, new ArrivalListener() {
            @Override
            public void onDataRecieved(final List<Datum> data)
            {


                mRecycle.setAdapter(new UserListAdapter(data, new CatchMeAdapterInterface() {
                    @Override
                    public void onstartCallback(int position) {

                     listener.onStartCmiyc(data.get(position));
                    }
                }));

                displayUserOnMap(data);

            }

            @Override
            public void onEmptyData(String msg) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void displayUserOnMap(List<Datum> data)
    {

        if(data!=null)
        {
            getMapFragment().showUsers(data);
            getMapFragment().showQBLocationMap();
        }
    }

    private MapFragment getMapFragment()
    {
        FragmentManager fm = getFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentByTag(Constants.MAP_TAG);
        return fragment;
    }



    public interface OnStartButtonListener
    {
        void onStartCmiyc(Datum datum);
    }




}
