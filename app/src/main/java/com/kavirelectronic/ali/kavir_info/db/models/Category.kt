package com.kavirelectronic.ali.kavir_info.db.models

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Required

open class Category: RealmObject {
    @Required
    var id:Int?= null
    var slug: String? = null
    var title: String? = null
    var description: String? = null
    var post_count:Int?=null
    var parent:Int?=null
    var subCount:Int?=null

    constructor(String: Int, slug: String?, title: String?, description: String?, post_count: Int, parent: Int, sub: Int) {
        this.id = id
        this.slug = slug
        this.title = title
        this.description = description
        this.post_count = post_count
        this.parent = parent
        subCount = sub
    }

    constructor() {}
}