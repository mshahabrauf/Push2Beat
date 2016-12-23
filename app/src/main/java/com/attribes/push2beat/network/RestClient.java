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
    private static FirebaseInterface fbClient;

    static {
        setupClient();
    }

    private static void setupClient() {

        setupRestClient();
        setupFireBaseClient();

    }

    private static void setupFireBaseClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Content-Type","application/json")
                        .header("Authorization", "key= AIzaSyBQeEHVP1CQ6VHdu5nGkS42wsxZ5mFlJts"); // <-- this is the server key line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(NetworkConstant.FIREBASE_URL)
                .client(client).build();

        fbClient = retrofit.create(FirebaseInterface.class);
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
                                .addHeader("accept", "application/json")
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

    public static FirebaseInterface getFbAdapter(){
        return fbClient;
    }
}

