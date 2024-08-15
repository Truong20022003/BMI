package com.example.bmi.Model;

public class ChatRequest {
    private String content;
    private String language;
    private boolean stream;

    public ChatRequest(String content, String language, boolean stream) {
        this.content = content;
        this.language = language;
        this.stream = stream;
    }

    // Getters and setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}

