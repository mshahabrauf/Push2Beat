package com.attribes.push2beat.Utils;

import models.SocialSignUpResponse;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public interface OnSocialSignUpSuccess {
    void onSuccess(SocialSignUpResponse socialSignUpResponse);



    void onFailure();
}
