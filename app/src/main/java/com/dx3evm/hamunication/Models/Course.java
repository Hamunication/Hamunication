package com.dx3evm.hamunication.Models;

import java.io.Serializable;

public class Course implements Serializable {
    private String id, title;

    public Course() {}
    public Course(String Title) {
        this.title = Title;
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
}
