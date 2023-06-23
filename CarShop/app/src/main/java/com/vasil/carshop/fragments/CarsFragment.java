package com.vasil.carshop.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.vasil.carshop.utils.Constants.SHARED_PREF_NAME;
import static com.vasil.carshop.utils.Utils.isLoggedIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vasil.carshop.R;
import com.vasil.carshop.activities.CarCreateActivity;
import com.vasil.carshop.adapters.CarAdapter;
import com.vasil.carshop.api.CarsApi;
import com.vasil.carshop.responses.cars.CarGetResponse;
import com.vasil.carshop.utils.Api;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CarsFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private TextView carsCountTV;
    private List<CarGetResponse> cars;
    private Button carsCheckBtn;
    private Button carsAddCarBtn;
    private ProgressDialog loadingDialog;

    private SharedPreferences pref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cars, container, false);

        recyclerView = root.findViewById(R.id.cars);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pref = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        carsCountTV = root.findViewById(R.id.carsCountTV);
//        carsCheckBtn = root.findViewById(R.id.carsCheckBtn);
        carsAddCarBtn = root.findViewById(R.id.carsAddCarBtn);

        if (isLoggedIn(pref)) {
            carsAddCarBtn.setVisibility(View.VISIBLE);
        } else {
            carsAddCarBtn.setVisibility(View.GONE);
        }

        cars = new ArrayList<>();
        carAdapter = new CarAdapter(getContext(), cars);
        recyclerView.setAdapter(carAdapter);

        loadingDialog = new ProgressDialog(getContext());
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setMessage(getString(R.string.cars_loading));
        loadingDialog.show();

        Retrofit retrofit = Api.getRetrofit();

        CarsApi carsApi = retrofit.create(CarsApi.class);

        carsApi.getCars().enqueue(new Callback<List<CarGetResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<CarGetResponse>> call, @NonNull Response<List<CarGetResponse>> response) {

                List<CarGetResponse> storedCars = response.body();

                cars.addAll(storedCars);
                carAdapter.notifyDataSetChanged();
                carsCountTV.setText(String.valueOf(carAdapter.getItemCount()));
                loadingDialog.hide();
            }

            @Override
            public void onFailure(@NonNull Call<List<CarGetResponse>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), R.string.cars_general_error, Toast.LENGTH_SHORT).show();
                carsCountTV.setText("0");
                loadingDialog.hide();
            }
        });

        carsAddCarBtn.setOnClickListener(v -> {
//            Snackbar.make(v, "Here", BaseTransientBottomBar.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), CarCreateActivity.class);
            startActivity(intent);
        });

        return root;
    }
}
