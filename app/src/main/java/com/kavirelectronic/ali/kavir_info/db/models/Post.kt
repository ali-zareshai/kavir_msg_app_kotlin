package com.kavirelectronic.ali.kavir_info.db.models

import io.realm.RealmObject
import io.realm.annotations.Required

open class Post : RealmObject {
    @Required
    var id: String? = null
    var title: String? = null
    var content: String? = null
    var date: String? = null
    var author: String? = null
    var url: String? = null

    constructor() {}
    constructor(id: String?, title: String?, content: String?, date: String?, author: String?, url: String?) {
        this.id = id
        this.title = title
        this.content = content
        this.date = date
        this.author = author
        this.url = url
    }

}