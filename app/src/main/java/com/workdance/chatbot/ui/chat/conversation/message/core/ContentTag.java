/*
 * Copyright (c) 2020 WildFireChat. All rights reserved.
 */

package com.workdance.chatbot.ui.chat.conversation.message.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ContentTag {
    int type() default 0;

    PersistFlag flag() default PersistFlag.No_Persist;
}
