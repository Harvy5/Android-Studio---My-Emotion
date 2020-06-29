package com.example.firebaseuirecycleview;

public class Story {
    private String title;
    private String description;
    private int priority;

    public Story()
    {

    }

    public Story(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
