package com.attribes.push2beat.Utils;


import com.attribes.push2beat.models.Response.SocialSignUpResponse;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public interface OnSocialSignUpSuccess {
    void onSuccess(SocialSignUpResponse socialSignUpResponse);



    void onFailure();
}
