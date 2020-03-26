package com.shafa.ali.kavir_msg.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveItem {
        public static final String USER_FIRST_NAME ="firstname";
        public static final String USER_LAST_NAME  ="lastname";
        public static final String USER_NAME  ="username";
        public static final String USER_EMAIL ="useremail";
        public static final String USER_ID    ="userid";
        public static final String USER_MOBILE="usermobile";
        public static final String USER_COOKIE="usercookie";
        public static final String USERNAME_LOGIN ="username_login";
        public static final String PASSWORD_LOGIN ="password_login";
        public static final String S_CODE ="s_code";
        public static final String MID_CODE ="mid";
        public static final String REGISTER_PHONE ="register_phone";
        public static final String APK_ID ="apk_id";
        public static final String raw_Scode="raw_s";
        public static final String COME_NEW_VERSION ="new_version";
        public static final String NEW_VERSION_NAME="version_name";
        public static final String NEW_VERSION_URL ="version_url";


        public static SharedPreferences getSP(Context context){
            return PreferenceManager.getDefaultSharedPreferences(context);
        }

        public static SharedPreferences.Editor getEdit(Context context){
            return getSP(context).edit();
        }

        public static void setItem(Context context,String key,String value){
            getEdit(context).putString(key,value).apply();
        }

        public static String getItem(Context context,String key,String defaultVal){
            String v = getSP(context).getString(key,"");
            if (v.equals("") || v.isEmpty()){
                return defaultVal.trim();
            }
            return v.trim();
        }
    }


