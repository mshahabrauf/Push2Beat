package com.attribes.push2beat.Utils;


import com.attribes.push2beat.models.Response.UserSignUp.SigninResponse;

/**
 * Created by Talha Ghaffar on 12/20/2016.
 */
public interface onSignInSuccess {
    void onSuccess(SigninResponse data);


    void onFailure();


}
