package com.shafa.ali.kavir_msg.models;

import com.google.gson.annotations.SerializedName;

public class RegisterModel {
    @SerializedName("result")
    private String status;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("message")
    private String message;

    @SerializedName("s")
    private String scode;

    @SerializedName("mid")
    private String mid;

    public RegisterModel() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
