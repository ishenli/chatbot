package com.workdance.chatbot.api;

import com.workdance.chatbot.api.dto.BaseResult;
import com.workdance.chatbot.api.dto.rep.ChatDetailRep;
import com.workdance.chatbot.api.dto.rep.ChatHistoryRep;
import com.workdance.chatbot.api.dto.rep.ChatItemRep;
import com.workdance.chatbot.api.dto.rep.MessageItemRep;
import com.workdance.chatbot.api.dto.req.ChatHistoryReq;
import com.workdance.chatbot.api.dto.req.ChatReq;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ChatService {
    @POST("/api/v1/chat/listChat")
    Call<BaseResult<List<ChatItemRep>>> listChat(@Body ChatReq chatReq);

    @GET("/api/v1/chat/{id}")
    Call<BaseResult<ChatDetailRep>> detail(@Path("id") String id);

    @POST("/api/v1/chat/add")
    Call<BaseResult<ChatDetailRep>> addChat(@Body ChatReq chatReq);

    @DELETE("/api/v1/chat/{id}")
    Call<BaseResult<String>> deleteChat(@Path("id") String id);

    @PUT("/api/v1/chat/{id}")
    Call<BaseResult<ChatDetailRep>> modifyChat(@Path("id") String id, @Body ChatReq chatReq);



    @POST("/api/v1/chatHistory/list")
    Call<BaseResult<List<MessageItemRep>>> listMessage(@Body ChatReq chatReq);

    @POST("/api/v1/chatHistory/add")
    Call<BaseResult<ChatHistoryRep>> createChatHistory(@Body ChatReq chatReq);

    @PUT("/api/v1/chatHistory/{messageId}")
    Call<BaseResult<ChatHistoryRep>> modifyChatHistory(@Path("messageId") String id, @Body ChatHistoryReq chatReq);


    // 通用服务
    @POST("/api/v1/upload")
    @Multipart
    Call<BaseResult<String>> uploadFile(@Part MultipartBody.Part file,  @Part("description") RequestBody description);
}
