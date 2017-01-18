package com.attribes.push2beat.Utils;

import android.content.Context;
import android.location.Location;

import com.attribes.push2beat.models.BodyParams.SignInParams;
import com.attribes.push2beat.models.CatchMeModel;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.google.android.gms.maps.model.LatLng;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 12/9/16.
 */

public class Common {
    private static Common Instance = null;
    private Location location;
    private int runType;
    private SignInParams user;
    private String password;
    private int SpeedValue;
    private String profile_image;
    private QBChatDialog qbChatDialog;
    private QBChatService chatService;
    private Datum opponentData;
    private CatchMeModel oppData;
    private boolean isCatchMeFromUser = false;
    private boolean isCatchMeFromNotification = false;
    private List<LatLng> ghostTrack;




    private Common() {
    }

    public static Common getInstance()
    {
        if(Instance == null)
        {
            Instance = new Common();
        }
        return Instance;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String calulateDistance(String lat, String lng) {
        Location userLocation = new Location("opponentLocation");
        userLocation.setLatitude(Double.parseDouble(lat));
        userLocation.setLongitude(Double.parseDouble(lng));
        int distance = (int) userLocation.distanceTo(DevicePreferences.getInstance().getLocation());
        return String.valueOf(distance)+"m";
    }


    public void initializeQBInstance(Context context)
    {
        QBSettings.getInstance().init(context, Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
      //  QBSession initialization

    }


    /**
     * ConvertString into a LatLng List
     * @param track_path
     * @return
     */
    public List<LatLng> convertStringIntoLatlng(String track_path) {
        List<String> latitudes = new ArrayList<>();
        List<String> longitudes = new ArrayList<>();
        List<LatLng> tracker =  new ArrayList<LatLng>();
        String[] commaSpliter = track_path.split(",");
        boolean isFirstValue = true;
        for (String latsLngs: commaSpliter)
        {
            if(isFirstValue)
            {
                longitudes.add(latsLngs);
                isFirstValue = false;
            }
            else {
                String[] underScoreSpliter = latsLngs.split("_");
                latitudes.add(underScoreSpliter[0]);
                if(commaSpliter[commaSpliter.length-1].equals(latsLngs) == false)
                {
                    longitudes.add(underScoreSpliter[1]);
                }
            }
        }
        if(latitudes.size()>1) {
            latitudes.add(0, latitudes.get(latitudes.size() - 1));
            latitudes.remove(latitudes.size() - 1);
        }
        for(int i=0;i<latitudes.size();i++)
        {
            LatLng latlng = new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i)));
            tracker.add(latlng);
        }

        return tracker;
    }

    public int getRunType() {
        return runType;
    }

    public void setRunType(int runType) {
        this.runType = runType;
    }

    public void setUser(SignInParams user) {
        this.user = user;
    }

    public SignInParams getUser()
    {

        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public int getSpeedValue() {
        return SpeedValue;
    }

    public void setSpeedValue(int speedValue) {
        SpeedValue = speedValue;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }


    public QBChatDialog getQbChatDialog() {
        return qbChatDialog;
    }

    public void setQbChatDialog(QBChatDialog qbChatDialog) {
        this.qbChatDialog = qbChatDialog;
    }

    public QBChatService getChatService() {
        return chatService;
    }

    public void setChatService(QBChatService chatService) {
        this.chatService = chatService;
    }

    public void setOpponentData(Datum opponentData) {
        this.opponentData = opponentData;
    }

    public Datum getOpponentData() {
        return opponentData;
    }


    public boolean isCatchMeFromUser() {
        return isCatchMeFromUser;
    }

    public void setCatchMeFromUser(boolean catchMeFromUser) {
        isCatchMeFromUser = catchMeFromUser;
    }

    public CatchMeModel getOppData() {
        return oppData;
    }

    public void setOppData(CatchMeModel oppData) {
        this.oppData = oppData;
    }

    public boolean isCatchMeFromNotification() {
        return isCatchMeFromNotification;
    }

    public void setCatchMeFromNotification(boolean catchMeFromNotification) {
        isCatchMeFromNotification = catchMeFromNotification;
    }

    public List<LatLng> getGhostTrack() {
        return ghostTrack;
    }

    public void setGhostTrack(List<LatLng> ghostTrack) {
        this.ghostTrack = ghostTrack;
    }


}
