package com.example.bmi.Ui.ChatAI.Adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.Database.ChatDatabaseHelper;
import com.example.bmi.Model.ChatMessage;
import com.example.bmi.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> messages = new ArrayList<>();
    private final TextView textViewEmptyConversation;
    private final ChatDatabaseHelper dbHelper;

    // Constructor
    public ChatAdapter(Context context, @Nullable TextView textViewEmptyConversation) {
        this.textViewEmptyConversation = textViewEmptyConversation;
        this.dbHelper = new ChatDatabaseHelper(context);
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void updateLastMessage(String message) {
        Log.v("streaming", "msg    " + message + "   " + message.isEmpty());
        if (!messages.isEmpty()) {
            messages.get(messages.size() - 1).setMessage(message);
            notifyItemChanged(messages.size() - 1);
        }
    }

    // Add a new message to the list and database
    public void addMessage(String message, boolean isBot) {
        ChatMessage chatMessage = new ChatMessage(message, isBot);
        messages.add(chatMessage);
        notifyDataSetChanged(); // Notify RecyclerView about data change
        checkEmptyState(); // Check if conversation is empty
        if (!isBot) {
            saveMessageToDatabase(chatMessage); // Save message to database
        }
    }

    // Remove a message from the list and database
    public void removeMessage(String message) {
        for (int i = messages.size() - 1; i >= 0; i--) {
            if (messages.get(i).getMessage().equals(message)) {
                ChatMessage removedMessage = messages.remove(i);
                notifyItemRemoved(i); // Notify RecyclerView about item removal
                removeMessageFromDatabase(removedMessage); // Remove message from database
                break;
            }
        }
        checkEmptyState(); // Check if conversation is empty
    }

    // Save message to the database
    public void saveMessageToDatabase(ChatMessage chatMessage) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ChatDatabaseHelper.COLUMN_MESSAGE, chatMessage.getMessage());
        values.put(ChatDatabaseHelper.COLUMN_IS_BOT, chatMessage.isBot() ? 1 : 0);
        values.put(ChatDatabaseHelper.COLUMN_TIMESTAMP, chatMessage.getTimestamp());
        db.insert(ChatDatabaseHelper.TABLE_MESSAGES, null, values);
    }

    // Remove message from the database
    private void removeMessageFromDatabase(ChatMessage chatMessage) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(ChatDatabaseHelper.TABLE_MESSAGES,
                ChatDatabaseHelper.COLUMN_MESSAGE + " = ?",
                new String[]{chatMessage.getMessage()});
    }

    // Check if conversation is empty and update UI accordingly
    private void checkEmptyState() {
        if (messages.isEmpty() && textViewEmptyConversation != null) {
            textViewEmptyConversation.setVisibility(View.VISIBLE);
        } else {
            if (textViewEmptyConversation != null) {
                textViewEmptyConversation.setVisibility(View.GONE);
            }
        }
    }

    // Inflate the item layout and create the ViewHolder
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    // Bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bind(message);
    }

    // Return the total number of items in the list
    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder class
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textMessageBot;
        TextView textMessageUser;
        TextView textTimestampBot;
        TextView textTimestampUser;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessageBot = itemView.findViewById(R.id.textMessageBot);
            textMessageUser = itemView.findViewById(R.id.textMessageUser);
            textTimestampBot = itemView.findViewById(R.id.textTimestampBot);
            textTimestampUser = itemView.findViewById(R.id.textTimestampUser);
        }

        // Bind data to views
        public void bind(ChatMessage message) {
            textTimestampBot.setVisibility(View.GONE);
            textTimestampUser.setVisibility(View.GONE);

            if (message.isBot()) {
                if (message.getMessage().equals("Please wait")) {
                    textMessageBot.setText("Please wait...");
                    textMessageBot.setVisibility(View.VISIBLE);
                    textMessageUser.setVisibility(View.GONE);
                } else {
                    textMessageBot.setText(message.getMessage());
                    textTimestampBot.setText(message.getTimestamp());
                    textMessageBot.setVisibility(View.VISIBLE);
                    textTimestampBot.setVisibility(View.VISIBLE);
                    textMessageUser.setVisibility(View.GONE);
                }
            } else {
                textMessageUser.setText(message.getMessage());
                textTimestampUser.setText(message.getTimestamp());
                textMessageUser.setVisibility(View.VISIBLE);
                textTimestampUser.setVisibility(View.VISIBLE);
                textMessageBot.setVisibility(View.GONE);
            }
        }
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged(); // Notify RecyclerView about data change
        checkEmptyState(); // Check if conversation is empty
    }
}
