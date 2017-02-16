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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        if (remoteMessage.getNotification() != null) {
//            handleRequest(remoteMessage.getData());
//        }
        if(remoteMessage.getData().size() > 0)
        {
            if(remoteMessage.getData().get("text").contains("beaten"))
            {
                showNotification(remoteMessage.getData().get("text"));
                Common.getInstance().setOpponentLeave(true);
               // restartCatchActivity(remoteMessage.getData().get("text"));
            }
            else {
                handleRequest(remoteMessage.getData());
                showNotification(remoteMessage.getData().get("text"));
            }
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
        if(data.get("challenger_id").equals(DevicePreferences.getInstance().getuser().getId()))
        {
            if(data.get("text").contains("accepted")) {
                startDialogAcitivity(data,false);
              //  requestUserDetail(data.get("challanged_person_id").toString());
            }
            else {
                restartCatchActivity(data.get("text"));

            }
        }
        else {
            startDialogAcitivity(data,true);

        }
    }

    private void startDialogAcitivity(Map<String, String> data, boolean isFromNotification) {
        Intent intent = new Intent(this, ChallengeDialog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromNotification",isFromNotification);
        bundle.putString("text", data.get("text"));
        bundle.putString("challenger_id",data.get("challenger_id"));
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
