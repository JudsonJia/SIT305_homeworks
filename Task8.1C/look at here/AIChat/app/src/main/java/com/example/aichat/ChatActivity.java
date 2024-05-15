package com.example.aichat;

import android.os.Bundle;
import android.util.Log;
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


import com.google.gson.Gson;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private List<ChatHistoryItem> chatHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String username = getIntent().getStringExtra("username");

        recyclerView = findViewById(R.id.recyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        chatHistory = new ArrayList<>();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    ChatMessage userMessage = new ChatMessage(username, message, true);
                    chatMessages.add(userMessage);
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    messageEditText.setText("");

                    //update the chat history
                    chatHistory.add(new ChatHistoryItem("User", message));
                    getChatbotResponse(message);
                }
            }
        });
    }


    private void getChatbotResponse(String message) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, TimeUnit.MINUTES).build())
                .build();

        ChatbotService chatbotService = retrofit.create(ChatbotService.class);

        // 创建请求对象
        ChatRequest request = new ChatRequest(message, chatHistory);

        Call<ChatbotResponse> call = chatbotService.sendMessage(request);
        call.enqueue(new Callback<ChatbotResponse>() {
            @Override
            public void onResponse(Call<ChatbotResponse> call, Response<ChatbotResponse> response) {
                if (response.isSuccessful()) {
                    ChatbotResponse chatbotResponse = response.body();

                    // 从解析后的对象中获取消息内容
                    String chatbotMessage = chatbotResponse.getMessage();

                    // 更新 chatHistory
                    chatHistory.add(new ChatHistoryItem("Llama", chatbotMessage));

                    // 更新 RecyclerView
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
                    // 处理错误响应
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
                // 处理请求失败
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


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

    private static class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
        private List<ChatMessage> chatMessages;

        ChatAdapter(List<ChatMessage> chatMessages) {
            this.chatMessages = chatMessages;
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_message, parent, false);
            return new ChatViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {
            ChatMessage chatMessage = chatMessages.get(position);
            holder.bind(chatMessage);
        }

        @Override
        public int getItemCount() {
            return chatMessages.size();
        }
    }

    private static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView messageTextView;

        ChatViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        void bind(ChatMessage chatMessage) {
            senderTextView.setText(chatMessage.sender);
            messageTextView.setText(chatMessage.message);

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
}