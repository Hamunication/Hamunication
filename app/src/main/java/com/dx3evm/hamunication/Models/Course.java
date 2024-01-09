package com.dx3evm.hamunication.Models;

import java.io.Serializable;

public class Course implements Serializable {
    private String id;
    private String title;
    private String img;
    private String description;
    private String progress;

    public Course() {}
    public Course(String Title, String img, String description, String progress) {
        this.title = Title;
        this.description = description;
        this.img = img;
        this.progress = progress;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

}
