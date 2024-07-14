package com.example.wechat.repository.data;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.wechat.repository.entity.ConversationEntity;
import com.example.wechat.repository.entity.ConversationWithUserEntity;
import com.example.wechat.repository.entity.UserEntity;

import java.util.List;

public class UserWithConversation {
    @Embedded
    public UserEntity userEntity;

    @Relation(parentColumn = "uid", entityColumn = "conversation_id", associateBy = @Junction(ConversationWithUserEntity.class))
    public List<ConversationEntity> conversationEntityList;
}
