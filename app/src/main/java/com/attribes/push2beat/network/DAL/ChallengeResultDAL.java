package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;
import com.attribes.push2beat.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 1/10/17.
 */

public class ChallengeResultDAL {


    public static void sendResultChallenge(String winnerId,String loserId)
    {
        RestClient.getAuthAdapter().challengeLoser(winnerId,loserId).enqueue(new Callback<PushResponse>() {
            @Override
            public void onResponse(Call<PushResponse> call, Response<PushResponse> response) {

            }

            @Override
            public void onFailure(Call<PushResponse> call, Throwable t) {

            }
        });


    }
}
