package com.vasil.carshop.activities;

import static com.vasil.carshop.utils.Api.getRetrofit;
import static com.vasil.carshop.utils.Constants.SHARED_PREF_NAME;
import static com.vasil.carshop.utils.Utils.getLoggedUser;
import static com.vasil.carshop.utils.Utils.getStringResource;
import static com.vasil.carshop.utils.Utils.isLoggedIn;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vasil.carshop.R;
import com.vasil.carshop.api.CarsApi;
import com.vasil.carshop.requests.cars.CarCreateRequest;
import com.vasil.carshop.requests.cars.MileageCreateRequest;
import com.vasil.carshop.responses.cars.CarGetResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarActivity extends AppCompatActivity {

    private TextView carBrandModelTV;
    private TextView carPriceTV;
    private TextView carYearTV;
    private TextView carEngineTypeTV;
    private TextView carHorsePowersTV;
    private TextView carGearboxTV;
    private TextView carCategoryTV;
    private TextView carMileageTV;
    private TextView carColorTV;
    private Button carCheckBtn;
    private ProgressDialog dialog;

    private SharedPreferences pref;

    private CarGetResponse currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        getSupportActionBar().setTitle(getString(R.string.car_title));

        pref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        carBrandModelTV = findViewById(R.id.carBrandModelTV);
        carPriceTV = findViewById(R.id.carPriceTV);
        carYearTV = findViewById(R.id.carYearTV);
        carEngineTypeTV = findViewById(R.id.carEngineTypeTV);
        carHorsePowersTV = findViewById(R.id.carHorsePowersTV);
        carGearboxTV = findViewById(R.id.carGearboxTV);
        carCategoryTV = findViewById(R.id.carCategoryTV);
        carMileageTV = findViewById(R.id.carMileageTV);
        carColorTV = findViewById(R.id.carColorTV);
        carCheckBtn = findViewById(R.id.carCheckBtn);

        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.car_loading));
        dialog.show();

        String carId = getIntent().getStringExtra("carId");

        Retrofit retrofit = getRetrofit();

        CarsApi carsApi = retrofit.create(CarsApi.class);

        carsApi.getCar(carId).enqueue(new Callback<CarGetResponse>() {
            @Override
            public void onResponse(@NonNull Call<CarGetResponse> call, @NonNull Response<CarGetResponse> response) {

                CarGetResponse car = response.body();
                currentCar = car;

                if (car != null) {

                    setCarData(car);
                    if (isLoggedIn(pref)) {
                        if (getLoggedUser(pref).getUserId().equals(car.getOwner().getUserId())) {
                            carCheckBtn.setVisibility(View.VISIBLE);
                        } else {
                            carCheckBtn.setVisibility(View.GONE);
                        }
                    } else {
                        carCheckBtn.setVisibility(View.GONE);
                    }
                }

                dialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<CarGetResponse> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.car_general_error, Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });

        carCheckBtn.setOnClickListener(v -> {

            dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.car_check_changes_checking));
            dialog.show();

            carsApi.getCar(carId).enqueue(new Callback<CarGetResponse>() {
                @Override
                public void onResponse(@NonNull Call<CarGetResponse> call, @NonNull Response<CarGetResponse> response) {

                    CarGetResponse car = response.body();

                    if (car != null) {
                        if (car.isUpdated(currentCar)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);

                            builder.setTitle(getString(R.string.car_check_changes_yes_title));

                            builder.setMessage(getString(R.string.car_check_changes_yes_message));

                            builder.setPositiveButton(R.string.car_check_changes_update_latest, (dialog, which) -> {
                                setCarData(car);
                                currentCar = car;
                                Toast.makeText(getApplicationContext(), R.string.car_check_changes_update_latest_success, Toast.LENGTH_SHORT).show();
                            });

                            builder.setNegativeButton(R.string.car_check_changes_discard, (dialog, which) -> {

                                CarCreateRequest ccr = new CarCreateRequest();
                                ccr.setBrand(currentCar.getBrand());
                                ccr.setModel(currentCar.getModel());
                                ccr.setPrice(currentCar.getPrice());
                                ccr.setYear(currentCar.getYear());
                                ccr.setEngineType(currentCar.getEngineType());
                                ccr.setHorsePowers(currentCar.getHorsePowers());
                                ccr.setGearbox(currentCar.getGearbox());
                                ccr.setCategory(currentCar.getCategory());

                                MileageCreateRequest mcr = new MileageCreateRequest();
                                mcr.setMileage(currentCar.getMileage().getMileage());
                                mcr.setUnit(currentCar.getMileage().getUnit());
                                ccr.setMileage(mcr);

                                ccr.setColor(currentCar.getColor());
                                ccr.setUserId(currentCar.getOwner().getUserId());

                                CarsApi carsApi = retrofit.create(CarsApi.class);

                                carsApi.updateCar(currentCar.getCarId(), ccr).enqueue(new Callback<CarGetResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<CarGetResponse> call, @NonNull Response<CarGetResponse> response) {

                                        Toast.makeText(getApplicationContext(), R.string.car_check_changes_discard_success, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<CarGetResponse> call, @NonNull Throwable t) {

                                    }
                                });
                            });

                            builder.setNeutralButton(R.string.car_check_changes_duplicate, (dialog, which) -> {

                                CarCreateRequest ccr = new CarCreateRequest();
                                ccr.setBrand(currentCar.getBrand());
                                ccr.setModel(currentCar.getModel());
                                ccr.setPrice(currentCar.getPrice());
                                ccr.setYear(currentCar.getYear());
                                ccr.setEngineType(currentCar.getEngineType());
                                ccr.setHorsePowers(currentCar.getHorsePowers());
                                ccr.setGearbox(currentCar.getGearbox());
                                ccr.setCategory(currentCar.getCategory());

                                MileageCreateRequest mcr = new MileageCreateRequest();
                                mcr.setMileage(currentCar.getMileage().getMileage());
                                mcr.setUnit(currentCar.getMileage().getUnit());
                                ccr.setMileage(mcr);

                                ccr.setColor(currentCar.getColor());
                                ccr.setUserId(currentCar.getOwner().getUserId());

                                CarsApi carsApi = retrofit.create(CarsApi.class);

                                carsApi.createCar(ccr).enqueue(new Callback<CarGetResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<CarGetResponse> call, @NonNull Response<CarGetResponse> response) {
//                                        loadingDialog.hide();

                                        Toast.makeText(getApplicationContext(), R.string.car_check_changes_duplicate_success, Toast.LENGTH_SHORT).show();
                                        getParent().findViewById(R.id.nav_view).findViewById(R.id.nav_cars).performClick();
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<CarGetResponse> call, @NonNull Throwable t) {
                                        Toast.makeText(getApplicationContext(), R.string.car_create_error, Toast.LENGTH_SHORT).show();
//                                        loadingDialog.hide();
                                    }
                                });


                            });

                            AlertDialog dialog = builder.create();

                            dialog.show();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.car_check_changes_no, Toast.LENGTH_SHORT).show();
                        }
                    }

                    dialog.hide();
                }

                @Override
                public void onFailure(@NonNull Call<CarGetResponse> call, @NonNull Throwable t) {
                    dialog.hide();
                }
            });
        });
    }

    private void setCarData(CarGetResponse currentCarData) {

        NumberFormat formatter = new DecimalFormat("#0");

        carBrandModelTV.setText(currentCarData.getBrand() + " " + currentCarData.getModel());
        carPriceTV.setText(formatter.format(currentCarData.getPrice()) + " " + getStringResource(getApplicationContext(), "currency_bgn"));
        carYearTV.setText(String.valueOf(currentCarData.getYear()));
        carEngineTypeTV.setText(getStringResource(getApplicationContext(), "car_engine_type_" + currentCarData.getEngineType()));
        carHorsePowersTV.setText(getString(R.string.car_horse_powers_hp, currentCarData.getHorsePowers()));
        carGearboxTV.setText(getStringResource(getApplicationContext(), "car_gearbox_" + currentCarData.getGearbox()));
        carCategoryTV.setText(getStringResource(getApplicationContext(), "car_category_" + currentCarData.getCategory()));
        carMileageTV.setText(formatter.format(currentCarData.getMileage().getMileage()) + " " + getStringResource(getApplicationContext(), "car_mileage_unit_" + currentCarData.getMileage().getUnit()));
        carColorTV.setText(currentCarData.getColor());
    }
}
