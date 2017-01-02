package com.attribes.push2beat.Utils;

import models.SigninResponseDetail;

/**
 * Created by Talha Ghaffar on 12/20/2016.
 */
public interface onSignInSuccess {
    void onSuccess(SigninResponseDetail data);


    void onFailure();


}
