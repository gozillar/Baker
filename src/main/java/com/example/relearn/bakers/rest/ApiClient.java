package com.example.relearn.bakers.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private Retrofit retrofit;

    public ApiClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public ApiInterface getService() {
        return retrofit.create(ApiInterface.class);
    }

}
