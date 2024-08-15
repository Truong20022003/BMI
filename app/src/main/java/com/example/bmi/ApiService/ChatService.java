package com.example.bmi.ApiService;


import com.example.bmi.Model.ChatRequest;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface ChatService {
    @Streaming
    @POST("/chat/generate")
    Observable<ResponseBody> chatData(@Body ChatRequest request);
}
