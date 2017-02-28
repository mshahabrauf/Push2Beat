package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.Utils.Common;
import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.extras.Constants;
import com.attribes.push2beat.interfaces.MyCallBacks;
import com.attribes.push2beat.models.BodyParams.UserLoginDetailParams;
import com.attribes.push2beat.models.Response.UserSignUp.SigninResponse;
import com.attribes.push2beat.network.RestClient;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Talha Ghaffar on 12/14/2016.
 * modified by Uzair Qureshi
 */
public class LoginDAL {

    public static void userLogin(UserLoginDetailParams userdata, final MyCallBacks<SigninResponse> onSignInListner){
        {
            HashMap<String, Object> params = new HashMap<>();
            params.put("user_email", userdata.getUser_email());
            params.put("device_type", userdata.getDevice_type());
            params.put("device_token", userdata.getDevice_token());
            params.put("password", userdata.getPassword());



            RestClient.getAuthAdapter().signin(params).enqueue(new Callback<SigninResponse>() {
                @Override
                public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response)
                {

                    if (response.isSuccessful()&&response.body()!=null)//executes when server gives response
                    {
                        if(response.body().getCode()==Constants.status_OK) //executes when successfully signin
                        {
                            Common.getInstance().setUser(response.body().getData());
                            onSignInListner.onSuccess(response.body());
                        }
//


                    }


                }

                @Override
                public void onFailure(Call<SigninResponse> all, Throwable t)//executes when something goes wrong!
                {
                    if(t instanceof SocketTimeoutException)
                    {
                        onSignInListner.onFailure("Time Out!Your internet is slow");
                    }
                    else if(t instanceof JsonSyntaxException)//executes when invalid login
                    {
                        onSignInListner.onFailure("Wrong Email ID or Password");
                    }
                   else if(t instanceof java.net.UnknownHostException)
                    {
                        onSignInListner.onFailure("No Internet Connection");
                    }
//


                }
            });
        }}}
