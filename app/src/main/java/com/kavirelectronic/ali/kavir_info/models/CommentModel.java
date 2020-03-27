package com.kavirelectronic.ali.kavir_info.models;

import com.google.gson.annotations.SerializedName;

public class CommentModel {
    @SerializedName("date")
    private String date;

    @SerializedName("content")
    private String content;

    @SerializedName("name")
    private String name;

    public CommentModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
