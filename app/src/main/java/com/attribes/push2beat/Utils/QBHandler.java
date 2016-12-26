package com.attribes.push2beat.Utils;

import android.os.Bundle;
import android.util.Log;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.session.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

/**
 * Created by android on 12/26/16.
 */

public class QBHandler {
    private static QBHandler Instance = null;
    QBUser[] opponent = new QBUser[1];
    QBChatService qbChatService;

    private QBHandler() {
    }

    public static QBHandler getInstance()
    {
        if(Instance == null)
        {
            Instance = new QBHandler();
        }
        return Instance;
    }



    public void initQBChatter()
    {

        QBChatService.setDebugEnabled(true); // enable chat logging
        QBChatService.setDefaultAutoSendPresenceInterval(10); //enable sending online status every 60 sec to keep connection alive

        QBChatService.ConfigurationBuilder chatServiceConfigurationBuilder = new QBChatService.ConfigurationBuilder();
        chatServiceConfigurationBuilder.setSocketTimeout(60); //Sets chat socket's read timeout in seconds
        chatServiceConfigurationBuilder.setKeepAlive(true); //Sets connection socket's keepAlive option.
        chatServiceConfigurationBuilder.setUseTls(true); //Sets the TLS security mode used when making the connection. By default TLS is disabled.
        QBChatService.setConfigurationBuilder(chatServiceConfigurationBuilder);
    }



    public void QBChatLogin(QBUser user)
    {
        QBChatService.getInstance().login(user, new QBEntityCallback() {
            @Override
            public void onSuccess(Object o, Bundle bundle) {
                QBChatService service = QBChatService.getInstance();
                service.addConnectionListener(new CustomConnectionListener());
                startQBChatListener(service);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }



    public void QBCreateSession()
    {
        final QBUser user = new QBUser(Common.getInstance().getQbUser().getEmail(),Common.getInstance().getPassword());

        QBAuth.createSession(user).performAsync(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {
                user.setId(qbSession.getId());
                QBChatLogin(user);
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });

    }

    public void setQBOpponentUser(String email)
    {
        QBUsers.getUserByEmail(email).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
              opponent[0] = qbUser;

            }

            @Override
            public void onError(QBResponseException e) {
            }
        });

    }

    public QBUser getQBOpponent()
    {
        return opponent[0];
    }


    public QBChatDialog getQBDialog()
    {
        final QBChatDialog[] dialog = new QBChatDialog[1];
        QBRestChatService.createChatDialog(DialogUtils.buildPrivateDialog(21939584)).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                   dialog[0] = qbChatDialog;

            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("mChat",""+e);
            }
        });

        return dialog[0];
    }




    public QBChatService getQBChatService()
    {

        return qbChatService;
    }

    public void startQBChatListener(QBChatService service)
    {
        QBIncomingMessagesManager manager = service.getIncomingMessagesManager();
        manager.addDialogMessageListener(new QBChatDialogMessageListener() {
            @Override
            public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
                String message = qbChatMessage.getBody();
                String[] str = message.split(",");
                double lat = Double.parseDouble(str[0]);
                double lng = Double.parseDouble(str[1]);
            //    getMapFragment().moveOpponent(lat,lng);
                Log.d("mChat","Message REcieved :"+lat+","+lat);
            }

            @Override
            public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

            }
        });

    }


}
