package com.attribes.push2beat.models.BodyParams;

/**
 *
 *these params used for fetching track list and user list
 * Created by android on 12/11/16.
 */

public class GetListRequestParams {
   private int user_id;
    private double lat;
    private double lng;

    public int getUser_id() {
        return user_id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setUser_id(int user_id) {this.user_id = user_id;}

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
