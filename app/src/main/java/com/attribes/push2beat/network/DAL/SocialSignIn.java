package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;
import com.attribes.push2beat.localinterface.OnSocialSignInSuccess;
import com.attribes.push2beat.localinterface.OnSocialSignUpSuccess;
import com.attribes.push2beat.network.RestClient;
import models.SocialSigninRequest;
import models.SocialSignInResponse;
import models.UserProfile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public class SocialSignIn {

    public static void socialsigninnew(final UserProfile Logindata, final Context context,final OnSocialSignInSuccess listener){
        {
            HashMap<String, Object> params = new HashMap<>();
            params.put("social_token", Logindata.getSocial_token());
            params.put("device_type", Logindata.getDevice_type());
            params.put("device_token", Logindata.getDevice_token());
            params.put("profile_image", Logindata.getProfile_image());


            RestClient.getAuthAdapter().socialsignin(params).enqueue(new Callback<SocialSignInResponse>() {
                @Override
                public void onResponse(Call<SocialSignInResponse> call, Response<SocialSignInResponse> response) {

                    if (response.isSuccessful())
                    {
                   //     listener.onSuccess();
                        // SignUp.acntsignup(data.getEmail(),data.getPassword());


                        Toast.makeText(context, "Social SigninResponseDetail and Facebook SigninResponseDetail Sucessfully!", Toast.LENGTH_SHORT).show();


                    }
                }



                @Override
                public void onFailure(Call<SocialSignInResponse> call, Throwable t) {

                }
            });



        }
    }}