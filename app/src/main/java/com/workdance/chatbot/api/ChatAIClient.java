package com.workdance.chatbot.api;

import static com.workdance.chatbot.config.Constant.AI_SERVICE_HOSTNAME;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.workdance.chatbot.api.dto.StreamEvent;
import com.workdance.chatbot.api.dto.StreamEventEnum;
import com.workdance.chatbot.api.dto.req.ChatReq;
import com.workdance.chatbot.core.util.MarkdownFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatAIClient {
    private static final String TAG = "ChatAIClient";
   private static final String Base_URL = AI_SERVICE_HOSTNAME;
    private static Retrofit retrofit = null;

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ChatAIService getChatAIService() {
        return getClient().create(ChatAIService.class);
    }


    public static LiveData<StreamEvent> askOllama(ChatReq chatReq) {
        MutableLiveData<StreamEvent> mData = new MutableLiveData<>();
        getChatAIService().streamOllama(chatReq).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.submit(() -> {
                        MarkdownFormatter markdownFormatter = new MarkdownFormatter();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                        String line;
                        StreamEvent se = new StreamEvent();
                        try {
                            while ((line = reader.readLine()) != null) {
                                Log.d(TAG, "onResponse: " + reader.readLine());
                                markdownFormatter.formatMarkdown(line);
                                se.setStatus(StreamEventEnum.Doing);
                                se.setData(markdownFormatter.getOutput());
                                mData.postValue(se);
                            }
                            se.setStatus(StreamEventEnum.Done);
                            mData.postValue(se);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Log.e(TAG,"SSE connection failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.d(TAG, "onFailure: " + throwable.getMessage());
            }
        });
        return mData;
    }


    public static LiveData<StreamEvent> askQuestionStream(ChatReq chatReq) {
        MutableLiveData<StreamEvent> mData = new MutableLiveData<>();
        StringBuilder sb = new StringBuilder();
        getChatAIService().askQuestionStream(chatReq.getChatId(),chatReq).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.submit(() -> {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.body().byteStream()));
                        String line;
                        try {
                            while ((line = reader.readLine()) != null) {
                                Log.d(TAG, "onResponse: " + reader.readLine());
                                if (line.startsWith("data: ")) {
                                    String data = line.substring(6);
                                    if (data.equals("[DONE]")) {
                                        StreamEvent se = new StreamEvent();
                                        se.setStatus(StreamEventEnum.Done);
                                        se.setData(sb.toString());
                                        mData.postValue(se);
                                        break;
                                    }

                                    try {
                                        // 尝试解析 JSON
                                        JSONObject jsonObject = new JSONObject(data);
                                        if (jsonObject.has("assistant")) {
                                            String assistantContent = jsonObject.getString("assistant");
                                            sb.append(assistantContent);
                                        }
                                    } catch (JSONException e) {
                                        // 如果不是 JSON 格式，直接添加整行内容
                                        sb.append(data);
                                    }
                                    StreamEvent se = new StreamEvent();
                                    // 在UI线程上更新UI
                                    se.setStatus(StreamEventEnum.Doing);
                                    se.setData(sb.toString());
                                    mData.postValue(se);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // try {
                            //     reader.close();
                            // } catch (IOException e) {
                            //     e.printStackTrace();
                            // }
                        }
                    });
                } else {
                    Log.e(TAG,"SSE connection failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
            }
        });
        return mData;
    }
}
