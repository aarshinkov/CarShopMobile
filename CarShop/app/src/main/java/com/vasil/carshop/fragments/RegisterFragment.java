package com.vasil.carshop.fragments;

import static com.vasil.carshop.utils.Api.getRetrofit;
import static com.vasil.carshop.utils.Constants.API_URL;
import static com.vasil.carshop.utils.Utils.getStringResource;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vasil.carshop.R;
import com.vasil.carshop.api.UsersApi;
import com.vasil.carshop.requests.users.UserCreateRequest;
import com.vasil.carshop.responses.users.UserCreateResponse;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterFragment extends Fragment {

    private EditText registerEmailET;
    private EditText registerPasswordET;
    private EditText registerConfirmPasswordET;
    private EditText registerFirstNameET;
    private EditText registerLastNameET;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_register, container, false);

        registerEmailET = root.findViewById(R.id.registerEmailET);
        registerPasswordET = root.findViewById(R.id.registerPasswordET);
        registerConfirmPasswordET = root.findViewById(R.id.registerConfirmPasswordET);
        registerFirstNameET = root.findViewById(R.id.registerFirstNameET);
        registerLastNameET = root.findViewById(R.id.registerLastNameET);
        Button registerBtn = root.findViewById(R.id.registerBtn);

        ProgressDialog registeringDialog = new ProgressDialog(getContext());
        registeringDialog.setMessage(getString(R.string.register_registering));

        registerBtn.setOnClickListener(v -> {
            registeringDialog.show();

            if (isFieldsValid()) {
                registeringDialog.hide();
                return;
            }

            String email = registerEmailET.getText().toString();
            String password = registerPasswordET.getText().toString();
            String confirmPassword = registerConfirmPasswordET.getText().toString();
            String firstName = registerFirstNameET.getText().toString();
            String lastName = registerLastNameET.getText().toString();

            Retrofit retrofit = getRetrofit();

            UsersApi usersApi = retrofit.create(UsersApi.class);

            UserCreateRequest ucr = new UserCreateRequest();
            ucr.setEmail(email);
            ucr.setPassword(password);
            ucr.setFirstName(firstName);
            ucr.setLastName(lastName);

            usersApi.createUser(ucr).enqueue(new Callback<UserCreateResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserCreateResponse> call, @NonNull Response<UserCreateResponse> response) {
                    if (!response.isSuccessful()) {
                        registeringDialog.hide();
                        Toast.makeText(getContext(), R.string.register_general_error, Toast.LENGTH_LONG).show();
                        return;
                    }

                    UserCreateResponse ucr = response.body();

                    Toast.makeText(getContext(), getString(R.string.register_success, ucr.getFullName()), Toast.LENGTH_LONG).show();
                    registeringDialog.hide();

                    getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_login).performClick();
                }

                @Override
                public void onFailure(@NonNull Call<UserCreateResponse> call, @NonNull Throwable st) {
                    registeringDialog.hide();
                    Toast.makeText(getContext(), R.string.register_general_error, Toast.LENGTH_LONG).show();
                }
            });
        });

        TextView registerHaveAccountTV = root.findViewById(R.id.registerHaveAccountTV);
        registerHaveAccountTV.setOnClickListener(v -> {
            getActivity().findViewById(R.id.nav_view).findViewById(R.id.nav_login).performClick();
        });

        return root;
    }

    private boolean isFieldsValid() {

        boolean hasErrors = false;

        String email = registerEmailET.getText().toString();
        String password = registerPasswordET.getText().toString();
        String confirmPassword = registerConfirmPasswordET.getText().toString();
        String firstName = registerFirstNameET.getText().toString();

        if (email.isEmpty()) {
            registerEmailET.setError(getString(R.string.register_error_email_empty));
            hasErrors = true;
        } else {
            if (!isEmail(email)) {
                registerEmailET.setError(getString(R.string.login_error_email_pattern));
                hasErrors = true;
            }
        }

        if (password.isEmpty()) {
            registerPasswordET.setError(getString(R.string.register_error_password_empty));
            hasErrors = true;
        }

        if (confirmPassword.isEmpty()) {
            registerConfirmPasswordET.setError(getString(R.string.register_error_confirmation_password_empty));
            hasErrors = true;
        }

        if (!password.equals(confirmPassword)) {
            String errorMessage = getString(R.string.register_error_passwords_not_match);
            registerPasswordET.setError(errorMessage);
            registerConfirmPasswordET.setError(errorMessage);
            hasErrors = true;
        }

        if (firstName.isEmpty()) {
            registerFirstNameET.setError(getString(R.string.register_error_first_name_empty));
            hasErrors = true;
        }

        return hasErrors;
    }

    public boolean isEmail(String email) {
        return Pattern.compile("^(.+)@(\\S+)$")
                .matcher(email)
                .matches();
    }
}
