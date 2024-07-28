package com.workdance.chatbot.api;

import static com.workdance.chatbot.config.Constant.WEB_SERVICE_HOSTNAME;
import static com.workdance.chatbot.core.util.TimeUtils.getMsgFormatTime;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.workdance.chatbot.api.adapter.DateTypeAdapter;
import com.workdance.chatbot.api.dto.BaseResult;
import com.workdance.chatbot.api.dto.rep.BrainItemRep;
import com.workdance.chatbot.api.dto.rep.ChatDetailRep;
import com.workdance.chatbot.api.dto.rep.ChatHistoryRep;
import com.workdance.chatbot.api.dto.rep.ChatItemRep;
import com.workdance.chatbot.api.dto.rep.MessageItemRep;
import com.workdance.chatbot.api.dto.req.ChatHistoryReq;
import com.workdance.chatbot.api.dto.req.ChatReq;
import com.workdance.chatbot.core.enums.ErrorCodeEnum;
import com.workdance.chatbot.core.util.OperateResult;
import com.workdance.chatbot.core.util.StringUtil;
import com.workdance.chatbot.model.Assistant;
import com.workdance.chatbot.model.Brain;
import com.workdance.chatbot.model.Conversation;
import com.workdance.chatbot.model.Message;
import com.workdance.chatbot.model.UserInfo;
import com.workdance.chatbot.ui.chat.conversation.MessageVO;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageDirection;
import com.workdance.chatbot.ui.chat.conversation.message.core.MessageStatus;
import com.workdance.chatbot.ui.chat.conversation.message.core.TextMessageContent;
import com.workdance.chatbot.ui.main.home.conversationlist.ConversationListItemVO;

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
    // private static final String Base_URL = "http://10.0.2.2:8080";
    private static final String Base_URL = WEB_SERVICE_HOSTNAME;
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


    public static LiveData<List<ConversationListItemVO>> getConversationList(String userId) {
        MutableLiveData<List<ConversationListItemVO>> data = new MutableLiveData<>();
        ChatReq chatReq = new ChatReq();
        chatReq.setWorkId(userId);
        getChatService().listChat(chatReq).enqueue(new Callback<BaseResult<List<ChatItemRep>>>() {
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

                            // 头像先取聊天头像，再取大脑的头像
                            if (chatItem.getAvatar() == null) {
                                String brainLogo = chatItem.getBrains().get(chatItem.getBrains().size() - 1).getLogo();
                                if (StringUtil.isEmpty(brainLogo)) {
                                    chatListItemVO.avatar = "https://kimi.moonshot.cn/kimi-chat/assets/avatar/kimi_avatar_keep_light.png";
                                } else {
                                    chatListItemVO.avatar = brainLogo;
                                }
                            } else {
                                chatListItemVO.avatar = chatItem.getAvatar();
                            }
                            chatListItemVO.message = chatItem.getUserMessage();
                            chatListItemVO.time = chatItem.getUserMessage() == null ? getMsgFormatTime(chatItem.getChatGmtCreate()) : getMsgFormatTime(chatItem.getMessageGmtCreate());
                            if (!chatItem.getBrains().isEmpty()) {
                                Brain brain = chatItem.getBrains().get(chatItem.getBrains().size() - 1);
                                chatListItemVO.brainId = brain.getBrainId();
                            }
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


    /**
     * 根据chatId获取对话详情
     *
     * @param conversation
     * @return
     */
    public static LiveData<ChatDetailRep> getChatDetail(Conversation conversation) {
        MutableLiveData<ChatDetailRep> data = new MutableLiveData<>();
        getChatService().detail(conversation.getId()).enqueue(new Callback<BaseResult<ChatDetailRep>>() {
            @Override
            public void onResponse(Call<BaseResult<ChatDetailRep>> call, Response<BaseResult<ChatDetailRep>> response) {
                if (response.isSuccessful()) {
                    BaseResult<ChatDetailRep> result = response.body();
                    ChatDetailRep chatItems = result.getData();
                    if (chatItems != null) {
                        data.setValue(chatItems);
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

    public static LiveData<List<MessageItemRep>> getMessages(Conversation conversation) {
        MutableLiveData<List<MessageItemRep>> data = new MutableLiveData<>();
        ChatReq chatReq = new ChatReq();
        chatReq.setChatId(conversation.getId());
        getChatService().listMessage(chatReq).enqueue(new Callback<BaseResult<List<MessageItemRep>>>() {
            @Override
            public void onResponse(Call<BaseResult<List<MessageItemRep>>> call, Response<BaseResult<List<MessageItemRep>>> response) {
                if (response.isSuccessful()) {
                    List<MessageItemRep> chatItems = response.body().getData();
                    data.postValue(chatItems);
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
                            assistant.setBrainId(item.getBrainId());
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

    // todo: 核心逻辑放到 ViewModel 层
    public static LiveData<MessageVO> sendChatMessage(Message message) {
        ChatReq chatReq = new ChatReq();
        chatReq.setChatId(message.getConversation().getId());
        chatReq.setBrainId(message.toUsers[0]);
        TextMessageContent textMessageContent = (TextMessageContent) message.content;
        chatReq.setQuestion(textMessageContent.getContent());

        MutableLiveData<MessageVO> data = new MutableLiveData<>();
        getChatService().createChatHistory(chatReq).enqueue(new Callback<BaseResult<ChatHistoryRep>>() {

            @Override
            public void onResponse(Call<BaseResult<ChatHistoryRep>> call, Response<BaseResult<ChatHistoryRep>> response) {
                if (response.isSuccessful()) {
                    ChatHistoryRep chatHistoryRep = response.body().getData();
                    if (chatHistoryRep != null) {
                        TextMessageContent textMessageContent = new TextMessageContent(chatHistoryRep.getUserMessage());
                        Message msg = new Message();
                        msg.setMessageId(chatHistoryRep.getMessageId());
                        msg.setDirection(MessageDirection.Send);
                        msg.setStatus(MessageStatus.Sending);
                        msg.setAvatar(ChatApi.getDefaultUser().portrait);
                        msg.setContent(textMessageContent);
                        msg.setConversation(message.getConversation());
                        MessageVO messageVO = new MessageVO(msg);
                        data.setValue(messageVO);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResult<ChatHistoryRep>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });

        return data;
    }

    public static LiveData<ChatHistoryRep> modifyChatMessage(String messageId, String assistant) {
        ChatHistoryReq chatReq = new ChatHistoryReq();
        chatReq.setMessageId(messageId);
        chatReq.setAssistant(assistant);
        MutableLiveData<ChatHistoryRep> data = new MutableLiveData<>();
        getChatService().modifyChatHistory(messageId, chatReq).enqueue(new Callback<BaseResult<ChatHistoryRep>>() {
            @Override
            public void onResponse(Call<BaseResult<ChatHistoryRep>> call, Response<BaseResult<ChatHistoryRep>> response) {
                if (response.isSuccessful()) {
                    ChatHistoryRep chatHistoryRep = response.body().getData();
                    if (chatHistoryRep != null) {
                        data.setValue(chatHistoryRep);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResult<ChatHistoryRep>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(null);
            }
        });

        return data;
    }


    /**
     * 获取对象详情，根据 chatId
     *
     * @param conversation
     * @return
     */
    public static LiveData<Brain> getBrainById(String brainId) {
        MutableLiveData<Brain> data = new MutableLiveData<>();
        getChatService().brainDetail(brainId).enqueue(new Callback<BaseResult<BrainItemRep>>() {
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


    public static LiveData<UserInfo> getUserInfoById(String userIdOrBrainId) {
        MutableLiveData<UserInfo> data = new MutableLiveData<>();
        if (userIdOrBrainId.equals(ChatApi.getDefaultUser().uid)) {
            data.setValue(ChatApi.getDefaultUser());
        } else {
            getBrainById(userIdOrBrainId).observeForever(brain -> {
                UserInfo userInfo = new UserInfo();
                userInfo.displayName = brain.getName();
                userInfo.uid = brain.getBrainId();
                userInfo.name = brain.getDescription();
                userInfo.portrait = brain.getLogo();
                data.setValue(userInfo);
            });
        }
        return data;
    }


    public static LiveData<OperateResult<Boolean>> deleteChat(Conversation conversation) {
        MutableLiveData<OperateResult<Boolean>> data = new MutableLiveData<>();
        getChatService().deleteChat(conversation.getId()).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().isSuccess()) {
                        data.setValue(new OperateResult<>(true));
                    } else {
                        data.setValue(new OperateResult<>(false, ErrorCodeEnum.SYSTEM_ERROR.getErrDtlCode()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResult<String>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(new OperateResult<>(false, ErrorCodeEnum.NETWORK_ERROR.getErrDtlCode()));
            }
        });

        return data;
    }

    public static LiveData<OperateResult<Boolean>> modifyChat(ChatReq chatReq) {
        MutableLiveData<OperateResult<Boolean>> data = new MutableLiveData<>();
        getChatService().modifyChat(chatReq.getChatId(), chatReq).enqueue(new Callback<BaseResult<ChatDetailRep>>() {
            @Override
            public void onResponse(Call<BaseResult<ChatDetailRep>> call, Response<BaseResult<ChatDetailRep>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().isSuccess()) {
                        data.setValue(new OperateResult<>(true));
                    } else {
                        data.setValue(new OperateResult<>(false, ErrorCodeEnum.SYSTEM_ERROR.getErrDtlCode()));
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResult<ChatDetailRep>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable);
                data.setValue(new OperateResult<>(false, ErrorCodeEnum.NETWORK_ERROR.getErrDtlCode()));
            }
        });

        return data;
    }



    public static LiveData<ChatDetailRep> createChat(ChatReq chatReq) {
        MutableLiveData<ChatDetailRep> data = new MutableLiveData<>();
        String userId = ChatApi.getUserId();
        chatReq.setUserId(userId);
        getChatService().addChat(chatReq).enqueue(new Callback<BaseResult<ChatDetailRep>>() {
            @Override
            public void onResponse(Call<BaseResult<ChatDetailRep>> call, Response<BaseResult<ChatDetailRep>> response) {
                if (response.isSuccessful()) {
                    ChatDetailRep chatDetailRep = response.body().getData();
                    data.setValue(chatDetailRep);
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
}
