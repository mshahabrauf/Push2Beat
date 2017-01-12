package com.attribes.push2beat.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by android on 12/8/16.
 */

public class DevicePreferences {

    public static final String preference = "mPrefernce";
    public static final String TRACK_KEY = "track";
    public static final String MUSIC_TRACK_KEY = "music_track";
    public static DevicePreferences instance = null;
    public static SharedPreferences mPref;
    public Context context;


    private DevicePreferences(){}

    public static DevicePreferences getInstance()
    {
        if(instance == null)
        {
            instance = new DevicePreferences();
        }

        return instance;
    }


    public void init(Context context)
    {
        this.context = context;
        mPref = context.getSharedPreferences(preference,context.MODE_PRIVATE);
    }



    public void savetrack(List<LatLng> listlocation)
    {
        SharedPreferences.Editor editor;
        editor = mPref.edit();
        Gson gson = new Gson();

        String locationObject = gson.toJson(listlocation);
        editor.putString(TRACK_KEY,locationObject);
        editor.commit();
    }


    public List<LatLng> getTrack()
    {
        Gson gson = new Gson();
        String list = mPref.getString(TRACK_KEY,null);
        List<LatLng> location = gson.fromJson(list,null);
        return  location;
    }


    public void save_musicTrackPath(String music_path){

        SharedPreferences.Editor editor;
        editor = mPref.edit();
        editor.putString(MUSIC_TRACK_KEY,music_path);
        editor.commit();
    }

    public String getMusicTrackPath(){

        String music_path = mPref.getString(MUSIC_TRACK_KEY,null);
        return music_path;
    }


}
