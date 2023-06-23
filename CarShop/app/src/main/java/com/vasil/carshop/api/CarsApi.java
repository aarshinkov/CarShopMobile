package com.vasil.carshop.api;

import com.vasil.carshop.requests.cars.CarCreateRequest;
import com.vasil.carshop.responses.cars.CarGetResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CarsApi {

    @GET("api/cars")
    Call<List<CarGetResponse>> getCars();

    @GET("api/cars/{carId}")
    Call<CarGetResponse> getCar(@Path("carId") String carId);

    @POST("api/cars")
    Call<CarGetResponse> createCar(@Body CarCreateRequest ccr);

    @PUT("api/cars/{carId}")
    Call<CarGetResponse> updateCar(@Path("carId") String carId, @Body CarCreateRequest ccr);
}
