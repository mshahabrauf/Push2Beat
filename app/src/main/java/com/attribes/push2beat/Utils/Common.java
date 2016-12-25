package com.attribes.push2beat.Utils;

import android.location.Location;

/**
 * Created by android on 12/9/16.
 */

public class Common {
    private static Common Instance = null;
    private Location location;
    private int SpeedValue;
    private String profile_image;


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

}
