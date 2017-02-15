package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;

import com.attribes.push2beat.models.BodyParams.AddTrackParams;
import com.attribes.push2beat.models.Response.AddTrackResponse;
import com.attribes.push2beat.network.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 12/9/16.
 */

public class AddTrackDAL {
    public static void postTrack(AddTrackParams data, final Context context)
    {
        HashMap<String,Object> params = new HashMap<>();
        params.put("track_name",data.getTrack_name());
        params.put("genrated_by",data.getGenrated_by());
        params.put("distance",data.getDistance());
        params.put("track_time",data.getTrack_time());
        params.put("caleries_burnt",data.getCaleries_burnt());
        params.put("start_lattitude",data.getStart_latitude());
        params.put("start_longitude",data.getStart_longitude());
        params.put("end_lattitude",data.getEnd_latitude());
        params.put("end_longitude",data.getEnd_longitude());
        params.put("track_path",data.getTrack_path());
        params.put("track_type",data.getTrack_type());
        params.put("top_speed",data.getAverage_speed());
        params.put("average_speed",data.getTop_speed());



        RestClient.getAuthAdapter().postTrack(params).enqueue(new Callback<AddTrackResponse>() {
            @Override
            public void onResponse(Call<AddTrackResponse> call, Response<AddTrackResponse> response) {

                if(response.isSuccessful())
                {
                    Toast.makeText(context, "Save Sucessfully!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddTrackResponse> call, Throwable t) {
                Toast.makeText(context, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
