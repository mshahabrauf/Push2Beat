package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.models.BodyParams.UpdateProfileParams;
import com.attribes.push2beat.models.Response.UpdateProfileResponse;
import com.attribes.push2beat.network.RestClient;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maaz on 12/24/2016.
 */
public class UpdateProfileDAL {

    public static void updateProfile(UpdateProfileParams profileData, MultipartBody.Part file, final OnSignUpSuccess listener){

        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id",profileData.getUser_id());
        params.put("first_name",profileData.getFirst_name());
        params.put("last_name",profileData.getLast_name());
    //    params.put("profile_image",profileData.getProfile_image());

       // RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),profileData.getUser_id());
        RequestBody firstname = RequestBody.create(MediaType.parse("text/plain"),profileData.getFirst_name());
        RequestBody lastname = RequestBody.create(MediaType.parse("text/plain"),profileData.getLast_name());


        RestClient.getAuthAdapter().updateProfile(user_id,firstname,lastname,file).enqueue(new Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                if(response.isSuccessful())
                {
                    listener.onSuccess();
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
               listener.onFailure();

            }
        });
    }
}
