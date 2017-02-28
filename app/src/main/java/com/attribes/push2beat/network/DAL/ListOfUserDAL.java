package com.attribes.push2beat.network.DAL;

import android.widget.Toast;
import com.attribes.push2beat.models.BodyParams.GetListRequestParams;
import com.attribes.push2beat.models.Response.UserList.Datum;
import com.attribes.push2beat.models.Response.UserList.ListOfUserResponse;
import com.attribes.push2beat.network.RestClient;
import com.attribes.push2beat.network.interfaces.UsersArrivalListener;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by android on 12/11/16.
 */

public class ListOfUserDAL {
    public static void getUserList(GetListRequestParams params, final UsersArrivalListener listener)
    {

        HashMap<String,Object> body = new HashMap<>();
        body.put("user_id",params.getUser_id());
        body.put("lat",params.getLat());
        body.put("lng",params.getLng());

        RestClient.getAuthAdapter().getUsers(body).enqueue(new Callback<ListOfUserResponse>() {
            @Override
            public void onResponse(Call<ListOfUserResponse> call, Response<ListOfUserResponse> response) {
                if(response.isSuccessful())
                {
                    List<Datum> data = response.body().getData();
                    if (data.isEmpty())
                    {
                        listener.onEmptyData(response.body().getMsg());
                    }
                    else
                    {
                        listener.onDataRecieved(data);
                    }
                }

            }

            @Override
            public void onFailure(Call<ListOfUserResponse> call, Throwable t) {
                if(t instanceof SocketTimeoutException)
                {
                   listener.onFailure(t.getMessage());

                }
                else
                {
                    listener.onFailure(t.getMessage());

                }

            }
        });


    }


}
