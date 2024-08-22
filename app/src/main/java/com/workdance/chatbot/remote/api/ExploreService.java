package com.workdance.chatbot.remote.api;

import com.workdance.chatbot.remote.dto.BaseResult;
import com.workdance.chatbot.remote.dto.rep.CycleHomeRep;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ExploreService {

    @GET("api/v1/listCycle")
    Call<BaseResult<CycleHomeRep>> cycleHome();
}
