package com.dx3evm.hamunication.Models;

import java.io.Serializable;

public class News implements Serializable {

    private String newsId;
    private String newsImage;
    private String newsTitle;
    private String newsDescription;
    private String newsEditor;
    private String newsTime;

    public News(){}
    public News(String newsId,String newsTitle, String newsDescription, String newsEditor, String newsTime) {
        this.newsId = newsId;
        this.newsImage = newsImage;
        this.newsTitle = newsTitle;
        this.newsDescription = newsDescription;
        this.newsEditor = newsEditor;
        this.newsTime = newsTime;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(String newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getNewsEditor() {
        return newsEditor;
    }

    public void setNewsEditor(String newsEditor) {
        this.newsEditor = newsEditor;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }
}
