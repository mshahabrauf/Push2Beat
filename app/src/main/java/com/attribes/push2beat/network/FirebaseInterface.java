package com.attribes.push2beat.network;

import com.attribes.push2beat.models.Response.PushFireBase.PushData;
import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by android on 12/22/16.
 */
public interface FirebaseInterface {

    @POST(NetworkConstant.SEND_PUSH)
    Call<PushResponse> sendPushToUser(@Body PushData pushData);

}
