package com.attribes.push2beat.services;

import android.content.Intent;
import android.os.Bundle;

import com.attribes.push2beat.Utils.ChallengeDialog;
import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.network.DAL.GetProfileDAL;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by android on 12/22/16.
 */

public class FirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        if (remoteMessage.getNotification() != null) {
//            handleRequest(remoteMessage.getData());
//        }
        if(remoteMessage.getData().size() > 0)
        {
            if(remoteMessage.getData().get("text").contains("beaten"))
            {
                restartCatchActivity(remoteMessage.getData().get("text"));
            }
            else {
                handleRequest(remoteMessage.getData());
            }
        }

    }




    private void handleRequest(Map<String, String> data) {
        if(data.get("challenger_id").equals(DevicePreferences.getInstance().getuser().getId()))
        {
            if(data.get("text").contains("accepted")) {
                requestUserDetail(data.get("challanged_person_id").toString());
            }
            else {
                restartCatchActivity(data.get("text"));

            }
        }
        else {
            Intent intent = new Intent(this, ChallengeDialog.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putBoolean("fromNotification",true);
            bundle.putString("text", data.get("text"));
            bundle.putString("challenger_id",data.get("challenger_id"));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }



    private void restartCatchActivity(String text) {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromendrun",true);
        bundle.putString("text",text);
        intent.putExtras(bundle);
        Common.getInstance().setRunType(3);
        startActivity(intent);
    }

    private void requestUserDetail(String opponentId) {
        GetProfileDAL.getProfileData(opponentId, new ProfileDataArrivalListner() {
            @Override
            public void onDataRecieved(MyProfileResponse.Data data) {
               startMainActivity(data);

            }

            @Override
            public void onEmptyData(String msg) {

            }
        });
    }

    private void startMainActivity(MyProfileResponse.Data data) {

        Intent intent = new Intent(this,MainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("id",data.getId());
        bundle.putBoolean("fromcatch",true);
        bundle.putString("email",data.getEmail());
        bundle.putString("latitude",data.getLattitude());
        bundle.putString("longitude",data.getLongitude());
        bundle.putString("username",data.getFirst_name());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtras(bundle);
        startActivity(intent);
    }
}
