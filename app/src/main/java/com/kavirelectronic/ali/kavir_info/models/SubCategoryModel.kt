package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class SubCategoryModel {
    @SerializedName("id")
    var id = 0

    @SerializedName("slug")
    var slug: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("post_count")
    var post_count = 0

    @SerializedName("parent")
    var parent = 0

    constructor(id: Int, slug: String?, title: String?, description: String?, post_count: Int, parent: Int) {
        this.id = id
        this.slug = slug
        this.title = title
        this.description = description
        this.post_count = post_count
        this.parent = parent
    }

    constructor() {}

}