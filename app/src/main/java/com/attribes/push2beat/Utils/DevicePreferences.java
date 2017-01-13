package com.attribes.push2beat.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.attribes.push2beat.models.BodyParams.SignInParams;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.quickblox.users.model.QBUser;

import java.util.List;

/**
 * Created by android on 12/8/16.
 */

public class DevicePreferences {

    public static final String preference = "mPrefernce";
    public static final String TRACK_KEY = "track";
    public static final String MUSIC_TRACK_KEY = "music_track";
    public static final String USER_KEY = "user";
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
        mPref = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
    }




    public void saverememberme(Boolean Isremember){
        SharedPreferences.Editor prefsEditor = mPref.edit();
        prefsEditor.putBoolean("MyBOOL", Isremember);
        prefsEditor.commit();


    }

    public void saveusers(SignInParams user)
    {
        SharedPreferences.Editor prefsEditor = mPref.edit();
        Gson gson = new Gson();

        String json = gson.toJson(user);
        prefsEditor.putString("UserObject", json);
        prefsEditor.commit();

    }


    public void saveQbuser(QBUser user)
    {
        SharedPreferences.Editor prefsEditor = mPref.edit();
        Gson gson = new Gson();

        String json = gson.toJson(user);
        prefsEditor.putString("qbuser", json);
        prefsEditor.commit();

    }


    public QBUser getQbUser()
    {
        Gson gson = new Gson();
        String json = mPref.getString("qbuser","");
        QBUser obj = gson.fromJson(json, QBUser.class);
        return obj;

    }


    public SignInParams getuser(){
        Gson gson = new Gson();
        String json = mPref.getString("UserObject", "");
        SignInParams obj = gson.fromJson(json, SignInParams.class);
        return obj;
    }

    public void saveLocation(Location location)
    {
        SharedPreferences.Editor editor = mPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        editor.putString("location",json);
        editor.commit();

    }


    public Location getLocation()
    {
        Gson gson = new Gson();
        String json = mPref.getString("location","");
        Location location = gson.fromJson(json,Location.class);
        return location;
    }

    public boolean isRemember ()
    {
        Boolean yourLocked = mPref.getBoolean("MyBOOL", false);
        return yourLocked;
    }




    public void  removeUserObject()
    {
        SharedPreferences.Editor prefsEditor = mPref.edit();
        prefsEditor.remove("UserObject").commit();
    }



//    public void setuserlogin(UserLogin userlogin)
//    {
//        SharedPreferences.Editor editor;
//        editor = mPref.edit();
//        Gson gson = new Gson();
//
//        String locationObject = gson.toJson(userlogin);
//        editor.putString(USER_KEY,locationObject);
//        editor.commit();
//    }


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
