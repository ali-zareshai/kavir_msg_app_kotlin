package com.kavirelectronic.ali.kavir_info.models

import com.google.gson.annotations.SerializedName

class AccessModel {
    @SerializedName("id_group")
    var groupId: String? = null

    @SerializedName("groupname")
    var groupName: String? = null

    @SerializedName("groupdesc")
    var groupDes: String? = null

    @SerializedName("id_product")
    var productId: String? = null

    @SerializedName("product_des")
    var prodectDes: String? = null

}