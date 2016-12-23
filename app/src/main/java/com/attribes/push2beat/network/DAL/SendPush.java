package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.models.Response.PushFireBase.PushData;
import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;
import com.attribes.push2beat.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 12/22/16.
 */

public class SendPush {

    public static void sendPushToUser(PushData data)
    {
        RestClient.getFbAdapter().sendPushToUser(data).enqueue(new Callback<PushResponse>() {
            @Override
            public void onResponse(Call<PushResponse> call, Response<PushResponse> response) {

            }

            @Override
            public void onFailure(Call<PushResponse> call, Throwable t) {

            }
        });
    }
}
