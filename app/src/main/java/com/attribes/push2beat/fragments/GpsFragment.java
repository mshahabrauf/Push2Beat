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
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.Constants;
import com.attribes.push2beat.databinding.FragmentTimerBinding;
import com.attribes.push2beat.models.BodyParams.AddTrackParams;
import com.attribes.push2beat.models.Response.UserList.Datum;
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

import static com.google.android.gms.location.LocationRequest.create;

/**
 * Created by Moiz on 12/8/16.
 */

public class GpsFragment extends android.support.v4.app.Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,CatchMeFragment.OnStartButtonListener{

    private MapView mapView;
    private FragmentTimerBinding binding;
    private android.support.v4.app.Fragment mapFragment;
    private GoogleApiClient apiClient;
    private LocationRequest request;
    private Location startLocation;
    private Location curr;
    private Location prev;
    private SpeedoMeterFragment speedMeterFragment;

    private double totalDistance = 0;
    private List<LatLng> track;
    private String trackPath = "";
    private boolean isSavedButtonClicked = false;


    //Timer Constants
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    Handler handler = new Handler();
    private String SpeedTag =  "speed";



    public GpsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_timer,container,false);
        View view = binding.getRoot();
        initGoogleApi();
        init();
        initFragments();
        startButtons();
        timer_start();

        return view;
    }

    private void initFragments() {

        speedMeterFragment = new SpeedoMeterFragment();
        FragmentManager fragment = getFragmentManager();
        FragmentTransaction transaction = fragment.beginTransaction();
        transaction.add(R.id.container_above, speedMeterFragment,SpeedTag);
        transaction.commit();




        // Initialize map Fragment
        if(startLocation != null)
        {mapFragment = new MapFragment(startLocation);}
        else {mapFragment = new MapFragment();}
        showHideFragment(mapFragment,speedMeterFragment);


    }




    private void init() {
        track = new ArrayList<LatLng>();

    }


    /**
     * This method Hide and Show Fragments
     * @param fragment
     * @param fragment1
     */
    private void showHideFragment(final Fragment fragment, Fragment fragment1) {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.container_above,fragment, Constants.MAP_TAG);
        if (fragment.isHidden()) {
            ft.show(fragment);
            ft.hide(fragment1);
        } else {
            ft.hide(fragment);
            ft.show(fragment1);
        }

        ft.commit();
    }


    private void startButtons() {

        binding.layoutTimerSubReplace.btnGps.setOnClickListener(new GpsButtonListener());
        binding.layoutTimerSubReplace.timerStop.setOnClickListener(new StopButtonListener());
        binding.layoutCmiyc.cmGo.setOnClickListener(new CatchMeButtonListener());
        binding.layoutGhostrider.ghostGo.setOnClickListener(new GhostButtonListener());

    }




    /**
     * This method return Map Fragment
     * @return
     */
    private MapFragment getMapFragment()
    {
        FragmentManager fm = getChildFragmentManager();
        MapFragment fragment = (MapFragment)fm.findFragmentByTag(Constants.MAP_TAG);
        return fragment;
    }

    private SpeedoMeterFragment getSpeedMeterFragment()
    {
        FragmentManager fm = getChildFragmentManager();
        SpeedoMeterFragment fragment = (SpeedoMeterFragment)fm.findFragmentByTag(SpeedTag);
        return fragment;
    }

    private void startpostButtonListener() {
        binding.layoutTimerSubReplace.btnAddTrack.setOnClickListener(new AddTrackButtonListener());
    }

    private void uiUpdate() {

        binding.layoutTimerSubReplace.btnAddTrack.setVisibility(View.VISIBLE);
        binding.layoutTimerSubReplace.timerStop.setVisibility(View.GONE);
        binding.layoutTimerSubReplace.btnGps.setVisibility(View.GONE);
        binding.layoutCmiyc.cmiycRow.setVisibility(View.GONE);
        binding.layoutGhostrider.ghostRow.setVisibility(View.GONE);

    }


    private void saveRoute(){
        //Todo Saving Track to preference
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
        request = create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000); //Request after every 5 second
    }



    /**
     * Todo // Convert according to this Format lat,lat_lng,lat_lng,lng
     * Converting LatLng list into single String
     * @return
     */
    private String getTrackPath() {
        for(LatLng latLng:track)
        {
            trackPath.concat(latLng.toString());
        }
        return trackPath;
    }




    /**
     * this method calculating distance from start position to the location he/she is right now
     * @param current
     */
    private double calculateDistance(Location current,Location prev) {
       return  prev.distanceTo(current);// this Distance is in Meter / 1000; // convert Distance into km

    }


    private void cal_setCalories(double distance){                      // calculate calories and set it on view
       int burntCalories = (int)(160*0.75*distance);                      // distance in miles
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

    private Runnable updateTimer = new Runnable() {

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



    private void callAddTrackApi() {
        AddTrackParams model = new AddTrackParams();
        model.setTrack_name(binding.layoutCounterCal.trackName.getText().toString());
        model.setStart_latitude(startLocation.getLatitude());
        model.setStart_longitude(startLocation.getLongitude());
        model.setEnd_latitude(track.get(track.size() - 1).latitude);
        model.setEnd_longitude(track.get(track.size() - 1).longitude);
        model.setTrack_path(getTrackPath());
        // Dummy values
        //Todo change these dummy values to actual
        model.setTrack_type(1); // 1 for simple run, 2 for ghots rider and 3 for catch me if you can
        model.setGenrated_by(48);
        model.setDistance(12);
        model.setCaleries_burnt(22);
        model.setTrack_time(12);
        AddTrackDAL.postTrack(model,getContext());

    }


    @Override
    public void onStartCmiyc(Datum datum) {

        updateUiforCmifyc();
        showOpponentDetail(datum);


    }

    private void showOpponentDetail(Datum datum) {
       binding.layoutCounterCal.userFname.setText(datum.getUser_name()+" "+datum.getUser_lastname());
       binding.layoutCounterCal.catchDistance.setText(Common.getInstance().calulateDistance(datum.getLat(),datum.getLng()));
    }

    private void updateUiforCmifyc() {
        binding.layoutBelow.setVisibility(View.VISIBLE);
        binding.layoutTimerSubReplace.timeSubReplaceRow.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.userRow.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.timerRow.setVisibility(View.GONE);
        binding.layoutGhostrider.ghostRow.setVisibility(View.GONE);
        binding.layoutCmiyc.cmiycRow.setVisibility(View.GONE);
    }


    //====================================== Listeners==================================================================//


    private class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location curr) {
            Common.getInstance().setLocation(curr);
            getMapFragment().addLocationToQb(curr);

            double distance = calculateDistance(curr,prev);
          //  if(distance > 5) {
                totalDistance += distance * 0.00062137;  // multiplying By this value is converting it into Miles
                cal_setCalories(totalDistance);
                LatLng latLng = new LatLng(curr.getLatitude(),curr.getLongitude());
                track.add(latLng);
                prev = curr;
           // }


            //getSpeedMeterFragment().setSpeed((int) curr.getSpeed()); //For Speed

        }
    }


    private class GpsButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showHideFragment(mapFragment,speedMeterFragment);
            getMapFragment().moveMapCamera(startLocation.getLatitude(),startLocation.getLongitude());
        }
    }

    private class AddTrackButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

                if (isSavedButtonClicked) {

                    if (binding.layoutCounterCal.trackName.getText().toString().equals("")) {

                    } else {
                        callAddTrackApi();
                    }//apiClient.connect();
                } else {
                    binding.layoutCounterCal.layoutAddTrackname.setVisibility(View.VISIBLE);
                    binding.layoutCounterCal.timerRow.setVisibility(View.GONE);
                    isSavedButtonClicked = true;
                }
            }


        }


    private class GhostButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

        }
    }

    private class CatchMeButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if(getMapFragment().isHidden())
            {showHideFragment(getMapFragment(),speedMeterFragment);}
            getMapFragment().removeTrackMarkers();

            Fragment fragment = new CatchMeFragment();
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();

            ft.add(R.id.container_below,fragment,Constants.CMIYC_TAG).commit();
            binding.layoutBelow.setVisibility(View.GONE);
            getMapFragment().moveMapCamera(Common.getInstance().getLocation().getLatitude(),Common.getInstance().getLocation().getLatitude());
        }


    }

    private class StopButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (track.isEmpty()) {
                Toast.makeText(getContext(), "No Track found", Toast.LENGTH_SHORT).show();
            }
            else {
                timeSwapBuff += timeInMilliseconds;
                handler.removeCallbacks(updateTimer);
                t = 1;

                if(getMapFragment().isHidden())
                {showHideFragment(getMapFragment(),speedMeterFragment);
                    getMapFragment().moveMapCamera(startLocation.getLatitude(),startLocation.getLongitude());}
                getMapFragment().showRoute(track);
                saveRoute();
                uiUpdate();
                startpostButtonListener();
                apiClient.disconnect(); // to stop getting any further locations Todo :Should connect again

            }
        }
    }





    // ============================Google Location Api Callbacks=======================================//





    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, request,new CustomLocationListener());
        startLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);
        Common.getInstance().setLocation(startLocation);
        getMapFragment().addLocationToQb(startLocation);
        prev = startLocation;

    }

    @Override
    public void onConnectionSuspended(int i) {
        apiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        apiClient.disconnect();
    }



//=========================Activity Lifecycle Callbacks=================================//



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
    public void onPause() {
        super.onPause();
        apiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiClient.stopAutoManage(getActivity());
        apiClient.disconnect();
    }



    //===========================Request Permissions Callback===================================//

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



}
