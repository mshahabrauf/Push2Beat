package com.attribes.push2beat.Utils;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;

/**
 * Created by android on 12/26/16.
 */

public class QBHandler {
    private static QBHandler Instance = null;
    QBUser[] opponent = new QBUser[1];
    QBChatService qbChatService;
    QBChatDialog dialog;

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


    }



    public void QBCreateSession()
    {


    }

    public void setQBOpponentUser(String email)
    {

    }

    public QBUser getQBOpponent()
    {
        return opponent[0];
    }


    public void createQBDialog()
    {



    }


    public QBChatDialog getDialog() {
        return dialog;
    }

    public void setDialog(QBChatDialog dialog) {
        this.dialog = dialog;
    }



    public void startQBChatListener(QBChatService service)
    {
        QBChatService chatService = QBChatService.getInstance();
       // chatService.addConnectionListener(new CustomConnectionListener());

    }


}
