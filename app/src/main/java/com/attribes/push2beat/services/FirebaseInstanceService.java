package com.attribes.push2beat.services;

import android.widget.Toast;

import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.interfaces.MyCallBacks;
import com.attribes.push2beat.models.Response.UpdateToken;
import com.attribes.push2beat.network.DAL.UpdateDeviceTokenDAL;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by android on 12/22/16.
 */

public class FirebaseInstanceService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        DevicePreferences.getInstance().init(getApplicationContext());
        String token =  FirebaseInstanceId.getInstance().getToken();
       //here we send refresh token to server
        if(DevicePreferences.getInstance().getuser()!=null)
        {
            sendTokenToAppServer(DevicePreferences.getInstance().getuser().getId(), token);
        }

    }

    private void sendTokenToAppServer(String user_id,String token)
    {
        UpdateDeviceTokenDAL.updateToken(user_id, token, new MyCallBacks<UpdateToken>()
        {
            @Override
            public void onSuccess(UpdateToken data)
            {
                Toast.makeText(getApplicationContext(),data.msg,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(String message)
            {

            }
        });
    }

}
