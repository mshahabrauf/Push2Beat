package com.attribes.push2beat.network.DAL;

import android.content.Context;

import com.attribes.push2beat.Utils.DevicePreferences;
import com.attribes.push2beat.Utils.OnSocialSignInSuccess;
import com.attribes.push2beat.models.Response.SocialSignIn.SocialSignInResponse;
import com.attribes.push2beat.models.UserProfile;
import com.attribes.push2beat.network.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public class SocialSignIn {

    public static void socialsigninnew(final UserProfile Logindata, final Context context, final OnSocialSignInSuccess listener){
        {
            HashMap<String, String> params = new HashMap<>();
            params.put("social_token", Logindata.getSocial_token());
            params.put("device_type", Logindata.getDevice_type() );
            params.put("device_token", Logindata.getDevice_token());
            params.put("profile_image", Logindata.getProfile_image().toString());


            RestClient.getAuthAdapter().socialsignin(params).enqueue(new Callback<SocialSignInResponse>() {
                @Override
                public void onResponse(Call<SocialSignInResponse> call, Response<SocialSignInResponse> response) {

                    if (response.isSuccessful())
                    {
                      listener.onSuccess();
                        // SignUp.QBSignUp(data.getEmail(),data.getPassword());

                        DevicePreferences.getInstance().saveusers(response.body().getData());
                      //  Toast.makeText(context, "Social Signin Sucessfully!", Toast.LENGTH_SHORT).show();


                    }
                }



                @Override
                public void onFailure(Call<SocialSignInResponse> call, Throwable t) {

                }
            });



        }
    }}