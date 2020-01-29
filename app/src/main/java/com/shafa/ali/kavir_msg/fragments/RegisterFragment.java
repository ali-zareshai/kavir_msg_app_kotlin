package com.shafa.ali.kavir_msg.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shafa.ali.kavir_msg.R;


public class RegisterFragment extends Fragment {
    private static android.app.Fragment fragment = null;

    public RegisterFragment() {
    }


    public static Fragment newInstance() {
        if (fragment == null){
            fragment = new RegisterFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        return view;
    }

}
