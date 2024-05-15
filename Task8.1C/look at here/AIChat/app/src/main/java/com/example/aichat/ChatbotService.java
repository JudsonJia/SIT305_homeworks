package com.example.aichat;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatbotService {
    @POST("/chat")
    Call<ChatbotResponse> sendMessage(@Body ChatRequest request);
}
