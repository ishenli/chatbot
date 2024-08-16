package com.workdance.chatbot.remote;

import static com.workdance.chatbot.config.Constant.MEDIA_SERVICE_HOSTNAME;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.workdance.chatbot.remote.api.MediaService;
import com.workdance.chatbot.remote.dto.MediaResult;
import com.workdance.chatbot.remote.dto.rep.DramaRep;
import com.workdance.chatbot.remote.dto.req.DramaReq;
import com.workdance.chatbot.ui.multimedia.model.DramaInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaClient {
    private static final String TAG = "MediaClient";
    private static final String Base_URL = MEDIA_SERVICE_HOSTNAME;
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

    public static MediaService getService() {
        return getClient().create(MediaService.class);
    }

    public static LiveData<List<DramaInfo>> listDrama( int pageIndex, int pageSize) {
        MutableLiveData<List<DramaInfo>> data = new MutableLiveData<>();
        DramaReq dramaReq = new DramaReq();
        dramaReq.setOffset(pageIndex * pageSize);
        dramaReq.setPageSize(pageSize);
        getService().listDrama(dramaReq).enqueue(new Callback<MediaResult<List<DramaRep>>>() {
            @Override
            public void onResponse(Call<MediaResult<List<DramaRep>>> call, Response<MediaResult<List<DramaRep>>> response) {
                if (response.isSuccessful()) {
                    List<DramaRep> items = response.body().getResult();
                    if (items != null) {
                        List<DramaInfo> list = new ArrayList<>();
                        for (DramaRep item : items) {
                            DramaInfo vo = new DramaInfo();
                            vo.setDramaId(item.getDramaId());
                            vo.setDramaTitle(item.getDramaTitle());
                            vo.setDescription(item.getDescription());
                            vo.setAuthorId(item.getAuthorId());
                            vo.setCoverUrl(item.getCoverUrl());
                            vo.setLatestEpisodeNumber(item.getLatestEpisodeNumber());
                            vo.setTotalEpisodeNumber(item.getTotalEpisodeNumber());
                            list.add(vo);
                        }
                        data.setValue(list);
                    }

                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MediaResult<List<DramaRep>>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });
        return data;
    }

}