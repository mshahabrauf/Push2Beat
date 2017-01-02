package com.attribes.push2beat.Utils;

import models.SocialSignInResponse;

/**
 * Created by Talha Ghaffar on 12/30/2016.
 */
public interface OnSocialSignInSuccess {
    void onSuccess(SocialSignInResponse socialSignInResponse);
    void onFailure();
}
