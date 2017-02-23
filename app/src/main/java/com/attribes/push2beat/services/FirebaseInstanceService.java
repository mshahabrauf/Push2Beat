package com.attribes.push2beat.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by android on 12/22/16.
 */

public class FirebaseInstanceService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

       String token =  FirebaseInstanceId.getInstance().getToken();

        //Todo: retrieve new device token and call api ..There is no API right now


    }
}
