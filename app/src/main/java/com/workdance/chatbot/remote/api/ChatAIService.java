package com.workdance.chatbot.remote.api;

import com.workdance.chatbot.remote.dto.req.ChatReq;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface ChatAIService {
    @POST("/ollama")
    @Streaming
    Call<ResponseBody> streamOllama(@Body ChatReq chatReq);


    @POST("/chat/{chat_id}/question/stream")
    @Streaming
    Call<ResponseBody> askQuestionStream(@Path ("chat_id") String chatId, @Body ChatReq chatReq);
}
