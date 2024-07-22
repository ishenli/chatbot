package com.workdance.chatbot.api;

import static com.workdance.chatbot.core.util.TimeUtils.getMsgFormatTime;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.workdance.chatbot.api.adapter.DateTypeAdapter;
import com.workdance.chatbot.api.dto.BaseResult;
import com.workdance.chatbot.api.dto.BrainItemRep;
import com.workdance.chatbot.api.dto.ChatDetailRep;
import com.workdance.chatbot.api.dto.ChatItemRep;
import com.workdance.chatbot.api.dto.ChatReq;
import com.workdance.chatbot.api.dto.MessageItemRep;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageDirection;
import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;
import com.workdance.chatbot.ui.chat.conversationlist.ConversationListItemVO;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.ConversationInfo;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.ui.chat.conversation.MessageVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatClient {
    private static final String TAG = "ChatClient";
//    private static final String Base_URL = "http://10.0.2.2:8080";
    private static final String Base_URL = "http://debug1721618944350.test.alipay.net:8080";
    private static Retrofit retrofit = null;


    static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ChatService getChatService() {
        return getClient().create(ChatService.class);
    }


    public static LiveData<List<ConversationListItemVO>> getConversationList() {
        MutableLiveData<List<ConversationListItemVO>> data = new MutableLiveData<>();
        ChatReq chatReq = new ChatReq();
        chatReq.setWorkId("105766");
        getChatService().listChatItems(chatReq).enqueue(new Callback<BaseResult<List<ChatItemRep>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<ChatItemRep>>> call, Response<BaseResult<List<ChatItemRep>>> response) {
                if (response.isSuccessful()) {
                    BaseResult<List<ChatItemRep>> result = response.body();
                    List<ChatItemRep> chatItems = result.getData();
                    if (chatItems != null) {
                        List<ConversationListItemVO> conversations = new ArrayList<>();
                        for (ChatItemRep chatItem : chatItems) {
                            ConversationListItemVO chatListItemVO = new ConversationListItemVO();
                            chatListItemVO.id = chatItem.getChatId();
                            chatListItemVO.name = chatItem.getChatName();
                            chatListItemVO.avatar = chatItem.getAvatar() == null ? "https://kimi.moonshot.cn/kimi-chat/assets/avatar/kimi_avatar_keep_light.png" : chatItem.getAvatar();
                            chatListItemVO.message = chatItem.getUserMessage();
                            chatListItemVO.time = chatItem.getUserMessage() == null ? getMsgFormatTime(chatItem.getChatGmtCreate()) : getMsgFormatTime(chatItem.getMessageGmtCreate());
                            conversations.add(chatListItemVO);
                        }
                        data.setValue(conversations);
                    }

                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResult<List<ChatItemRep>>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });
        return data;
    }


    public static LiveData<ConversationInfo> getConversation(Conversation conversation) {
        MutableLiveData<ConversationInfo> data = new MutableLiveData<>();
        getChatService().detail(conversation.getTarget()).enqueue(new Callback<BaseResult<ChatDetailRep>>() {
            @Override
            public void onResponse(Call<BaseResult<ChatDetailRep>> call, Response<BaseResult<ChatDetailRep>> response) {
                if (response.isSuccessful()) {
                    BaseResult<ChatDetailRep> result = response.body();
                    ChatDetailRep chatItems = result.getData();
                    if (chatItems != null) {
                        ConversationInfo conversationInfo = new ConversationInfo();
                        data.setValue(conversationInfo);
                    }

                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResult<ChatDetailRep>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });
        return data;
    }

    public static LiveData<List<MessageVO>> getMessages(Conversation conversation) {
        MutableLiveData<List<MessageVO>> data = new MutableLiveData<>();
        ChatReq chatReq = new ChatReq();
        chatReq.setChatId(conversation.getTarget());
        getChatService().listMessage(chatReq).enqueue(new Callback<BaseResult<List<MessageItemRep>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<MessageItemRep>>> call, Response<BaseResult<List<MessageItemRep>>> response) {
                if (response.isSuccessful()) {
                    List<MessageItemRep> chatItems = response.body().getData();
                    if (chatItems != null) {
                        List<MessageVO> list = new ArrayList<>();
                        for (MessageItemRep item : chatItems) {

                            Conversation conversation1 = new Conversation(Conversation.ConversationType.Single, conversation.getTarget());

                            if (item.getUserMessage() != null) {
                                Message message = new Message();
                                message.conversation = conversation1;
                                message.messageId = item.getMessageId();
                                message.content = new TextMessageContent(item.getUserMessage());
                                message.direction = MessageDirection.Send;
                                MessageVO messageVO = new MessageVO(message);
                                list.add(messageVO);
                            }

                            if (item.getAssistant() != null) {
                                Message message = new Message();
                                message.conversation = conversation1;
                                message.messageId = item.getMessageId();
                                message.direction = MessageDirection.Receive;
                                message.content = new TextMessageContent(item.getAssistant());
                                MessageVO messageVO = new MessageVO(message);
                                list.add(messageVO);
                            }
                        }
                        data.setValue(list);
                    }

                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseResult<List<MessageItemRep>>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });
        return data;
    }

    public static LiveData<List<Assistant>> getAllAssistant(ChatReq chatReq) {
        MutableLiveData<List<Assistant>> data = new MutableLiveData<>();
        getChatService().listBrain(chatReq).enqueue(new Callback<BaseResult<List<BrainItemRep>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<BrainItemRep>>> call, Response<BaseResult<List<BrainItemRep>>> response) {
                if (response.isSuccessful()) {
                    List<BrainItemRep> items = response.body().getData();
                    if (items != null) {
                        List<Assistant> list = new ArrayList<>();
                        for (BrainItemRep item : items) {
                            Assistant assistant = new Assistant();
                            assistant.setName(item.getName());
                            assistant.setLogo(item.getLogo());
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
}
