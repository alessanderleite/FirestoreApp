package com.example.alessander.firestoreapp;

public class Note {
    private String title;
    private String description;

    public Note() {
        // no arguments constructor needed, just an empty constructor
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
