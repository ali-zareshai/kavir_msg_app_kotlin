package com.shafa.ali.kavir_msg.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.utility.Utility;


public class AboutFragment extends Fragment {
    private static Fragment fragment = null;
    private WebView webView;



    public AboutFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        if (fragment == null){
            fragment = new AboutFragment();
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
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        webView =(WebView)view.findViewById(R.id.webview_about);
        webView.loadUrl("https://www.kavir.info/fa/about-apk/");
        return view;
    }

}
