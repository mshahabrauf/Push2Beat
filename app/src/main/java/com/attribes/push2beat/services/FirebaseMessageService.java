package com.attribes.push2beat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.attribes.push2beat.R;
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

    private MyProfileResponse.Data data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {

        //handles when push recieves an data
        if(remoteMessage.getData().size() > 0)
        {
            handleRequest(remoteMessage.getData());   // Other than ending push
        }

    }

    private void showNotification(String text) {
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri notificationsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder nBuilder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.catch_me_icon)
                .setContentTitle("Catch me if you can")
                .setContentText(text)
                .setSound(notificationsound)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);
        // .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(),nBuilder.build());
    }


    private void handleRequest(Map<String, String> data) {
        if(data.get("text").contains("beaten")) // if push contains beaten words means Catch me if you can Ending Push
        {
            Common.getInstance().setOpponentLeave(true);
        }
        else if(data.get("challenger_id").equals(DevicePreferences.getInstance().getuser().getId()))  // if Challenger Id is my Id then ITs a Challenge Resonpse push
        {
            if(data.get("text").contains("accepted"))
            {   // if opponent accept Challenge
                startDialogAcitivity(data,false,data.get("challanged_person_id"));
            if(data.get("text").contains("accepted")) {
                requestUserDetail(data.get("challanged_person_id").toString()); //accept Request
            }
            else
            {
                restartCatchActivity(data.get("text"));  // if opponent reject Challenge
            else {
                restartCatchActivity(data.get("text")); //Reject

            }
        }
        else
        {
            startDialogAcitivity(data,true, data.get("challenger_id"));  // else this is Challenge Push to the opponent
        }
        showNotification(data.get("text"));
    }

    private void startDialogAcitivity(Map<String, String> data, boolean isFromNotification, String openentId) {
        Intent intent = new Intent(this, ChallengeDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromNotification",isFromNotification);
        bundle.putString("text", data.get("text"));
        bundle.putString("challenger_id",openentId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void startMainActivityForUser(Map<String, String> data) {
        Intent intent = new Intent(this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromcatch",true);
        bundle.putString("challenger_id",data.get("challenger_id"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
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

            @Override
            public void onFailure() {

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
