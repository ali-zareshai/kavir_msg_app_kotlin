package com.kavirelectronic.ali.kavir_info.models;

import com.google.gson.annotations.SerializedName;

public class ActiveRespone {
    @SerializedName("result")
    private String result;

    @SerializedName("message")
    private String message;

    public ActiveRespone() {
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
}
