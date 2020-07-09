package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class CommentModel {
    @SerializedName("date")
    var date: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("name")
    var name: String? = null

}