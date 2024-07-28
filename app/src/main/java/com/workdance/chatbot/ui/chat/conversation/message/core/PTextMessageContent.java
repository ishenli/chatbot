package com.workdance.chatbot.ui.chat.conversation.message.core;

@ContentTag(type = MessageContentType.ContentType_P_Text, flag = PersistFlag.Persist)
public class PTextMessageContent extends TextMessageContent {
    private String content;

    public PTextMessageContent() {
    }

    public PTextMessageContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public MessagePayload encode() {
        MessagePayload payload = super.encode();
        payload.searchableContent = content;
        return payload;
    }

}

