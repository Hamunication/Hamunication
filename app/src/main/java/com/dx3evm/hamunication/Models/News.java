package com.dx3evm.hamunication.Models;

public class News {

    private int newsImage;
    private String newsTitle;
    private String newsEditor;
    private String newsTime;

    public News(int newsImage, String newsTitle, String newsEditor, String newsTime) {
        this.newsImage = newsImage;
        this.newsTitle = newsTitle;
        this.newsEditor = newsEditor;
        this.newsTime = newsTime;
    }

    public int getNewsImage() {
        return newsImage;
    }

    public void setNewsImage(int newsImage) {
        this.newsImage = newsImage;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
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
