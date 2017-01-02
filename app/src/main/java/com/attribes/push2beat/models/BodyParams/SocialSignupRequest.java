package com.attribes.push2beat.models.BodyParams;

/**
 * Created by Talha Ghaffar on 12/28/2016.
 */
public class SocialSignupRequest {

private String firstname;
private String email;
private String lattitude;
private String longitude;
private String social_token;
private String profile_image;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public String getSocial_token() {
        return social_token;
    }

    public void setSocial_token(String social_token) {
        this.social_token = social_token;
    }

    public Object getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
