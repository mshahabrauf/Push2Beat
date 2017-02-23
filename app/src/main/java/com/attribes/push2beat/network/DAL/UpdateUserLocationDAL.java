package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.attribes.push2beat.models.Response.PushFireBase.PushResponse;
import com.attribes.push2beat.models.Response.UserList.UpdateLocationResponse;
import com.attribes.push2beat.network.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: Uzair Qureshi
 * Date:  2/22/17.
 * Description:
 */

public class UpdateUserLocationDAL
{
    public static void updateUserlocation(String user_id, Location location, final Context context)
    {
        RestClient.getAuthAdapter().updateUserLocation(user_id,String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude())).enqueue(new Callback<UpdateLocationResponse>() {
            @Override
            public void onResponse(Call<UpdateLocationResponse> call, Response<UpdateLocationResponse> response)
            {
              // Toast.makeText(context.getApplicationContext(),response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UpdateLocationResponse> call, Throwable t)
            {
                //Toast.makeText(context.getApplicationContext(),t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });



    }

}
