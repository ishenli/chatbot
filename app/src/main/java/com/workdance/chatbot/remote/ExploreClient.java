package com.workdance.chatbot.remote;

import static com.workdance.chatbot.config.Constant.MEDIA_SERVICE_HOSTNAME_MOCK;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.workdance.chatbot.config.Constant;
import com.workdance.chatbot.model.Circle;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.remote.api.ExploreService;
import com.workdance.chatbot.remote.dto.BaseResult;
import com.workdance.chatbot.remote.dto.rep.CycleHomeRep;
import com.workdance.chatbot.remote.dto.req.FeedStreamReq;
import com.workdance.core.data.Page;
import com.workdance.core.dto.OperateResult;
import com.workdance.core.enums.ErrorCodeEnum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExploreClient {
    private static final String TAG = "ExploreClient";
    private static final String Base_URL = MEDIA_SERVICE_HOSTNAME_MOCK;
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static ExploreService getExploreService() {
        return getClient().create(ExploreService.class);
    }

    public static LiveData<OperateResult<Page<Circle>>> listCycle(int pageIndex, int pageSize) {
        MutableLiveData<OperateResult<Page<Circle>>> vm = new MutableLiveData<>();
        FeedStreamReq feedStreamReq = new FeedStreamReq();
        feedStreamReq.setOffset(pageIndex * pageSize);
        feedStreamReq.setPageSize(pageSize);
        feedStreamReq.setUserID(Constant.SHORT_VIDEO_USER_ID);
        feedStreamReq.setAuthorId(Constant.SHORT_VIDEO_AUTHOR_ID);

        getExploreService().cycleHome().enqueue(new Callback<BaseResult<CycleHomeRep>>() {
            @Override
            public void onResponse(Call<BaseResult<CycleHomeRep>> call, Response<BaseResult<CycleHomeRep>> response) {
                if (response.isSuccessful()) {
                    CycleHomeRep data = response.body().getData();
                    if (data != null) {
                        List<Circle> items = new ArrayList<>();
                        Circle header = new Circle();
                        items.add(header); // 添加一个头部占位
                        for (Circle detail : data.getCycleList()) {
                            UserInfo user = new UserInfo();
                            user.uid = ChatApi.getDefaultUser().uid;
                            user.portrait = ChatApi.getDefaultUser().portrait;
                            user.displayName = ChatApi.getDefaultUser().displayName;
                            detail.setUser(user);
                            items.add(detail);
                        }
                        Page<Circle> result = new Page<>(items, pageIndex, Page.TOTAL_INFINITY);
                        vm.setValue(new OperateResult<>(result));
                    } else {
                        vm.setValue(new OperateResult<>(null, ErrorCodeEnum.SYSTEM_ERROR.getErrDtlCode()));
                    }
                }
            }
            @Override
            public void onFailure(Call<BaseResult<CycleHomeRep>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                vm.setValue(new OperateResult<>(null, ErrorCodeEnum.NETWORK_ERROR.getErrDtlCode()));
            }
        });

        return vm;
    }

}