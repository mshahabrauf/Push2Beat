package com.attribes.push2beat.network;

import com.attribes.push2beat.models.Response.AddTrackResponse;
import com.attribes.push2beat.models.Response.TrackList.ListOfTrackResponse;
import com.attribes.push2beat.models.Response.UserList.ListOfUserResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Muhammad Shahab on 12/7/16.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(EndPoints.Add_Track)
    Call<AddTrackResponse> postTrack(@FieldMap Map<String,Object> params);


    @FormUrlEncoded
    @POST(EndPoints.User_List)
    Call<ListOfUserResponse> getUsers(@FieldMap Map<String,Object> params);


    @FormUrlEncoded
    @POST(EndPoints.Track_List)
    Call<ListOfTrackResponse> getTracks(@FieldMap Map<String,Object> params);


}
