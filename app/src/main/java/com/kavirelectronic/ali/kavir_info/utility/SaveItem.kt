package com.kavirelectronic.ali.kavir_info.utility

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object SaveItem {
    const val USER_FIRST_NAME = "firstname"
    const val USER_LAST_NAME = "lastname"
    const val USER_NAME = "username"
    const val USER_EMAIL = "useremail"
    const val USER_ID = "userid"
    const val USER_MOBILE = "usermobile"
    const val USER_COOKIE = "usercookie"
    const val USERNAME_LOGIN = "username_login"
    const val PASSWORD_LOGIN = "password_login"
    const val S_CODE = "s_code"
    const val MID_CODE = "mid"
    const val REGISTER_PHONE = "register_phone"
    const val APK_ID = "apk_id"
    const val raw_Scode = "raw_s"
    const val COME_NEW_VERSION = "new_version"
    const val NEW_VERSION_NAME = "version_name"
    const val NEW_VERSION_URL = "version_url"
    fun getSP(context: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getEdit(context: Context?): SharedPreferences.Editor {
        return getSP(context).edit()
    }

    @JvmStatic
    fun setItem(context: Context?, key: String?, value: String?) {
        getEdit(context).putString(key, value).apply()
    }

    @JvmStatic
    fun getItem(context: Context?, key: String?, defaultVal: String): String {
        val v = getSP(context).getString(key, "")
        return if (v == "" || v?.isEmpty()!!) {
            defaultVal.trim { it <= ' ' }
        } else v.trim { it <= ' ' }
    }
}