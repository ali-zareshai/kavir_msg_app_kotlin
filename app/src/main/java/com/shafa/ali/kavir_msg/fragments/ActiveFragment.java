package com.shafa.ali.kavir_msg.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.activity.QrCodeScanerActivity;
import com.shafa.ali.kavir_msg.utility.FormatHelper;
import com.shafa.ali.kavir_msg.utility.SaveItem;


public class ActiveFragment extends Fragment {
    private static Fragment fragment = null;
    private TextView activeCode;
    private Button scanCodeBtn;
    public ActiveFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance() {
        if (fragment == null){
            fragment = new ActiveFragment();
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
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        scanCodeBtn=(Button)view.findViewById(R.id.scan_code_btn);
        activeCode=(TextView)view.findViewById(R.id.active_code_tv);
        activeCode.setText(calActiveCode());
        scanCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity().getApplicationContext(), QrCodeScanerActivity.class));
            }
        });
        return view;
    }

    private String calActiveCode(){
        String code = SaveItem.getItem(getActivity().getApplicationContext(),SaveItem.MID_CODE,"")+SaveItem.getItem(getActivity().getApplicationContext(),SaveItem.USER_ID,"");
        return FormatHelper.toPersianNumber(code);
    }
}
