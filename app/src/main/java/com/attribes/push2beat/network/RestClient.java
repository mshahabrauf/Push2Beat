package com.attribes.push2beat.network;

/**
 * Created by Muhammad Shahab on 12/7/16.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Muhammad Shahab on 7/20/16.
 */
public class RestClient {
    private static final int TIMEOUT = 25;
    private static ApiInterface restClient;


    static {
        setupClient();
    }

    private static void setupClient() {

        setupRestClient();


    }



    private static void setupRestClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest;
                        newRequest = request.newBuilder()
                                .addHeader("Accept", "application/json")
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).connectTimeout(TIMEOUT, TimeUnit.SECONDS).
                        writeTimeout(TIMEOUT, TimeUnit.SECONDS).
                        readTimeout(TIMEOUT,TimeUnit.SECONDS)
                .addInterceptor(logging)

                .build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(NetworkConstant.base_url).
                addConverterFactory(GsonConverterFactory.create(gson)).
                client(httpClient).
                build();

        restClient = retrofit.create(ApiInterface.class);
    }


    public static ApiInterface getAuthAdapter(){
        return restClient;
    }


}

