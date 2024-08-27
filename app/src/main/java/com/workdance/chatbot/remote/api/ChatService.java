package com.workdance.chatbot.remote.api;

import com.workdance.chatbot.remote.dto.BaseResult;
import com.workdance.chatbot.remote.dto.rep.ChatDetailRep;
import com.workdance.chatbot.remote.dto.rep.ChatHistoryRep;
import com.workdance.chatbot.remote.dto.rep.ChatItemRep;
import com.workdance.chatbot.remote.dto.rep.MessageItemRep;
import com.workdance.chatbot.remote.dto.req.ChatHistoryReq;
import com.workdance.chatbot.remote.dto.req.ChatReq;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

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

    // 对话服务
    @GET("/api/v1/chat/{id}/question")
    @Streaming
    Call<ResponseBody> chatToModel(@Path("id") String id, @Query("question") String question, @Query("conversationId") String conversationId);
}
