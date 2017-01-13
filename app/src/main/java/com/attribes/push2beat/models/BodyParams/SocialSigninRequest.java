package com.attribes.push2beat.models.BodyParams;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public class SocialSigninRequest {
    private String social_token;
    private String device_type;
    private String device_token;
    private String profile_image;

    public String getSocial_token() {
        return social_token;
    }

    public void setSocial_token(String social_token) {
        this.social_token = social_token;
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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }


}
