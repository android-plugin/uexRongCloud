/*
 * Copyright (c) 2016.  The AppCan Open Source Project.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package org.zywx.wbpalmstar.plugin.uexrongcloud.vo;

import java.io.Serializable;

import io.rong.imlib.model.Conversation;

/**
 * Created by ylt on 16/3/30.
 */
public class ConversationInputVO implements Serializable {

    private static final long serialVersionUID = 6574184922012738358L;

    private Conversation.ConversationType  conversationType;

    private Conversation.ConversationType[]  conversationTypes;

    private String targetId;

    private boolean isTop;

    private int status;

    private int count;

    private int oldestMessageId;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Conversation.ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(Conversation.ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOldestMessageId() {
        return oldestMessageId;
    }

    public void setOldestMessageId(int oldestMessageId) {
        this.oldestMessageId = oldestMessageId;
    }

    public Conversation.ConversationType[] getConversationTypes() {
        return conversationTypes;
    }

    public void setConversationTypes(Conversation.ConversationType[] conversationTypes) {
        this.conversationTypes = conversationTypes;
    }
}
