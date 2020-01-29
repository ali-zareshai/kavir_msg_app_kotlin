package com.shafa.ali.kavir_msg.models;

import com.google.gson.annotations.SerializedName;

public class RegisterModel {
    @SerializedName("status")
    private String status;

    @SerializedName("active_code")
    private String activeCode;

    @SerializedName("message")
    private String message;

    public RegisterModel() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
