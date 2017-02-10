package com.attribes.push2beat.models.Response.SocialSignUp;

import java.util.List;

/**
 * Created by android on 1/9/17.
 */

public class Data {
    private List<User_detail> user_details = null;
    private Failure_message failure_message;

    public Failure_message getFailure_message() {
        return failure_message;
    }

    public void setFailure_message(Failure_message failure_message) {
        this.failure_message = failure_message;
    }


    public List<User_detail> getUser_details() {
        return user_details;
    }

    public void setUser_details(List<User_detail> user_details) {
        this.user_details = user_details;
    }

}
