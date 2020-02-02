package com.shafa.ali.kavir_msg.models;

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

    public SecretCodeModel() {
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
