package com.shafa.ali.kavir_msg.db.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Post extends RealmObject {
    @Required
    private String id;
    private String title;
    private String content;
    private String date;
    private String author;
    private String url;

    public Post() {
    }

    public Post(String id, String title, String content, String date, String author, String url) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
