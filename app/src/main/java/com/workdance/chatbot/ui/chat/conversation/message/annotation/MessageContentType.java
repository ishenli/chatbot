/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.ui.chat.conversation.message.annotation;

import com.workdance.chatbot.model.MessageContent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageContentType {
    Class<? extends MessageContent>[] value();
}
