package com.attribes.push2beat.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import com.attribes.push2beat.R;
import com.attribes.push2beat.databinding.FragmentTimerBinding;
import com.attribes.push2beat.utils.Constants;
import com.attribes.push2beat.models.BodyParams.AddTrackParams;
import com.attribes.push2beat.network.DAL.AddTrackDAL;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maaz on 12/8/16.
 */

public class Timer_GpsFragment extends android.support.v4.app.Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private MapView mapView;
    private FragmentTimerBinding binding;
    private android.support.v4.app.Fragment speedoMeterFragment;
    private android.support.v4.app.Fragment mapFragment;
    private GoogleApiClient apiClient;
    private LocationRequest request;
    private Location startLocation;
    private Location curr;

    private List<LatLng> track;
    private String trackPath = "";
    private FragmentTransaction transaction;

    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    Handler handler = new Handler();


    public Timer_GpsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_timer,container,false);
        View view = binding.getRoot();
        initGoogleApi();
        initAspects();
        listners();
        timer_start();



        return view;
    }

    private void initAspects() {

        speedoMeterFragment = new SpeedoMeterFragment();
        FragmentManager fragment = getFragmentManager();
        FragmentTransaction transaction = fragment.beginTransaction();
        transaction.replace(R.id.timer_container, speedoMeterFragment);
        transaction.commit();

        track = new ArrayList<LatLng>();

        // Initialize map Fragment
        if(startLocation != null) {
            mapFragment = new mapFragment(startLocation);
        } else {
            mapFragment = new mapFragment();
        }
    }

    private void showHideFragment(final Fragment fragment, Fragment fragment1) {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.timer_container,fragment, Constants.MAP_TAG);
        if (fragment.isHidden()) {
            ft.show(fragment);
            ft.hide(fragment1);
        } else {
            ft.hide(fragment);
            ft.show(fragment1);
        }

        ft.commit();
    }

    private void listners() {
        binding.layoutTimerSubReplace.btnGps.setOnClickListener(new View_changeListner());
        binding.layoutTimerSubReplace.timerStop.setOnClickListener(new CounterStopListner());
    }

    private mapFragment getMapFragment() {
        FragmentManager fm = getChildFragmentManager();
        mapFragment fragment = (mapFragment)fm.findFragmentByTag(Constants.MAP_TAG);
        return fragment;
    }

    private void startpostButtonListener() {
        binding.layoutTimerSubReplace.btnAddTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTrackParams model = new AddTrackParams();
                if (binding.layoutTimerSubReplace.trackName.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Add track name", Toast.LENGTH_SHORT).show();} else {
                    model.setTrack_name(binding.layoutTimerSubReplace.trackName.getText().toString());
                }
                model.setStart_latitude(startLocation.getLatitude());
                model.setStart_longitude(startLocation.getLongitude());
                model.setEnd_latitude(track.get(track.size() - 1).latitude);
                model.setEnd_longitude(track.get(track.size() - 1).longitude);
                model.setTrack_path(getTrackPath());
                // Dummy values
                //Todo change these values to actual
                model.setTrack_type(1); // 1 for simple run, 2 for ghots rider and 3 for catch me if you can
                model.setGenrated_by(48);
                model.setDistance(12);
                model.setCaleries_burnt(22);
                model.setTrack_time(12);
                AddTrackDAL.postTrack(model,getContext());

            }
        });
    }

    private void uiUpdate() {
        binding.layoutTimerSubReplace.btnAddTrack.setVisibility(View.VISIBLE);
        binding.layoutTimerSubReplace.layoutAddTrackname.setVisibility(View.VISIBLE);
    }

    private void saveRoute() {
        //Todo Saving Track to preference
        //Toast.makeText(getContext(), "save Route Succesfully", Toast.LENGTH_SHORT).show();
    }

    private void initGoogleApi() {
        if(apiClient == null) {
            apiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(getActivity(), this)
                    .build();
        }
        apiClient.connect();
        createLocationRequest();
    }

    private void createLocationRequest() {
        request = request.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000); //Request after every 5 second
    }

    private void cal_setCalories(double distance){                      // calculate calories and set it on view
        double burntCalories = 160*0.75*distance;                      // distance in Miles
        binding.layoutCounterCal.caloriesTv.setText(""+burntCalories);
    }

    private void timer_start() {
        if(t == 1){
            //timer_start will start
            starttime = SystemClock.uptimeMillis();
            handler.postDelayed(updateTimer, 0);
            t = 0;
        }
    }

    public Runnable updateTimer = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            binding.layoutCounterCal.countTimer.setText(""+ mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0);
        }

    };

    @Override
    public void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        apiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                                                                                       Manifest.permission.ACCESS_COARSE_LOCATION },
                        Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, request,new CustomLocationListener());
        startLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
    }

    /**
     * Converting LatLng list into single String
     * @return
     */
    public String getTrackPath() {
        for(LatLng latLng:track) {
            trackPath.concat(latLng.toString());
        }
        return trackPath;
    }


    private class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            LatLng current = new LatLng(location.getLatitude(),location.getLongitude());
            track.add(current);
            calculateDistance(location);
        }
    }

    private void calculateDistance(Location location) {
        double distance = startLocation.distanceTo(location);
        cal_setCalories(distance);
    }

    @Override
    public void onPause() {
        super.onPause();
        apiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        apiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiClient.stopAutoManage(getActivity());
        apiClient.disconnect();
    }


    private class View_changeListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            mapFragment mapFragment = new mapFragment();
//            FragmentManager fragment = getFragmentManager();
//            FragmentTransaction transaction = fragment.beginTransaction();
//            transaction.replace(R.id.timer_container, mapFragment);
//            transaction.commit();
            showHideFragment(mapFragment,speedoMeterFragment);
            getMapFragment().moveMapCamera(startLocation.getLatitude(),startLocation.getLongitude());
        }
    }

    private class CounterStopListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //timer_start will pause
            timeSwapBuff += timeInMilliseconds;
            handler.removeCallbacks(updateTimer);
            t = 1;

            if(getMapFragment().isHidden()) {
                showHideFragment(mapFragment,speedoMeterFragment);
                getMapFragment().moveMapCamera(startLocation.getLatitude(),startLocation.getLongitude());
            }

            getMapFragment().showRoute(track);
            saveRoute();
            uiUpdate();
            startpostButtonListener();
        }
    }
}
