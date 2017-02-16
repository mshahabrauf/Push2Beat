package com.attribes.push2beat.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
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
import com.attribes.push2beat.Utils.CustomConnectionListener;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.databinding.FragmentTimerBinding;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.attribes.push2beat.models.BodyParams.AddTrackParams;
import com.attribes.push2beat.models.CatchMeModel;
import com.attribes.push2beat.models.StatsData;
import com.attribes.push2beat.network.DAL.AddTrackDAL;
import com.attribes.push2beat.network.DAL.ChallengeResultDAL;
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

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.location.LocationRequest.create;

/**
 * Created by Moiz on 12/8/16.
 */

public class GpsFragment extends android.support.v4.app.Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,GhostRiderFragment.OnStartButtonListener{

    private MapView mapView;
    private FragmentTimerBinding binding;
    private android.support.v4.app.Fragment mapFragment;
    private GoogleApiClient apiClient;
    private LocationRequest request;
    private Location startLocation;
    private Location curr;
    private Location prev;
    private boolean isCatchRunFinished = false;
    private SpeedoMeterFragment speedMeterFragment;

    private double totalDistance = 0;
    private int burntCalories = 0;
    private List<LatLng> track;
    private List<LatLng> ghostTrack;

    private boolean isCatchMeRunning = true;
    private String trackPath = "";
    private boolean isSavedButtonClicked = false;
    private String trackId;
    public static final String SpeedTag= "speed";


    private boolean isCatchMeIfYouCan = false;
    private boolean isGhostRider = false;
    private boolean isUserOnTrackPosition = false;
    //Timer Constants
    private List<Integer> speedList;

    private int speed = 0;
    private  long starttime = 0L;
    private  long timeInMilliseconds = 0L;
    private  long timeSwapBuff = 0L;
    private  long updatedtime = 0L;
    private  int t = 1;
    private  double distanceInMeter = 0;
    private int iterator = 0;
    private MediaPlayer mPlayer;
    private  int hrs = 0;
    private   int secs = 0;
    private   int mins = 0;

    private  int ghostHrs = 0;
    private   int ghostSecs = 0;
    private   int ghostMins = 0;

    private   int milliseconds = 0;
    private   Handler handler = new Handler();
    private QBUser opponent;
    private QBChatService chatService;


