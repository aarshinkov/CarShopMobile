package com.vasil.carshop.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.vasil.carshop.utils.Api.getRetrofit;
import static com.vasil.carshop.utils.Constants.SHARED_PREF_NAME;
import static com.vasil.carshop.utils.Constants.SHARED_PREF_USER;
import static com.vasil.carshop.utils.Utils.getStringResource;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.vasil.carshop.R;
import com.vasil.carshop.activities.RootActivity;
import com.vasil.carshop.api.AuthApi;
import com.vasil.carshop.requests.LoginRequest;
import com.vasil.carshop.responses.LoginResponse;
import com.vasil.carshop.responses.users.UserGetResponse;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginFragment extends Fragment {

    private EditText loginEmailET;
    private EditText loginPasswordET;
    private Button loginBtn;
    private TextView loginRegisterTV;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ProgressDialog loginDialog;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login, container, false);

        pref = getContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        editor = pref.edit();

        loginEmailET = root.findViewById(R.id.loginEmailET);
        loginPasswordET = root.findViewById(R.id.loginPasswordET);
        loginBtn = root.findViewById(R.id.loginBtn);
        loginRegisterTV = root.findViewById(R.id.loginRegisterTV);

        loginRegisterTV.setOnClickListener(v -> {
            getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_registration).performClick();
        });

        loginDialog = new ProgressDialog(getContext());
        loginDialog.setMessage(getString(R.string.login_loggingin));

        loginBtn.setOnClickListener(v -> {

            loginDialog.show();

            String email = loginEmailET.getText().toString();
            String password = loginPasswordET.getText().toString();

            boolean hasErrors = false;

            if (email.isEmpty()) {
                loginEmailET.setError(getString(R.string.login_error_email_empty));
                hasErrors = true;
            } else {
                if (!isEmail(email)) {
                    loginEmailET.setError(getString(R.string.login_error_email_pattern));
                    hasErrors = true;
                }
            }

            if (password.isEmpty()) {
                loginPasswordET.setError(getString(R.string.login_error_password_empty));
                hasErrors = true;
            }

            if (hasErrors) {
                loginDialog.hide();
                return;
            }

            Retrofit retrofit = getRetrofit();

            AuthApi authApi = retrofit.create(AuthApi.class);

            authApi.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {

                    if (!response.isSuccessful()) {
                        Toast.makeText(getContext(), R.string.login_bad_credentials, Toast.LENGTH_LONG).show();
                        loginDialog.hide();
                        return;
                    }

                    LoginResponse loginResponse = response.body();

                    UserGetResponse ugr = new UserGetResponse();
                    ugr.setUserId(loginResponse.getUserId());
                    ugr.setEmail(loginResponse.getEmail());
                    ugr.setFirstName(loginResponse.getFirstName());
                    ugr.setLastName(loginResponse.getLastName());

                    Gson gson = new Gson();
                    String json = gson.toJson(ugr);

                    editor.putString(SHARED_PREF_USER, json);
                    editor.apply();

                    loginDialog.hide();

                    Intent intent = new Intent(getContext(), RootActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), R.string.login_general_error, Toast.LENGTH_LONG).show();
                    Log.e("ERROR", "onFailure: ", t);
                    loginDialog.hide();
                }
            });
        });

        return root;
    }

    public boolean isEmail(String email) {
        return Pattern.compile("^(.+)@(\\S+)$")
                .matcher(email)
                .matches();
    }
}
