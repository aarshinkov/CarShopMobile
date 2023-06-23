package com.vasil.carshop.activities;

import static com.vasil.carshop.utils.Api.getRetrofit;
import static com.vasil.carshop.utils.Constants.SHARED_PREF_NAME;
import static com.vasil.carshop.utils.Utils.getLoggedUser;
import static com.vasil.carshop.utils.Utils.getStringResource;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vasil.carshop.R;
import com.vasil.carshop.api.CarsApi;
import com.vasil.carshop.requests.cars.CarCreateRequest;
import com.vasil.carshop.requests.cars.MileageCreateRequest;
import com.vasil.carshop.responses.cars.CarGetResponse;
import com.vasil.carshop.responses.users.UserGetResponse;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarCreateActivity extends AppCompatActivity {

    private EditText carCreateBrandET;
    private EditText carCreateModelET;
    private EditText carCreatePriceET;
    private EditText carCreateYearET;
    private Spinner carCreateEngineTypeSpinner;
    private EditText carCreateHorsePowersET;
    private Spinner carCreateGearboxSpinner;
    private Spinner carCreateCategorySpinner;
    private EditText carCreateMileageET;
    private EditText carCreateColorET;
    private Button carCreateBtn;

    private ProgressDialog loadingDialog;

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_create);

        getSupportActionBar().setTitle(getString(R.string.car_create_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplicationContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        carCreateBrandET = findViewById(R.id.carCreateBrandET);
        carCreateModelET = findViewById(R.id.carCreateModelET);
        carCreatePriceET = findViewById(R.id.carCreatePriceET);
        carCreateYearET = findViewById(R.id.carCreateYearET);
        carCreateEngineTypeSpinner = findViewById(R.id.carCreateEngineTypeSpinner);
        carCreateHorsePowersET = findViewById(R.id.carCreateHorsePowersET);
        carCreateGearboxSpinner = findViewById(R.id.carCreateGearboxSpinner);
        carCreateCategorySpinner = findViewById(R.id.carCreateCategorySpinner);
        carCreateMileageET = findViewById(R.id.carCreateMileageET);
        carCreateColorET = findViewById(R.id.carCreateColorET);
        carCreateBtn = findViewById(R.id.carCreateBtn);

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setMessage(getString(R.string.car_create_creating_car));

        carCreateBtn.setOnClickListener(v -> {

            loadingDialog.show();

//            Toast.makeText(getApplicationContext(), carCreateEngineTypeSpinner.getSelectedItemPosition() + "", Toast.LENGTH_SHORT).show();

            if (isFieldsValid()) {
                loadingDialog.hide();
                return;
            }

            String brand = carCreateBrandET.getText().toString();
            String model = carCreateModelET.getText().toString();
            Double price = Double.parseDouble(carCreatePriceET.getText().toString());
            Integer year = Integer.parseInt(carCreateYearET.getText().toString());
            Integer horsePowers = Integer.parseInt(carCreateHorsePowersET.getText().toString());
            Double mileage = Double.parseDouble(carCreateMileageET.getText().toString());
            String color = carCreateColorET.getText().toString();

            CarCreateRequest ccr = new CarCreateRequest();
            ccr.setBrand(brand);
            ccr.setModel(model);
            ccr.setPrice(price);
            ccr.setYear(year);
            ccr.setEngineType(carCreateEngineTypeSpinner.getSelectedItemPosition() + 1);
            ccr.setHorsePowers(horsePowers);
            ccr.setGearbox(carCreateGearboxSpinner.getSelectedItemPosition() + 1);
            ccr.setCategory(carCreateCategorySpinner.getSelectedItemPosition() + 1);

            MileageCreateRequest mcr = new MileageCreateRequest();
            mcr.setMileage(mileage);
            mcr.setUnit("km");
            ccr.setMileage(mcr);
            ccr.setColor(color);

            UserGetResponse loggedUser = getLoggedUser(pref);
            ccr.setUserId(loggedUser.getUserId());

            Retrofit retrofit = getRetrofit();

            CarsApi carsApi = retrofit.create(CarsApi.class);

            carsApi.createCar(ccr).enqueue(new Callback<CarGetResponse>() {
                @Override
                public void onResponse(@NonNull Call<CarGetResponse> call, @NonNull Response<CarGetResponse> response) {
                    Toast.makeText(getApplicationContext(), R.string.car_create_success, Toast.LENGTH_SHORT).show();
                    loadingDialog.hide();

                    getParent().findViewById(R.id.nav_view).findViewById(R.id.nav_cars).performClick();
                }

                @Override
                public void onFailure(@NonNull Call<CarGetResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.car_create_error, Toast.LENGTH_SHORT).show();
                    loadingDialog.hide();
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return true;
    }

    private boolean isFieldsValid() {

        boolean hasErrors = false;

        String brand = carCreateBrandET.getText().toString();

        if (brand.isEmpty()) {
            carCreateBrandET.setError(getString(R.string.car_create_error_brand_empty));
            hasErrors = true;
        }

        String model = carCreateModelET.getText().toString();

        if (model.isEmpty()) {
            carCreateModelET.setError(getString(R.string.car_create_error_model_empty));
            hasErrors = true;
        }

        String priceText = carCreatePriceET.getText().toString();

        if (priceText.isEmpty()) {
            carCreatePriceET.setError(getString(R.string.car_create_error_price_empty));
            hasErrors = true;
        }

        String yearText = carCreateYearET.getText().toString();

        if (yearText.isEmpty()) {
            carCreateYearET.setError(getString(R.string.car_create_error_year_empty));
            hasErrors = true;
        } else {
            int year = Integer.parseInt(yearText);

            Calendar now = Calendar.getInstance();
            int currentYear = now.get(Calendar.YEAR);

            if (year > currentYear) {
                carCreateYearET.setError(getString(R.string.car_create_error_year_future, currentYear));
            }
        }

        String horsePowersText = carCreateHorsePowersET.getText().toString();

        if (horsePowersText.isEmpty()) {
            carCreateHorsePowersET.setError(getString(R.string.car_create_error_horse_powers_empty));
            hasErrors = true;
        }

        String mileageText = carCreateMileageET.getText().toString();

        if (mileageText.isEmpty()) {
            carCreateMileageET.setError(getString(R.string.car_create_error_mileage_empty));
            hasErrors = true;
        }

        String color = carCreateColorET.getText().toString();

        if (color.isEmpty()) {
            carCreateColorET.setError(getString(R.string.car_create_error_color_empty));
            hasErrors = true;
        }

        return hasErrors;
    }
}
