package com.workdance.chatbot.remote;

import static com.workdance.chatbot.config.Constant.WEB_SERVICE_HOSTNAME;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.workdance.chatbot.config.Constant;
import com.workdance.chatbot.remote.api.AssistantService;
import com.workdance.chatbot.remote.dto.BaseResult;
import com.workdance.chatbot.remote.dto.rep.BrainItemRep;
import com.workdance.chatbot.remote.dto.req.BrainReq;
import com.workdance.chatbot.remote.dto.req.ChatReq;
import com.workdance.core.enums.ErrorCodeEnum;
import com.workdance.core.dto.OperateResult;
import com.workdance.core.util.FileUtils;
import com.workdance.chatbot.model.AIModel;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.Brain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AssistantClient {
    private static final String TAG = "BrainClient";
    private static final String Base_URL = WEB_SERVICE_HOSTNAME;
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

    public static AssistantService getBrainService() {
        return getClient().create(AssistantService.class);
    }

    public static LiveData<List<Assistant>> getAllAssistant(ChatReq chatReq) {
        MutableLiveData<List<Assistant>> data = new MutableLiveData<>();
        getBrainService().listBrain(chatReq).enqueue(new Callback<BaseResult<List<BrainItemRep>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<BrainItemRep>>> call, Response<BaseResult<List<BrainItemRep>>> response) {
                if (response.isSuccessful()) {
                    List<BrainItemRep> items = response.body().getData();
                    if (items != null) {
                        List<Assistant> list = new ArrayList<>();
                        for (BrainItemRep item : items) {
                            Assistant assistant = new Assistant();
                            assistant.setBrainId(item.getBrainId());
                            assistant.setName(item.getName());
                            assistant.setLogo(FileUtils.getStaticFilePath(Constant.STATIC_SERVICE_HOSTNAME, item.getLogo()));
                            assistant.setModel(item.getModel());
                            assistant.setDescription(item.getDescription());
                            list.add(assistant);
                        }
                        data.setValue(list);
                    }

                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResult<List<BrainItemRep>>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });
        return data;
    }

    /**
     * 获取对象详情，根据 chatId
     */
    public static LiveData<Brain> getAssistantById(String brainId) {
        MutableLiveData<Brain> data = new MutableLiveData<>();
        getBrainService().brainDetail(brainId).enqueue(new Callback<BaseResult<BrainItemRep>>() {
            @Override
            public void onResponse(Call<BaseResult<BrainItemRep>> call, Response<BaseResult<BrainItemRep>> response) {
                if (response.isSuccessful()) {
                    BaseResult<BrainItemRep> result = response.body();
                    BrainItemRep item = result.getData();
                    if (item != null) {
                        Brain brain = new Brain();
                        brain.setName(item.getName());
                        brain.setDescription(item.getDescription());
                        brain.setLogo(item.getLogo());
                        brain.setModel(item.getModel());
                        brain.setBrainId(item.getBrainId());
                        brain.setBrainType(item.getBrainType());
                        brain.setUserId(item.getUserId());
                        data.setValue(brain);
                    }

                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResult<BrainItemRep>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });
        return data;
    }


    public static LiveData<List<AIModel>> getAssistantModel() {
        MutableLiveData<List<AIModel>> data = new MutableLiveData<>();
        // 先写死
        data.setValue(AIModel.getAIModels());
        return data;
    }

    public static LiveData<OperateResult<Boolean>> addAssistant(Assistant assistant) {
        MutableLiveData<OperateResult<Boolean>> data = new MutableLiveData<>();
        // 先写死
        BrainReq brainReq = new BrainReq();
        brainReq.setUserId(ChatApi.getUserId());
        brainReq.setName(assistant.getName());
        brainReq.setDescription(assistant.getDescription());
        brainReq.setModel(assistant.getModel());
        brainReq.setLogo(assistant.getLogo());
        brainReq.setBrainType(assistant.getBrainType());
        getBrainService().addBrain(brainReq).enqueue(new Callback<BaseResult<BrainItemRep>>() {
            @Override
            public void onResponse(Call<BaseResult<BrainItemRep>> call, Response<BaseResult<BrainItemRep>> response) {
                if (response.isSuccessful()) {
                    BaseResult<BrainItemRep> result = response.body();
                    if (result.isSuccess()) {
                        data.setValue(new OperateResult<>(true));
                    } else {
                        data.setValue(new OperateResult<>(false, ErrorCodeEnum.SYSTEM_ERROR.getErrDtlCode()));
                    }
                } else {
                    data.setValue(new OperateResult<>(false, ErrorCodeEnum.SYSTEM_ERROR.getErrDtlCode()));
                }
            }
            @Override
            public void onFailure(Call<BaseResult<BrainItemRep>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(new OperateResult<>(false, ErrorCodeEnum.SYSTEM_ERROR.getErrDtlCode()));
            }
        });

        return data;
    }

}