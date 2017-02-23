package com.attribes.push2beat.network.interfaces;

import com.attribes.push2beat.models.Response.MyProfileResponse;

/**
 * Created by Maaz on 12/21/2016.
 */
public interface ProfileDataArrivalListner {

    void onDataRecieved(MyProfileResponse.Data data);
    void onEmptyData(String msg);
    void onFailure();
}
