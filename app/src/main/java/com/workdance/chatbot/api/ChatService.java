package com.workdance.chatbot.api;

import com.workdance.chatbot.api.dto.BaseResult;
import com.workdance.chatbot.api.dto.BrainItemRep;
import com.workdance.chatbot.api.dto.ChatDetailRep;
import com.workdance.chatbot.api.dto.ChatItemRep;
import com.workdance.chatbot.api.dto.ChatReq;
import com.workdance.chatbot.api.dto.MessageItemRep;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatService {
    @POST("/api/v1/chat/listChat")
    Call<BaseResult<List<ChatItemRep>>> listChatItems(@Body ChatReq chatReq);

    @GET("/api/v1/chat/{id}")
    Call<BaseResult<ChatDetailRep>> detail(@Path("id") String id);


    @POST("/api/v1/chatHistory/list")
    Call<BaseResult<List<MessageItemRep>>> listMessage(@Body ChatReq chatReq);


    @POST("/api/v1/brain/list")
    Call<BaseResult<List<BrainItemRep>>> listBrain(@Body ChatReq chatReq);
}
