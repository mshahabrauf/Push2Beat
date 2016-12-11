package com.attribes.push2beat.models.BodyParams;

/**
 * Created by android on 12/11/16.
 */

public class UserListParams {
    int user_id;
    double lat;
    double lng;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
