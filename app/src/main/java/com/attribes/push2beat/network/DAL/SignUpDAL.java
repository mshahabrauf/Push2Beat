package com.attribes.push2beat.network.DAL;

import android.content.Context;
import android.widget.Toast;

import com.attribes.push2beat.Utils.OnSignUpSuccess;
import com.attribes.push2beat.models.BodyParams.SignUpParams;
import com.attribes.push2beat.models.Response.UserSignUp.SignupResponse;
import com.attribes.push2beat.network.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Talha Ghaffar on 12/13/2016.
 */
public class SignUpDAL {


public boolean response1=false;


    public static void userRegister(final SignUpParams data, final Context context,final OnSignUpSuccess listner)
    {
        final HashMap<String,Object> params = new HashMap<>();
        params.put("firstname",data.getFirstName());
        params.put("lastname",data.getLastName());
        params.put("email",data.getEmail());
        params.put("profileimage",data.getProfileImage());
        params.put("lattitude",data.getLattitude());
        params.put("longitude",data.getLongitude());
        params.put("password",data.getPassword());


RestClient.getAuthAdapter().signup(params).enqueue(new retrofit2.Callback<SignupResponse>() {
    @Override
    public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
        if (response.isSuccessful())
        {
           // SignUp.acntsignup(data.getEmail(),data.getPassword());
            listner.onSuccess();
            Toast.makeText(context, "Push2Beat Signup Sucessfully!", Toast.LENGTH_SHORT).show();


        }
    }

    @Override
    public void onFailure(Call<SignupResponse> call, Throwable t) {
        Toast.makeText(context, "Push2Beat Signup Failed!", Toast.LENGTH_SHORT).show();
        listner.onFailure();

    }
});

    }

}
