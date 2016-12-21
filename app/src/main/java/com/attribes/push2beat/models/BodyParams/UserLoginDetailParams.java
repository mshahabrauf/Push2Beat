package com.attribes.push2beat.models.BodyParams;

/**
 * Created by Talha Ghaffar on 12/14/2016.
 */
public class UserLoginDetailParams {

    private String user_email;
    private String device_type;
    private String device_token;
    private String password;

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
