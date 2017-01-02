package com.attribes.push2beat.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.attribes.push2beat.R;
import com.attribes.push2beat.mainnavigation.MainActivity;
import com.attribes.push2beat.models.Response.PushFireBase.Data;
import com.attribes.push2beat.models.Response.PushFireBase.PushData;
import com.attribes.push2beat.network.DAL.SendPush;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by android on 12/22/16.
 */

public class FirebaseMessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if(remoteMessage.getData().size() > 0)
        {
            pushHandler(remoteMessage.getData());
        }

    }

    private void notifyOpponent(Map<String, String> push) {
        Data data = new Data();
        data.setStatus(1);
        data.setToken(push.get("token"));
        data.setUsername(push.get("username"));
        PushData pusher = new PushData();
        pusher.setData(data);
        pusher.setTo(push.get("token"));
        SendPush.sendPushToUser(pusher);

    }

    private void pushHandler(Map<String, String> data) {
              int status = Integer.parseInt(data.get("status"));
                if(status == 0)
                {
                    showNotification(data);
                    notifyOpponent(data);
                }
                else
                {
                    Intent intent = new Intent(this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromcatch",true);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }


        }


    private void showNotification(Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromNotification",true);
        bundle.putString("username",data.get("username"));
        bundle.putString("email",data.get("email"));
        bundle.putString("latitude",data.get("latitude"));
        bundle.putString("longitude",data.get("longitude"));
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri notificationsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        RemoteViews views = new RemoteViews(getPackageName(),R.layout.notification_layout);
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                  .setSmallIcon(R.drawable.p2b_icon)
                  .setCustomContentView(views)
                  .setContentTitle("Catch me if you can!")
                  .setContentText("Are you ready to beat " + data.get("username"))
                  .setAutoCancel(false)
                //  .addAction(R.drawable.start_button, "Start", pendingIntent)
                  .setSound(notificationsound)
                 .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), nBuilder.build());


    }


}
