package com.vasil.carshop.api;

import com.vasil.carshop.requests.LoginRequest;
import com.vasil.carshop.responses.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest login);
}
