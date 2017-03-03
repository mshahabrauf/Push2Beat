package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;

import com.attribes.push2beat.Utils.Alerts;
import com.attribes.push2beat.interfaces.MyCallBacks;
import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;

import com.attribes.push2beat.network.RestClient;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 1/10/17.
 * modified by Uzair
 */

public class ChallengeDAL {

    public static void challengeOpponent(String opponentId, String userId, final MyCallBacks<PushResponse> listner){

        RestClient.getAuthAdapter().challengeOpponent(userId,opponentId).enqueue(new Callback<PushResponse>() {
            @Override
            public void onResponse(Call<PushResponse> call, Response<PushResponse> response) {
                if (response.isSuccessful())
                {
                    if(response.body().getSuccess()==1)
                    {
                       // Toast.makeText(mContext, "Request sent successfully", Toast.LENGTH_SHORT).show();
                        listner.onSuccess(response.body());
                    }
                    if(response.body().getSuccess()==0)
                    {
                        //Toast.makeText(mContext, "Sorry! Request was not sent", Toast.LENGTH_SHORT).show();

                        listner.onSuccess(response.body());



                    }
                }
            }

            @Override
            public void onFailure(Call<PushResponse> call, Throwable t)
            {

                if(t instanceof SocketTimeoutException)
                {
                    listner.onFailure("Time Out!Your internet is slow");
                }
                else if(t instanceof JsonSyntaxException)//executes when invalid login
                {
                    listner.onFailure("Request is not sent due to some reason");
                }
                else if(t instanceof java.net.UnknownHostException)
                {
                    listner.onFailure("No Internet Connection");
                }

            }
        });
    }
}

