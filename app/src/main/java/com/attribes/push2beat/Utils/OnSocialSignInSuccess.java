package com.attribes.push2beat.Utils;


import com.attribes.push2beat.models.Response.UserSignUp.SocialSignInResponse;

/**
 * Created by Talha Ghaffar on 12/30/2016.
 */
public interface OnSocialSignInSuccess {
    void onSuccess(SocialSignInResponse socialSignInResponse);
    void onFailure();
}
