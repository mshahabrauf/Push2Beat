package com.attribes.push2beat.network;

import com.attribes.push2beat.models.BodyParams.AddTrackParams;
import com.attribes.push2beat.models.BodyParams.UserListParams;
import com.attribes.push2beat.models.Response.AddTrackResponse;
import com.attribes.push2beat.models.Response.ListOfUserResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import java.util.Map;

/**
 * Created by Muhammad Shahab on 12/7/16.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST(EndPoints.Add_Track)
    Call<AddTrackResponse> postTrack(@FieldMap Map<String,Object> params);


//    @FormUrlEncoded
//    @POST(EndPoints.User_List)
//    Call<ListOfUserResponse> getUsers(@FieldMap Map<String,Object> params);

}
