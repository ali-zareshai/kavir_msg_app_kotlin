package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class SecretCodeModel {
    @SerializedName("status")
    var status: String? = null

    @SerializedName("result")
    var result: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("s")
    var secretCode: String? = null

    @SerializedName("ver_code")
    var versionCode: String? = null

    @SerializedName("ver_name")
    var versionName: String? = null

    @SerializedName("url")
    var updateUrl: String? = null

}