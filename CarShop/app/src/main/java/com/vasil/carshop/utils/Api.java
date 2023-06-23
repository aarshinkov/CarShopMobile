package com.vasil.carshop.utils;

import static com.vasil.carshop.utils.Constants.API_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static Retrofit getRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
