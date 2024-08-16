package com.workdance.chatbot.remote.api;

import com.workdance.chatbot.remote.dto.BaseResult;
import com.workdance.chatbot.remote.dto.rep.VideoRep;
import com.workdance.chatbot.remote.dto.req.FeedStreamReq;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoService {

    @GET("api/v1/listVideo")
    Call<BaseResult<List<VideoRep>>> listVideo(@Query("req") FeedStreamReq request);
}
