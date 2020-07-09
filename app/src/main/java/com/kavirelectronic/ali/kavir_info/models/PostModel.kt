package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class PostModel {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("author")
    var author: String? = null

    @SerializedName("comments")
    var commentModelList: List<CommentModel>? = null

    @SerializedName("url")
    var url: String? = null

    @SerializedName("comment_status")
    var commentStatus: String? = null

}