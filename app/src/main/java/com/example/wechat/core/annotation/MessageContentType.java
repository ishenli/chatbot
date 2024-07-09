/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.example.wechat.core.annotation;

import com.example.wechat.chat.conversation.message.MessageContent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageContentType {
    Class<? extends MessageContent>[] value();
}
