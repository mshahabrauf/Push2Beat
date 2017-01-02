package com.attribes.push2beat.models;

/**
 * Created by Talha Ghaffar on 12/13/2016.
 */
public class UserProfile {

    private String firstname;
    private String lastname;
    private Object profile_image;
    private String email;
    private String lattitude;
    private String longitude;
    private String password;
    private String social_token;
    private String device_type;
    private String device_token;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getSocial_token() {
        return social_token;
    }

    public void setSocial_token(String social_token) {
        this.social_token = social_token;
    }
}