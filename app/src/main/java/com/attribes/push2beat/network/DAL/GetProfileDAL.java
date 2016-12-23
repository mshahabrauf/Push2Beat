package com.attribes.push2beat.network.DAL;

import com.attribes.push2beat.models.Response.MyProfileResponse;
import com.attribes.push2beat.network.RestClient;
import com.attribes.push2beat.network.interfaces.ProfileDataArrivalListner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maaz on 12/21/2016.
 */
public class GetProfileDAL {

    public static void getProfileData(String user_id, final ProfileDataArrivalListner profileDataArrivalListner){

        RestClient.getAuthAdapter().getProfile(user_id).enqueue(new Callback<MyProfileResponse>() {
            @Override
            public void onResponse(Call<MyProfileResponse> call, Response<MyProfileResponse> response) {
                if(response.isSuccessful()){
                    MyProfileResponse.Data my_profileData = response.body().getData();
                    if(my_profileData == null){
                        profileDataArrivalListner.onEmptyData(response.body().getMsg());
                    }else{
                        profileDataArrivalListner.onDataRecieved(my_profileData);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyProfileResponse> call, Throwable t) {

            }
        });
    }
}
