package com.kavirelectronic.ali.kavir_info.models;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("id")
    private int id;

    @SerializedName("slug")
    private String slug;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("post_count")
    private int post_count;

    @SerializedName("parent")
    private int parent;

    @SerializedName("sub")
    private int subCount;

    public CategoryModel(int id, String slug, String title, String description, int post_count, int parent,int sub) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.post_count = post_count;
        this.parent = parent;
        this.subCount =sub;
    }

    public CategoryModel() {

    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }
}
