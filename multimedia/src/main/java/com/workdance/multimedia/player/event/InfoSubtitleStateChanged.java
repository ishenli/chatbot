/*
 * Copyright (C) 2023 bytedance
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Create Date : 2023/6/30
 */

package com.workdance.multimedia.player.event;

import com.workdance.multimedia.player.PlayerEvent;
import com.workdance.core.event.Event;

public class InfoSubtitleStateChanged extends Event {

    public boolean enabled;

    public InfoSubtitleStateChanged() {
        super(PlayerEvent.Info.SUBTITLE_STATE_CHANGED);
    }

    public InfoSubtitleStateChanged init(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        enabled = false;
    }
}
