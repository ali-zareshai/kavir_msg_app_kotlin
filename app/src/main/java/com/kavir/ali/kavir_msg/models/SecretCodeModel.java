package com.kavir.ali.kavir_msg.models;

import com.google.gson.annotations.SerializedName;

public class SecretCodeModel {
    @SerializedName("status")
    private String status;

    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    @SerializedName("s")
    private String secretCode;

    @SerializedName("ver_code")
    private String versionCode;

    @SerializedName("ver_name")
    private String versionName;

    @SerializedName("url")
    private String updateUrl;



    public SecretCodeModel() {
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
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

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
}
