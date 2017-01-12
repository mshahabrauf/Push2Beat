package com.attribes.push2beat.Utils;

import android.content.Context;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

/**
 * Created by android on 12/22/16.
 */
public class CustomConnectionListener implements ConnectionListener {
    Context context;

    public CustomConnectionListener(Context context)
    {
        this.context = context;
    }

    @Override
    public void connected(XMPPConnection xmppConnection) {

    }

    @Override
    public void authenticated(XMPPConnection xmppConnection, boolean b) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Toast.makeText(context, "Connection with opponent is closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int i) {
        Toast.makeText(context, "Reconnecting to the opponent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reconnectionFailed(Exception e) {

    }
}
