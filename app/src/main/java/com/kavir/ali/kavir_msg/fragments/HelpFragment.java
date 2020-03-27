package com.kavir.ali.kavir_msg.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.kavir.ali.kavir_msg.R;

public class HelpFragment extends Fragment {
    private WebView webView;
    private static Fragment fragment = null;

    public HelpFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        if (fragment==null){
            fragment = new HelpFragment();
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
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        webView = (WebView)view.findViewById(R.id.webview_help);
//        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.kavir.info/fa/help-apk/");
        return view;
    }


}
