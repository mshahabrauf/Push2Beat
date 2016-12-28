package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.models.BodyParams.UserLoginDetailParams;
import com.attribes.push2beat.models.Response.UserSignUp.SigninResponse;
import com.attribes.push2beat.network.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Talha Ghaffar on 12/14/2016.
 */
public class LoginDAL {

    public static void userLogin(UserLoginDetailParams userdata, OnSignUpSuccess onSignUpSuccess){
        {
            HashMap<String, Object> params = new HashMap<>();
            params.put("user_email", userdata.getUser_email());
            params.put("device_type", userdata.getDevice_type());
            params.put("device_token", userdata.getDevice_token());
            params.put("password", userdata.getPassword());
            final OnSignUpSuccess listener;
            listener = onSignUpSuccess;


            RestClient.getAuthAdapter().signin(params).enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {

                    if (response.isSuccessful())
                    {

                        Common.getInstance().setUser(response.body().getData());
                        listener.onSuccess();
                        // Toast.makeText(context, "Push2Beat Login Sucessfully!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<SigninResponse> call, Throwable t) {

                    listener.onFailure();
                    //Toast.makeText(context, "Push2beat Login Failed!", Toast.LENGTH_SHORT).show();

                }
            });
        }}}
