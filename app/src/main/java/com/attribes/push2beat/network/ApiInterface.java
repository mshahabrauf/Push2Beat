package com.attribes.push2beat.network;

import com.attribes.push2beat.models.BodyParams.AddTrackParams;
import com.attribes.push2beat.models.BodyParams.UserListParams;
import com.attribes.push2beat.models.Response.AddTrackResponse;
import com.attribes.push2beat.models.Response.ListOfUserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Muhammad Shahab on 12/7/16.
 */

public interface ApiInterface {

    @POST(EndPoints.Add_Track)
    Call<AddTrackResponse> postTrack(@Body AddTrackParams data);


    @POST(EndPoints.User_List)
    Call<ListOfUserResponse> getUsers(@Body UserListParams params);



}
