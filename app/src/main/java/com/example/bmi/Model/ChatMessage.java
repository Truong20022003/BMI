package com.example.bmi.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage {
    private String message;
    private boolean isBot;
    private String timestamp;

    public ChatMessage(String message, boolean isBot) {
        this.message = message;
        this.isBot = isBot;
        this.timestamp = getCurrentTimestamp(); // Initialize timestamp with current time
    }

    public ChatMessage(String message, boolean isBot, String timestamp) {
        this.message = message;
        this.isBot = isBot;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBot() {
        return isBot;
    }

    public String getTimestamp() {
        return timestamp;
    }

    private String getCurrentTimestamp() {
        // Method to get current timestamp as a string
        // Example implementation, you might want to format it differently
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }
}
