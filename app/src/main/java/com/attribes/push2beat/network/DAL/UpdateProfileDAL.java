package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.models.BodyParams.UpdateProfileParams;
import com.attribes.push2beat.models.Response.UpdateProfileResponse;
import com.attribes.push2beat.network.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maaz on 12/24/2016.
 */
public class UpdateProfileDAL {

    public static void updateProfile(UpdateProfileParams profileData,final OnSignUpSuccess listener){

        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id",profileData.getUser_id());
        params.put("first_name",profileData.getFirst_name());
        params.put("last_name",profileData.getLast_name());
        params.put("profile_image",profileData.getProfile_image());

        RestClient.getAuthAdapter().updateProfile(params).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if(response.isSuccessful())
                {
                    listener.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
               listener.onFailure();

            }
        });
    }
}