    public GpsFragment() {
        Common.getInstance().updateFragmentCounter();
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

    /**
     * this method is change screen UI according to run Type
     */
    private void setScreenForRunType() {
        switch ( Common.getInstance().getRunType())
        {
            case 0:
              //  ((MainActivity)getActivity()).showMusicIcon();
               // binding.pickMusic.setVisibility(View.VISIBLE);

                ((MainActivity)getActivity()).showMusicIcon();
                showGhostAndCMButton();
                break;
            case 1:
                hideGhostAndCMButton();
               // binding.pickMusic.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).playMusic();
                ((MainActivity)getActivity()).showMusicIcon();
                break;
            case 2:
                hideGhostAndCMButton();
               ghostButtonCaller();
                break;
            case 3:
                ((MainActivity)getActivity()).showMusicIcon();
                hideGhostAndCMButton();
                if(Common.getInstance().isCatchMeFromNotification() == false && Common.getInstance().isCatchMeFromUser() == false) {
                    catchmeScreenButtonCaller();
                }
                break;
            default:

                break;
        }


    }

    private void hideGhostAndCMButton() {
        binding.timerCmiyc.cmiycRow.setVisibility(View.GONE);
        binding.timerGhost.ghostRow.setVisibility(View.GONE);
    }

    private void showGhostAndCMButton() {
        binding.timerCmiyc.cmiycRow.setVisibility(View.VISIBLE);
        binding.timerGhost.ghostRow.setVisibility(View.VISIBLE);
        binding.timerGhost.ghostGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).hideMusicIcon();
                hideGhostAndCMButton();
                ghostButtonCaller();
                Common.getInstance().setRunType(2);
            }
        });

        binding.timerCmiyc.cmGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catchmeScreenButtonCaller();

                hideGhostAndCMButton();
                Common.getInstance().setRunType(3);
            }
        });
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

    }




    private void init() {
        DevicePreferences.getInstance().init(getActivity());

        mPlayer = new MediaPlayer();

        ((MainActivity)getActivity()).changeTitle("GPS");
        track = new ArrayList<LatLng>();
        ghostTrack = new ArrayList<LatLng>();
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
        hrs = 0;


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



    private void startpostButtonListener() {
        binding.layoutTimerSubReplace.btnAddTrack.setOnClickListener(new AddTrackButtonListener());
    }

    private void uiUpdate() {
        binding.layoutTimerSubReplace.timerRecord.setVisibility(View.GONE);
        binding.layoutTimerSubReplace.btnAddTrack.setVisibility(View.VISIBLE);
        binding.layoutTimerSubReplace.timerStop.setVisibility(View.GONE);
        binding.layoutTimerSubReplace.btnGps.setVisibility(View.GONE);



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
     * @param track
     */
    private String getTrackPath(List<LatLng> track) {

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
            secs = (int) (timeInMilliseconds / 1000);
            mins = secs / 60;
            hrs = mins / 60;
            mins = mins % 60;
            secs = secs % 60;

            milliseconds = (int) (updatedtime % 1000);
            binding.layoutCounterCal.countTimer.setText(""+ String.format("%02d", hrs) + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
            handler.postDelayed(this, 0);
            if(isUserOnTrackPosition)
            {
                if(hrs >= ghostHrs && mins >= ghostMins && secs >= ghostSecs)
                {

                    binding.layoutTimerSubReplace.timerStop.callOnClick();

                    Toast.makeText(getContext(), "Your time is up", Toast.LENGTH_SHORT).show();
                    isUserOnTrackPosition = false;
                    stopTimer();


                }

            }

        }

    };



    private void callAddTrackApi() {

        AddTrackParams model = new AddTrackParams();
        model.setTrack_name(binding.layoutCounterCal.trackName.getText().toString());
        if(Common.getInstance().getRunType() == 2)
        {
            model.setStart_latitude(ghostTrack.get(0).latitude);
            model.setStart_longitude(ghostTrack.get(0).longitude);
            model.setEnd_latitude(ghostTrack.get(ghostTrack.size() - 1).latitude);
            model.setEnd_longitude(ghostTrack.get(ghostTrack.size() - 1).longitude);
            model.setTrack_path(getTrackPath(ghostTrack));
        }else
        {
            model.setStart_latitude(track.get(0).latitude);
            model.setStart_longitude(track.get(0).longitude);
            model.setEnd_latitude(track.get(track.size() - 1).latitude);
            model.setEnd_longitude(track.get(track.size() - 1).longitude);
            model.setTrack_path(getTrackPath(track));
        }


        model.setDistance(totalDistance * 1609.344); // converting miles into meter
        model.setCaleries_burnt(burntCalories);
        model.setTrack_time(""+ hrs + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs));
        model.setGenrated_by(Integer.parseInt(DevicePreferences.getInstance().getuser().getId()));
        if(Common.getInstance().getRunType() == 0)
        {
            model.setTrack_type(1);
        }
        else {
            model.setTrack_type(Common.getInstance().getRunType()); // 1 for simple run, 2 for ghots rider and 3 for catch me if you can
        }
        model.setTop_speed(Collections.max(speedList));
        model.setAverage_speed(calculateAverageSpeed(speedList));

        AddTrackDAL.postTrack(model,getContext());
        //((MainActivity)getActivity()).refreshPager();


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

        QBChatService.getInstance().addConnectionListener(new CustomConnectionListener(getContext()));


    }



    private void getUserByEmail(String email) {
        QBUsers.getUserByEmail(email).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {

                opponent = qbUser;
                createChatDialog(opponent.getId());
                isCatchMeIfYouCan = true;
                setMessageRecieverManager();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("mChat",""+e);
                Toast.makeText(getContext(), "Connection failed:Check your internet", Toast.LENGTH_SHORT).show();
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

        if(Common.getInstance().getChatService() !=null && isCatchMeIfYouCan) {
            final QBIncomingMessagesManager manager = Common.getInstance().getChatService().getIncomingMessagesManager();
            manager.addDialogMessageListener(new QBChatDialogMessageListener() {
                @Override
                public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                   if(isCatchMeRunning) {
                       String message = qbChatMessage.getBody();
                       String[] str = message.split(",");
                       double lat = Double.parseDouble(str[0]);
                       double lng = Double.parseDouble(str[1]);
                       Location opponent = new Location("a");
                       opponent.setLatitude(lat);
                       opponent.setLongitude(lng);
                       double opponentDistance = calculateDistance(opponent, DevicePreferences.getInstance().getLocation());
                       binding.layoutCounterCal.catchDistance.setText(String.valueOf(Math.round(opponentDistance * 100) / 100) + "m");
                       if (opponentDistance < 5) {
                           isCatchMeIfYouCan = false;
                           isCatchMeRunning = false;
                           binding.layoutTimerSubReplace.timerStop.callOnClick();
                           if (Common.getInstance().isCatchMeFromNotification()) {
                               ChallengeResultDAL.sendResultChallenge(DevicePreferences.getInstance().getuser().getId(), Common.getInstance().getOppData().getId());
//                               ChallengeResultDAL.sendResultChallenge(Common.getInstance().getOppData().getId(),DevicePreferences.getInstance().getuser().getId());
                               manager.removeDialogMessageListrener(this);
                           }
//                        else if(Common.getInstance().isCatchMeFromUser())
//                        {
//
//                            manager.removeDialogMessageListrener(this);
//                        }

                       }

                       getMapFragment().addOpponentMaker(opponent.getLatitude(), opponent.getLongitude());
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




    private void updateUiforCmifyc() {
        //((MainActivity)getActivity()).showMusicIcon();
        //binding.pickMusic.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).changeTitle("Catch Me If You Can");
       // binding.layoutTimerSubReplace.timerStop.setVisibility(View.VISIBLE);
       // binding.layoutTimerSubReplace.btnGps.setVisibility(View.VISIBLE);
        binding.layoutTimerSubReplace.timeSubReplaceRow.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.userRow.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.timerRow.setVisibility(View.GONE);

    }

    private void startLoaderFragment() {
        LoaderFragment fragment = new LoaderFragment("Waiting for opponent...");
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.container_full,fragment).commit();

    }

    /**
     * this is a callback of child fragment Ghost Rider
     * @param datum
     */

    public void onStartGhostRider(com.attribes.push2beat.models.Response.TrackList.Datum datum) {

        stopTimer();
        removeGhostFragment();
        updateUIforGhostRider();

        breakStringintoHrsMinSec(datum.getTrack_time());


        trackId = datum.getId();
        if(datum.getTrack_path().equals("") == false) {
            List<LatLng> traker = Common.getInstance().convertStringIntoLatlng(datum.getTrack_path());
            Common.getInstance().setGhostTrack(traker);
            if(getMapFragment().isHidden()) {
                showHideFragment(getMapFragment(),speedMeterFragment);
            }
            getMapFragment().showRoute(traker);
            getMapFragment().updateUIonGhostRider(datum.getTrack_name(),datum.getTrack_time());
        }
        isGhostRider = true;

    }

    private void breakStringintoHrsMinSec(String tracktime) {
        String[] split = tracktime.split(":");
        ghostHrs = Integer.parseInt(split[0]);
        ghostMins =  Integer.parseInt(split[1]);
        ghostSecs = Integer.parseInt(split[2]);
    }

    private void removeGhostFragment() {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(Constants.GHOST_TAG);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    private void updateUIforGhostRider() {
  //      binding.layoutTimerSubReplace.timerStop.setBackgroundResource(R.drawable.stop_ghost);
        ((MainActivity)getActivity()).showMusicIcon();
        //binding.pickMusic.setVisibility(View.VISIBLE);
        binding.layoutTimerSubReplace.btnGps.setVisibility(View.GONE);
        //binding.layoutTimerSubReplace.timerRecord.setVisibility(View.VISIBLE);
        binding.layoutCounterCal.timerRow.setBackgroundColor(getResources().getColor(R.color.secondary_dark_grey));
        binding.layoutCounterCal.countTimer.setTextColor(getResources().getColor(R.color.white));
        binding.layoutCounterCal.clock.setImageDrawable(getResources().getDrawable(R.drawable.ghost_icon));

      //  binding.layoutTimerSubReplace.timerRecord.setOnClickListener(new RecordButtonListener());
    }


    //====================================== Listeners==================================================================//


    private class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location curr) {
            DevicePreferences.getInstance().saveLocation(curr);
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

            if(Common.getInstance().isOpponentLeave())
            {
                binding.layoutTimerSubReplace.timerStop.callOnClick();
            }
            if(isCatchMeIfYouCan)
            {
                if(Common.getInstance().isOpponentLeave())
                {
                    isCatchMeIfYouCan = false;
                    isCatchMeRunning = false;
                    binding.layoutTimerSubReplace.timerStop.callOnClick();
                }

                QBChatMessage message = new QBChatMessage();
                if(Common.getInstance().getQbChatDialog() != null){
                message.setDialogId(Common.getInstance().getQbChatDialog().getDialogId());
                message.setRecipientId(opponent.getId());
                message.setBody(String.valueOf(curr.getLatitude()) + "," + String.valueOf(curr.getLongitude()));
                try {
                    Common.getInstance().getQbChatDialog().sendMessage(message);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                    Log.d("mChat",""+e);
                }catch (Exception e) {
                    Log.d("chat",""+e.getMessage());
                }
                }
            }

            if(isGhostRider) {
                Location trackLocation = new Location("");
                trackLocation.setLatitude(Common.getInstance().getGhostTrack().get(iterator).latitude);
                trackLocation.setLongitude(Common.getInstance().getGhostTrack().get(iterator).longitude);
                double startTrackDifference = calculateDistance(curr,trackLocation);
                if(startTrackDifference < 20)
                {
                    isUserOnTrackPosition = true;
                    resetScreensValues();
                    timer_start();
                    isGhostRider = false;
                }
                else
                {
                    Toast.makeText(getContext(), "You are not on Track starting position", Toast.LENGTH_SHORT).show();
                }


            }
            if(isUserOnTrackPosition)
            {

                int size = Common.getInstance().getGhostTrack().size() - 1;
                if (iterator < size) {

                    Location trackLocation = new Location("");
                    trackLocation.setLatitude(Common.getInstance().getGhostTrack().get(iterator).latitude);
                    trackLocation.setLongitude(Common.getInstance().getGhostTrack().get(iterator).longitude);


                    double trackDifference = calculateDistance(curr, trackLocation);
                    if (trackDifference < 5) {
                        LatLng ghostpoint = new LatLng(curr.getLatitude(),curr.getLongitude());
                        ghostTrack.add(ghostpoint);
                        iterator++;
                    }

                    else if(trackDifference >5 && trackDifference < 20) {
                        Toast.makeText(getContext(), "You are out of Track", Toast.LENGTH_SHORT).show();
                    }
                    else if(trackDifference > 20)
                    {
                        showGhostEndDialogTrack();
                        isUserOnTrackPosition = false;
                    }
                }
                else if (iterator == size)
                {
                    Toast.makeText(getContext(), "Track is Completed", Toast.LENGTH_SHORT).show();
                    binding.layoutTimerSubReplace.timerStop.callOnClick();

                }
            }


        }
    }

    private void showGhostEndDialogTrack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ghost Rider");
        builder.setMessage(" You are out of track and can't continue it further");
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog  = builder.create();
        alertDialog.show();
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
                    ((MainActivity) getActivity()).changeTitle("Save Your Track");
                    binding.layoutCounterCal.layoutAddTrackname.setVisibility(View.VISIBLE);
                    binding.layoutCounterCal.timerRow.setVisibility(View.GONE);
                    isSavedButtonClicked = true;
                }
            }


        }



    private void startStatsScreen() {
        Common.getInstance().setOnSaveState(false);

        StatsData data = new StatsData();

        data.setPath(track);
        data.setTopSpeed(Collections.max(speedList));
        data.setAverageSpeed(calculateAverageSpeed(speedList));
        data.setTraveledDistance(Math.round(totalDistance * 100) / 100);
        data.setCalories(burntCalories);
        data.setTrackname(binding.layoutCounterCal.trackName.getText().toString());



        StatsFragment fragment = new StatsFragment(data);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

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



    private void ghostButtonCaller()
    {
            startGhostFragment();
     //   binding.pickMusic.setVisibility(View.GONE);
    }




    private void startCatchMeFragment() {
        ((MainActivity) getActivity()).changeTitle("Catch Me If You Can");
        Fragment fragment = new CatchMeFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.container_full,fragment,Constants.CMIYC_TAG);
        ft.commit();
    }

    private void startGhostFragment() {
        ((MainActivity) getActivity()).changeTitle("Ghost Rider");
        Fragment fragment = new GhostRiderFragment();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.container_full,fragment,Constants.GHOST_TAG);
        ft.commit();
    }



    private void catchmeScreenButtonCaller()
    {

            startCatchMeFragment();
      //  binding.pickMusic.setVisibility(View.GONE);

    }

    private class StopButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            ((MainActivity)getActivity()).stopMusic();
            Common.getInstance().setOnSaveState(true);

            if (track.size() == 0 && !isGhostRider && !isUserOnTrackPosition) {
                Toast.makeText(getContext(), "No Track found", Toast.LENGTH_SHORT).show();
            }
            else if(ghostTrack.size() == 0 && (isGhostRider || isUserOnTrackPosition) )
            {
                Toast.makeText(getContext(), "No Track found", Toast.LENGTH_SHORT).show();

            }
            else {


                ((MainActivity) getActivity()).changeTitle("Great Work!");
                stopTimer();
                if(getMapFragment().isHidden())
                {showHideFragment(getMapFragment(),speedMeterFragment);
                getMapFragment().moveMapCamera(startLocation.getLatitude(),startLocation.getLongitude());}
                getMapFragment().showRoute(track);
                if(!isGhostRider && !isCatchMeIfYouCan) {
                    getMapFragment().updateUIonStop(String.valueOf(Math.round(totalDistance * 100) / 100), String.valueOf(speed));
                }
                if(isCatchMeIfYouCan && Common.getInstance().isOpponentLeave() == false)
                {
                    isCatchMeIfYouCan = false;
                    isCatchMeRunning = false;//Common.getInstance().setCatchMeFromNotification(false);
                    ChallengeResultDAL.sendResultChallenge(Common.getInstance().getOppData().getId(),DevicePreferences.getInstance().getuser().getId());
                }
                saveRoute();
                uiUpdate();
                startpostButtonListener();
                apiClient.disconnect(); // to stop getting any further locations Todo :Should connect again

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
        DevicePreferences.getInstance().saveLocation(startLocation);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK  && requestCode==5) {
            Uri uri = data.getData();
            DevicePreferences.getInstance().saveMusicTrackPath(uri.toString());
            //mPlayer.reset();

           // stopMusic();
            ((MainActivity)getActivity()).playMusic();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        apiClient.connect();

    }

    @Override
    public void onResume() {
        super.onResume();

        apiClient.connect();
        setScreenForRunType();
        if(Common.getInstance().isCatchMeFromUser() || Common.getInstance().isCatchMeFromNotification()){
            onStartCmiyc(Common.getInstance().getOppData());

        }
    }

    private void onStartCmiyc(CatchMeModel oppData) {


        if(getMapFragment().isHidden())
        {
            showHideFragment(getMapFragment(),speedMeterFragment);
        }

        getMapFragment().removeTrackMarkers();
        getMapFragment().addOpponentMaker(oppData.getLatitude(),oppData.getLongitude());

        initQBChat(oppData.getEmail());

        updateUiforCmifyc();

        resetScreensValues();

        binding.layoutCounterCal.userFname.setText(oppData.getUsername());
        binding.layoutCounterCal.catchDistance.setText(Common.getInstance().calulateDistance(String.valueOf(oppData.getLatitude()),String.valueOf(oppData.getLongitude())));


        timer_start();
    }



    @Override
    public void onPause() {
        super.onPause();
        apiClient.disconnect();
        ((MainActivity)getActivity()).stopMusic();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).stopMusic();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiClient.disconnect();
        ((MainActivity)getActivity()).stopMusic();
    }



    //===========================Request Permissions Callback===================================//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

            } else {
                        ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                        Constants.MY_PERMISSIONS_REQUEST_READ_LOCATION);
            }
        }
    }







}
