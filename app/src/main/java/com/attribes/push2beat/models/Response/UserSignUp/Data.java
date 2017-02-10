package com.attribes.push2beat.models.Response.UserSignUp;
import com.attribes.push2beat.models.BodyParams.SignUpParams;

import java.util.List;

/**
 * Created by Talha Ghaffar on 12/13/2016.
 */


public class Data {

    public List<SignUpParams> userDetails = null;

    public List<SignUpParams> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<SignUpParams> userDetails) {
        this.userDetails = userDetails;
    }
}
