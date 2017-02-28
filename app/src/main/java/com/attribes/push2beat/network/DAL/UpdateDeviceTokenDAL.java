package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.interfaces.MyCallBacks;
import com.attribes.push2beat.models.Response.UpdateToken;
import com.attribes.push2beat.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: Uzair Qureshi
 * Date:  2/27/17.
 * Description:
 */

public class UpdateDeviceTokenDAL {
    public static void updateToken(String userId, String token, final MyCallBacks<UpdateToken> listner)
    {
        RestClient.getAuthAdapter().updateDeviceToken(userId,token).enqueue(new Callback<UpdateToken>() {
            @Override
            public void onResponse(Call<UpdateToken> call, Response<UpdateToken> response)
            {
                if(response.isSuccessful()&&response.body()!=null)
                {
                  listner.onSuccess(response.body());
                }

            }

            @Override
            public void onFailure(Call<UpdateToken> call, Throwable t)
            {
                listner.onFailure(t.getMessage());

            }
        });

    }
}
