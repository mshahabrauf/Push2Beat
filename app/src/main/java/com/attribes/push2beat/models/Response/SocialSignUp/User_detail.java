package com.attribes.push2beat.models.Response.SocialSignUp;

/**
 * Created by android on 1/9/17.
 */

public class User_detail {
    private String first_name;
    private String last_name;
    private Object profile_image;
    private String email;
    private String lattitude;
    private String longitude;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Object getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(Object profile_image) {
        this.profile_image = profile_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
