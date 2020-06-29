package com.example.firebaseuirecycleview;

import java.io.Serializable;

public class ViewStory implements Serializable {
    private String title;
    private String description;

    public ViewStory() {
    }

    public ViewStory(String title, String description) {
        this.title = title;
        this.description = description;
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
}
