package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.models.BodyParams.UserListParams;
import com.attribes.push2beat.models.Response.Datum;
import com.attribes.push2beat.models.Response.ListOfUserResponse;
import com.attribes.push2beat.network.RestClient;
import com.attribes.push2beat.network.interfaces.UserListListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * Created by android on 12/11/16.
 */

public class ListOfUserDAL {
//    public static void getUserList(UserListParams params, final UserListListener listener)
//    {
//        RestClient.getAuthAdapter().getUsers(params).enqueue(new Callback<ListOfUserResponse>() {
//            @Override
//            public void onResponse(Call<ListOfUserResponse> call, Response<ListOfUserResponse> response) {
//                if(response.isSuccessful())
//                {
//                   List<Datum> data = response.body().getData();
//                    listener.onDataRecieved(data);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ListOfUserResponse> call, Throwable t) {
//
//            }
//        });
//
//
//    }


}
