package com.workdance.chatbot.remote;

import static com.workdance.chatbot.config.Constant.MEDIA_SERVICE_HOSTNAME_MOCK;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.workdance.chatbot.config.Constant;
import com.workdance.chatbot.remote.api.VideoService;
import com.workdance.chatbot.remote.dto.BaseResult;
import com.workdance.chatbot.remote.dto.rep.VideoRep;
import com.workdance.chatbot.remote.dto.req.FeedStreamReq;
import com.workdance.core.data.Page;
import com.workdance.core.dto.OperateResult;
import com.workdance.core.enums.ErrorCodeEnum;
import com.workdance.multimedia.scene.model.VideoItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoClient {
    private static final String TAG = "VideoClient";
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


    public static VideoService getVideoService() {
        return getClient().create(VideoService.class);
    }

    public static LiveData<OperateResult<Page<VideoItem>>> listVideo(int pageIndex, int pageSize) {
        MutableLiveData<OperateResult<Page<VideoItem>>> data = new MutableLiveData<>();
        FeedStreamReq feedStreamReq = new FeedStreamReq();
        feedStreamReq.setOffset(pageIndex * pageSize);
        feedStreamReq.setPageSize(pageSize);
        feedStreamReq.setUserID(Constant.SHORT_VIDEO_USER_ID);
        feedStreamReq.setAuthorId(Constant.SHORT_VIDEO_AUTHOR_ID);

        getVideoService().listVideo(feedStreamReq).enqueue(new Callback<BaseResult<List<VideoRep>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<VideoRep>>> call, Response<BaseResult<List<VideoRep>>> response) {
                if (response.isSuccessful()) {
                    List<VideoRep> details = response.body().getData();
                    List<VideoItem> items = new ArrayList<>();
                    if (details != null) {
                        for (VideoRep detail : details) {
                            VideoItem item = VideoItem.createVideoItem(
                                    detail.getVideoId(),
                                    (long) detail.getDuration(),
                                    detail.getCoverUrl(),
                                    detail.getTitle(),
                                    detail.getVideoUrl()
                            );
                            items.add(item);
                        }
                        Page<VideoItem> result = new Page<>(items, pageIndex, Page.TOTAL_INFINITY);
                        data.setValue(new OperateResult<>(result));
                    } else {
                        data.setValue(new OperateResult<>(null, ErrorCodeEnum.SYSTEM_ERROR.getErrDtlCode()));
                    }
                }
            }
            @Override
            public void onFailure(Call<BaseResult<List<VideoRep>>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(new OperateResult<>(null, ErrorCodeEnum.NETWORK_ERROR.getErrDtlCode()));
            }
        });

        return data;
    }

}