package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.models.Response.MyStatsList.Datum;
import com.attribes.push2beat.models.Response.MyStatsList.MyStatsResponse;
import com.attribes.push2beat.network.RestClient;
import com.attribes.push2beat.network.interfaces.MyStatsDataArrivalListner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

/**
 * Created by Maaz on 12/23/2016.
 */
public class MyStatsDAL {

    public static void getMyStatsList(String user_id, final MyStatsDataArrivalListner myStatsDataArrivalListner){

        RestClient.getAuthAdapter().getMyStats(user_id).enqueue(new Callback<MyStatsResponse>() {
            @Override
            public void onResponse(Call<MyStatsResponse> call, Response<MyStatsResponse> response) {
                if(response.isSuccessful()){
                    List<Datum> data = response.body().getData();
                    if (data.isEmpty()) {
                        myStatsDataArrivalListner.onEmptyData(response.body().getMsg());
                    } else{
                        myStatsDataArrivalListner.onDataRecieved(data);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyStatsResponse> call, Throwable t) {

            }
        });

    }
}
