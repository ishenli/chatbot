package com.workdance.chatbot.remote.api;

import com.workdance.chatbot.remote.dto.BaseResult;
import com.workdance.chatbot.remote.dto.rep.BrainItemRep;
import com.workdance.chatbot.remote.dto.req.BrainReq;
import com.workdance.chatbot.remote.dto.req.ChatReq;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AssistantService {

    @POST("/api/v1/brain/list")
    Call<BaseResult<List<BrainItemRep>>> listBrain(@Body ChatReq chatReq);

    @GET("/api/v1/brain/{id}")
    Call<BaseResult<BrainItemRep>> brainDetail(@Path("id") String id);

    @POST("/api/v1/brain/add")
    Call<BaseResult<BrainItemRep>> addBrain(@Body BrainReq brainReq);
}
