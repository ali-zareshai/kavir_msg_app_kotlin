package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class RegisterModel {
    @SerializedName("result")
    var status: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("s")
    var scode: String? = null

    @SerializedName("mid")
    var mid: String? = null

}