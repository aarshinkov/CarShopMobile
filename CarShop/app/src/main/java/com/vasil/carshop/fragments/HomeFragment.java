package com.vasil.carshop.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vasil.carshop.R;

public class HomeFragment extends Fragment {

    private Button homeBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeBtn = root.findViewById(R.id.homeBtn);

//        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
//
//        homeBtn.setOnClickListener(v -> {
//            navigationView.findViewById(R.id.nav_login).performClick();
////            View view = navigationView.findViewById(R.id.nav_login);
////            Toast.makeText(getContext(), (view != null) + "", Toast.LENGTH_LONG).show();
//        });

        return root;
    }
}
