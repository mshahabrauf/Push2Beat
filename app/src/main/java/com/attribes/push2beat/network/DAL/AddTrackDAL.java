package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;

import com.attribes.push2beat.models.BodyParams.AddTrackParams;
import com.attribes.push2beat.models.Response.AddTrackResponse;
import com.attribes.push2beat.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 12/9/16.
 */

public class AddTrackDAL {
    public static void postTrack(AddTrackParams data, final Context context)
    {
        RestClient.getAuthAdapter().postTrack(data).enqueue(new Callback<AddTrackResponse>() {
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
