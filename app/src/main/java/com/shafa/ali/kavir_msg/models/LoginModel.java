package com.shafa.ali.kavir_msg.models;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("status")
    private String status;

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("user_firstname")
    private String firstName;

    @SerializedName("user_lastname")
    private String lastName;

    @SerializedName("user_email")
    private String email;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("user_mobile")
    private String mobile;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("cookie")
    private String cookie;

    public LoginModel() {
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
