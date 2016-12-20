package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.models.BodyParams.GetListRequestParams;
import com.attribes.push2beat.models.Response.TrackList.ListOfTrackResponse;
import com.attribes.push2beat.network.RestClient;
import com.attribes.push2beat.network.interfaces.TracksArrivalListener;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 12/17/16.
 */

public class ListOfTrackDAL {
    public static void getTrackList(GetListRequestParams params, final TracksArrivalListener listener)
    {
        HashMap<String,Object> body = new HashMap<>();
        body.put("user_id",params.getUser_id());
        body.put("lat",params.getLat());
        body.put("lng",params.getLng());


        RestClient.getAuthAdapter().getTracks(body).enqueue(new Callback<ListOfTrackResponse>() {
            @Override
            public void onResponse(Call<ListOfTrackResponse> call, Response<ListOfTrackResponse> response) {
                if (response.isSuccessful())
                {
                    List<com.attribes.push2beat.models.Response.TrackList.Datum> data = response.body().getData();
                    if (data.isEmpty()) {
                        listener.onEmptyData(response.body().getMsg());
                    } else{
                        listener.onDataRecieved(data);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListOfTrackResponse> call, Throwable t) {

            }
        });



    }

}
