package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class TiltlePostsModel {
    @SerializedName("pages")
    var pages: String? = null

    @SerializedName("posts")
    var postsModels: List<PostsModel>? = null

    class PostsModel {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("title")
        var title: String? = null

        @SerializedName("date")
        var date: String? = null

        @SerializedName("content")
        var content: String? = null

        @SerializedName("author")
        var author: String? = null

        @SerializedName("comment")
        var commentCount: String? = null

    }
}