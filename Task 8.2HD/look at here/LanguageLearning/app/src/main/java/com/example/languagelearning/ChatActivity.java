package com.example.languagelearning;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private List<ChatHistoryItem> chatHistory;

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get username and language from intent extras
        String username = getIntent().getStringExtra("username");
        language = getIntent().getStringExtra("language");

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        // Initialize chat messages and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // Initialize chat history
        chatHistory = new ArrayList<>();

        // Set click listener for send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    // Create user message and add to chat
                    ChatMessage userMessage = new ChatMessage(username, message, true);
                    chatMessages.add(userMessage);
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    messageEditText.setText("");

                    // Update chat history
                    chatHistory.add(new ChatHistoryItem("User", message));
                    getChatbotResponse(message);
                }
            }
        });
    }

    // Method to get chatbot response from server
    private void getChatbotResponse(String message) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/") // Set base URL
                .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter
                .client(new OkHttpClient.Builder().readTimeout(10, TimeUnit.MINUTES).build()) // Set timeout
                .build();

        ChatbotService chatbotService = retrofit.create(ChatbotService.class);

        // Create request object
        ChatRequest request = new ChatRequest(message, chatHistory, language);

        // Make asynchronous call to server
        Call<ChatbotResponse> call = chatbotService.sendMessage(request);
        call.enqueue(new Callback<ChatbotResponse>() {
            @Override
            public void onResponse(Call<ChatbotResponse> call, Response<ChatbotResponse> response) {
                if (response.isSuccessful()) {
                    // Get response message
                    ChatbotResponse chatbotResponse = response.body();
                    String chatbotMessage = chatbotResponse.getMessage();

                    // Update chat history
                    chatHistory.add(new ChatHistoryItem("Llama", chatbotMessage));

                    // Update RecyclerView
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ChatMessage chatbotMessageObject = new ChatMessage("Chatbot", chatbotMessage, false);
                            chatMessages.add(chatbotMessageObject);
                            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                            recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                        }
                    });
                } else {
                    // Handle error response
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChatActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ChatbotResponse> call, Throwable t) {
                // Handle request failure
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // ChatMessage class to hold message data
    private static class ChatMessage {
        String sender;
        String message;
        boolean isUser;

        ChatMessage(String sender, String message, boolean isUser) {
            this.sender = sender;
            this.message = message;
            this.isUser = isUser;
        }
    }

    // Adapter for RecyclerView
    private static class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
        private List<ChatMessage> chatMessages;

        ChatAdapter(List<ChatMessage> chatMessages) {
            this.chatMessages = chatMessages;
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // Inflate chat message layout
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {
            // Bind chat message data to view
            ChatMessage chatMessage = chatMessages.get(position);
            holder.bind(chatMessage);
        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }
    }

    // ViewHolder for RecyclerView items
    private static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView messageTextView;

        ChatViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        void bind(ChatMessage chatMessage) {
            // Set text for sender and message
            senderTextView.setText(chatMessage.sender);
            messageTextView.setText(chatMessage.message);

            // Adjust layout parameters based on message sender
            if (chatMessage.isUser) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
                senderTextView.setLayoutParams(params);
                messageTextView.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.CENTER_VERTICAL | Gravity.START;
                senderTextView.setLayoutParams(params);
                messageTextView.setLayoutParams(params);
            }
        }
    }

    // Retrofit interface for chatbot service
    public interface ChatbotService {
        @POST("/chat")
        Call<ChatbotResponse> sendMessage(@Body ChatRequest request);
    }

    // Response class for chatbot response
    public static class ChatbotResponse {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    // ChatHistoryItem class to hold chat history data
    public static class ChatHistoryItem {
        private String User;
        private String Llama;

        public ChatHistoryItem(String user, String llama) {
            User = user;
            Llama = llama;
        }

        // Getter and setter methods
        public String getUser() {
            return User;
        }

        public void setUser(String user) {
            User = user;
        }

        public String getLlama() {
            return Llama;
        }

        public void setLlama(String llama) {
            Llama = llama;
        }
    }

    // Request class for sending message to chatbot
    public static class ChatRequest {
        private String userMessage;
        private List<ChatHistoryItem> chatHistory;
        private String language;

        public ChatRequest(String userMessage, List<ChatHistoryItem> chatHistory, String language) {
            this.userMessage = userMessage;
            this.chatHistory = chatHistory;
            this.language = language;
        }

        // Getter and setter methods
        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }

        public List<ChatHistoryItem> getChatHistory() {
            return chatHistory;
        }

        public void setChatHistory(List<ChatHistoryItem> chatHistory) {
            this.chatHistory = chatHistory;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
}