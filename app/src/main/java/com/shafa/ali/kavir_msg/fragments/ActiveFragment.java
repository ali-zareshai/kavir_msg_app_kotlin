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
import android.widget.ImageButton;
import android.widget.TextView;

import com.shafa.ali.kavir_msg.R;
import com.shafa.ali.kavir_msg.activity.QrCodeScanerActivity;
import com.shafa.ali.kavir_msg.utility.FormatHelper;
import com.shafa.ali.kavir_msg.utility.SaveItem;
import com.valdesekamdem.library.mdtoast.MDToast;


public class ActiveFragment extends Fragment {
    private static Fragment fragment = null;
    private TextView activeCode;
    private Button scanCodeBtn;
    private ImageButton saveClipBoradBtn;
    public static TextView result_tv;
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
        saveClipBoradBtn=(ImageButton)view.findViewById(R.id.save_clipbord);
        result_tv = (TextView)view.findViewById(R.id.result_tv);
        activeCode.setText(FormatHelper.toEngNumber(SaveItem.getItem(getActivity(),SaveItem.APK_ID,"")));
        saveClipBoradBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(getActivity().getApplicationContext(),SaveItem.getItem(getActivity(),SaveItem.APK_ID,""));
                MDToast.makeText(getActivity(),getActivity().getString(R.string.copied),2500,MDToast.TYPE_INFO).show();
            }
        });
        scanCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity().getApplicationContext(), QrCodeScanerActivity.class));
            }
        });
        return view;
    }

//    private String calActiveCode(){
//        String mid =SaveItem.getItem(getActivity().getApplicationContext(),SaveItem.MID_CODE,"");
//        String userid = SaveItem.getItem(getActivity().getApplicationContext(),SaveItem.USER_ID,"");
//        if (!mid.equalsIgnoreCase("") && !userid.equalsIgnoreCase(""))
//            return mid+userid;
//        else {
//            MDToast.makeText(getActivity(),getActivity().getString(R.string.login_please),2500,MDToast.TYPE_INFO).show();
//            return "";
//        }
//
//    }

    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }
}
