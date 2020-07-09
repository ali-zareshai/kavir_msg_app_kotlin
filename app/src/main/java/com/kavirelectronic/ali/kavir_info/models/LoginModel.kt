package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class LoginModel {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("result")
    var result: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("user_firstname")
    var firstName: String? = null

    @SerializedName("user_lastname")
    var lastName: String? = null

    @SerializedName("user_email")
    var email: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("user_mobile")
    var mobile: String? = null

    @SerializedName("display_name")
    var displayName: String? = null

    @SerializedName("cookie")
    var cookie: String? = null

}