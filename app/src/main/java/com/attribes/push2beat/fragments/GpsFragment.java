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
import android.util.Log;
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
import com.attribes.push2beat.models.StatsData;
import com.attribes.push2beat.network.DAL.AddTrackDAL;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.android.gms.location.LocationRequest.create;

/**
 * Created by Moiz on 12/8/16.
 */

public class GpsFragment extends android.support.v4.app.Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,CatchMeFragment.OnStartButtonListener,GhostRiderFragment.OnStartButtonListener{

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
    private int burntCalories = 0;
    private List<LatLng> track;
    private String trackPath = "";
    private boolean isSavedButtonClicked = false;
    private String trackId;
    public static final String SpeedTag= "speed";


    private boolean isCatchMeIfYouCan = false;
    //Timer Constants
    private List<Integer> speedList;

    private int speed = 0;
    private  long starttime = 0L;
    private  long timeInMilliseconds = 0L;
    private  long timeSwapBuff = 0L;
    private  long updatedtime = 0L;
    private  int t = 1;
    private  double distanceInMeter = 0;

    private   int secs = 0;
    private   int mins = 0;
    private   int milliseconds = 0;
    private   Handler handler = new Handler();
    private QBUser opponent;
    private QBChatService chatService;


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

        //getMapFragment().addOpponentMaker(Common.getInstance().getLocation().getLatitude(),Common.getInstance().getLocation().getLongitude());
        setMessageRecieverManager();


