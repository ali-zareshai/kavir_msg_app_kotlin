package com.shafa.ali.kavir_msg.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.models.RegisterModel;
import com.shafa.ali.kavir_msg.server.LoginServer;
import com.shafa.ali.kavir_msg.utility.RetrofitClientInstance;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.shafa.ali.kavir_msg.utility.Utility;
import com.valdesekamdem.library.mdtoast.MDToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static android.app.Fragment fragment = null;
    private EditText nameEd,emailEd,phoneEd,passEd;
    private TextView registerBtn;
    private ProgressBar loadingProgressBar;

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
        nameEd  = (EditText)view.findViewById(R.id.nameuser);
        emailEd = (EditText)view.findViewById(R.id.emailuser);
        phoneEd = (EditText)view.findViewById(R.id.mobileuser);
        passEd  = (EditText)view.findViewById(R.id.pswrdd);
        registerBtn =(TextView)view.findViewById(R.id.registerBtn);
        loadingProgressBar = (ProgressBar)view.findViewById(R.id.progress_register);

        registerBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerBtn:
                sendRegisterDate();
                break;
        }
    }

    private void sendRegisterDate() {
        if (checkData()){
            MDToast.makeText(getActivity().getApplicationContext(),getActivity().getString(R.string.full_all_fields),2500,MDToast.TYPE_SUCCESS).show();
        }

        sendData(nameEd.getText().toString(),emailEd.getText().toString(),phoneEd.getText().toString(),passEd.getText().toString());
    }

    private void sendData(String name, String email, final String phone, String password) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        LoginServer loginServer = retrofit.create(LoginServer.class);
        loginServer.registerUser(name,phone,email,password, calMID(phone.split("")),Utility.calSCode(getActivity().getApplicationContext(),phone.split(""))).enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.body().getStatus().equalsIgnoreCase("success")){
                    SaveItem.setItem(getActivity().getApplicationContext(),SaveItem.S_CODE,response.body().getScode());
                    SaveItem.setItem(getActivity().getApplicationContext(),SaveItem.MID_CODE,response.body().getMid());
                    SaveItem.setItem(getActivity().getApplicationContext(),SaveItem.USER_ID,response.body().getUserId());
                    SaveItem.setItem(getActivity().getApplicationContext(),SaveItem.REGISTER_PHONE,phone);
                    MDToast.makeText(getActivity().getApplicationContext(),getActivity().getApplicationContext().getString(R.string.register_success),2500,MDToast.TYPE_SUCCESS).show();
                }else {
                    MDToast.makeText(getActivity().getApplicationContext(),getActivity().getApplicationContext().getString(R.string.register_fail),2500,MDToast.TYPE_ERROR).show();
                }
                loadingProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                MDToast.makeText(getActivity().getApplicationContext(),getActivity().getApplicationContext().getString(R.string.error_in_connection),2500,MDToast.TYPE_ERROR).show();
                loadingProgressBar.setVisibility(View.INVISIBLE);
                Log.e("onFailure:",t.toString());
            }
        });
    }

    private boolean checkData() {
        if (nameEd.getText().toString().isEmpty() || emailEd.getText().toString().isEmpty() || phoneEd.getText().toString().isEmpty()|| passEd.getText().toString().isEmpty()){
            return true;
        }
        if (phoneEd.getText().toString().length()!=11){
            return true;
        }
        return false;
    }

    private String calMID(String[] numberPhone){
        try{
            int n10 = Integer.parseInt(numberPhone[10]);
            int n9  = Integer.parseInt(numberPhone[9]);
            String[] mac = Utility.getMacAddr().split("");
            return n9+mac[n9]+n10+mac[n10]+"";


        }catch (Exception e){
            return "";
        }
    }


}
