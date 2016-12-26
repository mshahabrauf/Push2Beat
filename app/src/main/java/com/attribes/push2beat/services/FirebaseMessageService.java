package com.attribes.push2beat.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.attribes.push2beat.R;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by android on 12/22/16.
 */

public class FirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().size() > 0){
            displayNotification(remoteMessage.getData());
        }
    }

    private void displayNotification(Map<String, String> data) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri notificationsound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder nBuilder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.p2b_icon)
                .setContentTitle("Catch me if you can!")
                .setContentText("Are you ready to beat "+data.get("username"))
                .setAutoCancel(false)
                .addAction(R.drawable.start_button,"Start",pendingIntent)
                .setSound(notificationsound)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(),nBuilder.build());
    }


}
