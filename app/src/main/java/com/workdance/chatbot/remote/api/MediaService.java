package com.workdance.chatbot.remote.api;

import com.workdance.chatbot.remote.dto.MediaResult;
import com.workdance.chatbot.remote.dto.rep.DramaRep;
import com.workdance.chatbot.remote.dto.req.DramaReq;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MediaService {

    @POST("/api/drama/v1/listDrama")
    Call<MediaResult<List<DramaRep>>> listDrama(@Body DramaReq dramaReq);

}
