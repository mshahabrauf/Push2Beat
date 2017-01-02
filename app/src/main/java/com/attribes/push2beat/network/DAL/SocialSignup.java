package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;

import com.attribes.push2beat.Utils.OnSocialSignUpSuccess;
import com.attribes.push2beat.models.Response.SocialSignUpResponse;
import com.attribes.push2beat.models.UserProfile;
import com.attribes.push2beat.network.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public class SocialSignup {

    public static void socialsignupnew(final UserProfile Socialdata, final Context context, final OnSocialSignUpSuccess listener){
        {
            HashMap<String, Object> params = new HashMap<>();
            params.put("firstname", Socialdata.getFirstname());
            params.put("email", Socialdata.getEmail());
            params.put("lattitude", Socialdata.getLattitude());
            params.put("longitude", Socialdata.getLongitude());
            params.put("social_token", Socialdata.getSocial_token());
            params.put("profile_image", Socialdata.getProfile_image());

            RestClient.getAuthAdapter().socialsignup(params).enqueue(new Callback<SocialSignUpResponse>() {
                @Override
                public void onResponse(Call<SocialSignUpResponse> call, Response<SocialSignUpResponse> response) {
                    if (response.isSuccessful())
                    {
                        // }
                        // SignUp.acntsignup(data.getEmail(),data.getPassword());
                        listener.onSuccess(response.body());

                        Toast.makeText(context, "Facebook Signup Sucessfully!", Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onFailure(Call<SocialSignUpResponse> call, Throwable t) {
                    listener.onFailure();

                }
            });


        }}
            }
