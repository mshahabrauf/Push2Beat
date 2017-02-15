package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;

import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;

import com.attribes.push2beat.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 1/10/17.
 */

public class ChallengeDAL {

    public static void challengeOpponent(String opponentId, String userId,final Context mContext){

        RestClient.getAuthAdapter().challengeOpponent(userId,opponentId).enqueue(new Callback<PushResponse>() {
            @Override
            public void onResponse(Call<PushResponse> call, Response<PushResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(mContext, "Request sent successfully", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushResponse> call, Throwable t) {
                Toast.makeText(mContext, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

