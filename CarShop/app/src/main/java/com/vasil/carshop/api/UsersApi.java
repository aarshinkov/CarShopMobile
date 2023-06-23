package com.vasil.carshop.api;

import com.vasil.carshop.requests.users.UserCreateRequest;
import com.vasil.carshop.responses.users.UserCreateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsersApi {

    @POST("api/users")
    Call<UserCreateResponse> createUser(@Body UserCreateRequest ucr);
}
