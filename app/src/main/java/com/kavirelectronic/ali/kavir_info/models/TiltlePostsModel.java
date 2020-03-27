package com.kavirelectronic.ali.kavir_info.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TiltlePostsModel {
    @SerializedName("pages")
    private String pages;

    @SerializedName("posts")
    private List<PostsModel> postsModels;

    public TiltlePostsModel() {
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public List<PostsModel> getPostsModels() {
        return postsModels;
    }

    public void setPostsModels(List<PostsModel> postsModels) {
        this.postsModels = postsModels;
    }

    public static class PostsModel{
        @SerializedName("id")
        private String id;

        @SerializedName("title")
        private String title;

        @SerializedName("date")
        private String date;

        @SerializedName("content")
        private String content;

        @SerializedName("author")
        private String author;

        @SerializedName("comment")
        private String commentCount;

        public PostsModel() {
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

}
