package com.kavirelectronic.ali.kavir_info.models;

import com.google.gson.annotations.SerializedName;

public class AccessModel {
    @SerializedName("id_group")
    private String groupId;

    @SerializedName("groupname")
    private String groupName;

    @SerializedName("groupdesc")
    private String groupDes;

    @SerializedName("id_product")
    private String productId;

    @SerializedName("product_des")
    private String prodectDes;

    public AccessModel() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDes() {
        return groupDes;
    }

    public void setGroupDes(String groupDes) {
        this.groupDes = groupDes;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProdectDes() {
        return prodectDes;
    }

    public void setProdectDes(String prodectDes) {
        this.prodectDes = prodectDes;
    }
}
