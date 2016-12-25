package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;
import com.attribes.push2beat.models.BodyParams.UpdateProfileParams;
import com.attribes.push2beat.models.Response.UpdateProfileResponse;
import com.attribes.push2beat.network.RestClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;

/**
 * Created by Maaz on 12/24/2016.
 */
public class UpdateProfileDAL {

    public static void updateProfile(UpdateProfileParams profileData,final Context context){

        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id",profileData.getUser_id());
        params.put("first_name",profileData.getFirst_name());
        params.put("last_name",profileData.getLast_name());
        params.put("profile_image",profileData.getProfile_image());

        RestClient.getAuthAdapter().updateProfile(params).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(context, "Updated Sucessfully !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Toast.makeText(context, "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