        return view;
    }

    private void initFragments() {
        if(startLocation != null)
        {mapFragment = new MapFragment(startLocation);}
        else {mapFragment = new MapFragment();}
        showHideFragment(mapFragment);


        speedMeterFragment = new SpeedoMeterFragment();
        FragmentManager fragment = getFragmentManager();
        FragmentTransaction transaction = fragment.beginTransaction();

        transaction.add(R.id.container_above, speedMeterFragment,SpeedTag);
        transaction.commit();




        // Initialize map Fragment

    }




    private void init() {
        track = new ArrayList<LatLng>();
        speedList = new ArrayList<Integer>();
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
            binding.layoutTimerSubReplace.btnGps.setBackgroundResource(R.drawable.timerbtn);
            ft.hide(fragment1);
        } else {
            ft.hide(fragment);
            binding.layoutTimerSubReplace.btnGps.setBackgroundResource(R.drawable.gps_button);
            ft.show(fragment1);
        }

        ft.commit();
    }

    private void showHideFragment(final Fragment fragment) {

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.container_above,fragment, Constants.MAP_TAG);
        if (fragment.isHidden()) {
            ft.show(fragment);

        } else {
            ft.hide(fragment);

        }

        ft.commit();
    }




    private void startButtons() {


        binding.layoutTimerSubReplace.btnGps.setOnClickListener(new GpsButtonListener());
        binding.layoutTimerSubReplace.timerStop.setOnClickListener(new StopButtonListener());
        binding.layoutCmiyc.cmGo.setOnClickListener(new CatchMeButtonListener());
        binding.layoutGhostrider.ghostGo.setOnClickListener(new GhostButtonListener());

    }


    public void resetScreensValues()
    {

        track.clear();
        totalDistance = 0;
        burntCalories = 0;
        distanceInMeter = 0;

        isCatchMeIfYouCan = false;

        isSavedButtonClicked =false;
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        t = 1;
        secs = 0;
        mins = 0;
        milliseconds = 0;


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
        binding.layoutTimerSubReplace.timerRecord.setVisibility(View.GONE);
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
     * Format : lng,lat_lng,lat_lng,lat_
     * Converting LatLng list into single String
     * @return
     */
    private String getTrackPath() {

       trackPath = String.valueOf(track.get(0).longitude)+",";

        for(LatLng latLng:track)
        {
          trackPath += String.valueOf(latLng.latitude)+"_";
          trackPath += String.valueOf(latLng.longitude)+",";
        }
        trackPath += String.valueOf(track.get(track.size()-1).latitude)+"_";
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
        burntCalories = (int)(160*0.75*distance);                      // distance in miles
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
        model.setDistance(totalDistance * 1609.344); // converting miles into meter
        model.setCaleries_burnt(burntCalories);
        model.setTrack_time(""+ mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds));
        model.setGenrated_by(Integer.parseInt(Common.getInstance().getUser().getId()));
        // Dummy values
        //Todo change these dummy values to actual
        model.setTrack_type(1); // 1 for simple run, 2 for ghots rider and 3 for catch me if you can


        AddTrackDAL.postTrack(model,getContext());


    }


    /**
     * This is a callback of child fragment Catch Me if you can
     * @param datum
     */
    @Override
    public void onStartCmiyc(Datum datum) {

      //  removeCatchMeFragment();
        initQBChat(datum.getEmail());
        getMapFragment().addOpponentMaker(Double.parseDouble(datum.getLat()),Double.parseDouble(datum.getLng()));
        updateUiforCmifyc();
        getChildFragmentManager().popBackStack();
        resetScreensValues();
        timer_start();
        showOpponentDetail(datum);
        isCatchMeIfYouCan = true;

        timer_start();
    }

    private void removeCatchMeFragment() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.remove(Common.getInstance().getFragmentStack().lastElement()).commit();
        Common.getInstance().getFragmentStack().pop();
    }

    private void initQBChat(String email) {

        QBChatService.setDebugEnabled(true); // enable chat logging
        QBChatService.setDefaultAutoSendPresenceInterval(10); //enable sending online status every 60 sec to keep connection alive
        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);


        getUserByEmail(email);

        QBChatService.getInstance().addConnectionListener(new CustomConnectionListener());


    }



    private void getUserByEmail(String email) {
        QBUsers.getUserByEmail(email).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {

                opponent = qbUser;
                createChatDialog(opponent.getId());

            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("mChat",""+e);
                //Todo show error mmessage to user
            }
        });
    }


    private void createChatDialog(int participantId) {

        QBRestChatService.createChatDialog(DialogUtils.buildPrivateDialog(participantId)).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {

              Common.getInstance().setQbChatDialog(qbChatDialog);

            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("mChat",""+e);
            }
        });
    }

    private void setMessageRecieverManager() {

        if(Common.getInstance().getChatService() !=null) {
            QBIncomingMessagesManager manager = Common.getInstance().getChatService().getIncomingMessagesManager();
            manager.addDialogMessageListener(new QBChatDialogMessageListener() {
                @Override
                public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                    String message = qbChatMessage.getBody();
                    String[] str = message.split(",");
                    double lat = Double.parseDouble(str[0]);
                    double lng = Double.parseDouble(str[1]);

                    if (isCatchMeIfYouCan) {
                        getMapFragment().moveOpponent(lat, lng);
                    }

                }

                @Override
                public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
                    Log.d("mChat", "" + e);
                }
            });

        }
    }
    private void showOpponentDetail(Datum datum) {
       binding.layoutCounterCal.userFname.setText(datum.getUser_name()+" "+datum.getUser_lastname());
       binding.layoutCounterCal.catchDistance.setText(Common.getInstance().calulateDistance(datum.getLat(),datum.getLng()));
    }

    private void updateUiforCmifyc() {
        binding.layoutTimerSubReplace.timeSubReplaceRow.setVisibility(View.VISIBLE);
        binding.layoutBelow.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.userRow.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.timerRow.setVisibility(View.GONE);
        binding.layoutGhostrider.ghostRow.setVisibility(View.GONE);
        binding.layoutCmiyc.cmiycRow.setVisibility(View.GONE);
    }

    /**
     * Todo show the data and update UI
     * this is a callback of child fragment Ghost Rider
     * @param datum
     */
    public void onStartGhostRider(com.attribes.push2beat.models.Response.TrackList.Datum datum) {
       Fragment fragment = getChildFragmentManager().findFragmentByTag(Constants.GHOST_TAG);
       FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.remove(fragment).commit();
        stopTimer();
        updateUIforGhostRider();
        trackId = datum.getId();
        if(datum.getTrack_path().equals("") == false) {
            List<LatLng> traker = Common.getInstance().convertStringIntoLatlng(datum.getTrack_path());
            if(getMapFragment().isHidden()) {
                showHideFragment(getMapFragment(),speedMeterFragment);
                getMapFragment().showRoute(traker);
            }
        }
    }

    private void updateUIforGhostRider() {
        binding.layoutTimerSubReplace.timerStop.setBackgroundResource(R.drawable.stop);
        binding.layoutTimerSubReplace.btnGps.setVisibility(View.GONE);
        binding.layoutTimerSubReplace.timerRecord.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.timerRow.setBackgroundColor(getResources().getColor(R.color.secondary_dark_grey));
        binding.layoutCounterCal.countTimer.setTextColor(getResources().getColor(R.color.white));
        binding.layoutCounterCal.clock.setImageDrawable(getResources().getDrawable(R.drawable.ghost_icon));
        binding.layoutGhostrider.ghostRow.setVisibility(View.GONE);
        binding.layoutCmiyc.cmiycRow.setVisibility(View.GONE);
        binding.layoutTimerSubReplace.timerRecord.setOnClickListener(new RecordButtonListener());
    }


    //====================================== Listeners==================================================================//


    private class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location curr) {
            Common.getInstance().setLocation(curr);
           // getMapFragment().addLocationToQb(curr);

            double distance = calculateDistance(curr,prev);
          //  if(distance > 5) { Todo uncomment it for accuracy
                totalDistance += distance / 1609.344; //miles
                distanceInMeter += distance;
                cal_setCalories(totalDistance);

                speedList.add(calculateSpeed());
                LatLng latLng = new LatLng(curr.getLatitude(),curr.getLongitude());
                track.add(latLng);
                prev = curr;
           // }

            if(isCatchMeIfYouCan)
            {
                QBChatMessage message = new QBChatMessage();
            if(Common.getInstance().getQbChatDialog() != null){
                message.setDialogId(Common.getInstance().getQbChatDialog().getDialogId());
                message.setRecipientId(Common.getInstance().getQbUser().getId());
                message.setBody(String.valueOf(curr.getLatitude()) + "," + String.valueOf(curr.getLongitude()));
                try {
                    Common.getInstance().getQbChatDialog().sendMessage(message);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                    Log.d("mChat",""+e);
                }
                }
            }


        }
    }

    private int calculateSpeed() {
      int hours = mins /60 + secs;
      int km = (int) (distanceInMeter / 1000);
        if(hours > 0){
          speed = km/hours;
            Common.getInstance().setSpeedValue(speed);
           }
        return speed;
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
                        Toast.makeText(getContext(), "Track name is empty", Toast.LENGTH_SHORT).show();
                    }
                    else{
                       callAddTrackApi();
                        startStatsScreen();


                    }
                } else {
                    binding.layoutCounterCal.layoutAddTrackname.setVisibility(View.VISIBLE);
                    binding.layoutCounterCal.timerRow.setVisibility(View.GONE);
                    isSavedButtonClicked = true;
                }
            }


        }

    private void startStatsScreen() {
        StatsData data = new StatsData();

        data.setPath(track);
        data.setTopSpeed(Collections.max(speedList));
        data.setAverageSpeed(calculateAverageSpeed(speedList));
        data.setTraveledDistance(totalDistance);
        data.setCalories(burntCalories);

        StatsFragment fragment = new StatsFragment(data);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Common.getInstance().getFragmentStack().add(fragment);
        ft.add(R.id.container_full,fragment,Constants.STATS_TAG).commit();

    }

    private double calculateAverageSpeed(List <Integer> speedList) {
        Integer sum = 0;
        if(!speedList.isEmpty()) {
            for (Integer mark : speedList) {
                sum += mark;
            }
            return sum.doubleValue() / speedList.size();
        }
        return sum;
    }


    private class GhostButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            Fragment fragment = new GhostRiderFragment();
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();

            ft.add(R.id.container_full,fragment,Constants.GHOST_TAG);
            Common.getInstance().getFragmentStack().push(fragment);
            ft.commit();
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

            ft.add(R.id.layout_below,fragment,Constants.CMIYC_TAG);
            Common.getInstance().getFragmentStack().push(fragment);



//            ft.add(R.id.container, resultListFragment);
//            fragmentStack.lastElement().onPause();
//            ft.hide(fragmentStack.lastElement());
//            fragmentStack.push(resultListFragment);

            ft.commit();
          //  binding.layoutBelow.setVisibility(View.GONE);
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
                stopTimer();
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


    }

    private void stopTimer() {
        timeSwapBuff += timeInMilliseconds;
        handler.removeCallbacks(updateTimer);
        t = 1;
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



    private class RecordButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {


            timer_start();
        }
    }
}
