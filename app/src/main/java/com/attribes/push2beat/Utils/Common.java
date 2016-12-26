package com.attribes.push2beat.Utils;

import android.content.Context;
import android.location.Location;

import com.attribes.push2beat.models.Response.UserSignUp.LoginData;
import com.google.android.gms.maps.model.LatLng;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 12/9/16.
 */

public class Common {
    private static Common Instance = null;
    private Location location;
    private QBUser qbUser;
    private int runType;
    private LoginData user;
    private String password;


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
        int distance = (int) userLocation.distanceTo(Common.getInstance().getLocation());
        return String.valueOf(distance)+"m";
    }


    public QBUser getQbUser() {
        return qbUser;
    }

    public void setQbUser(QBUser qbUser) {
        this.qbUser = qbUser;
    }

    public void initializeQBInstance(Context context)
    {
        QBSettings.getInstance().init(context, Constants.APP_ID, Constants.AUTH_KEY, Constants.AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(Constants.ACCOUNT_KEY);
      //  QBSessio

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
        latitudes.add(0,latitudes.get(latitudes.size()-1));
        latitudes.remove(latitudes.size()-1);

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

    public void setUser(LoginData user) {
        this.user = user;
    }

    public LoginData getUser()
    {

        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
