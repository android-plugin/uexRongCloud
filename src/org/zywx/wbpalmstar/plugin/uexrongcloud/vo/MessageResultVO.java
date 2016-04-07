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
import io.rong.imlib.model.Message;

/**
 * Created by ylt on 16/3/28.
 */
public class MessageResultVO implements Serializable {


    private static final long serialVersionUID = 558885233255779687L;

    private Conversation.ConversationType conversationType;
    private String targetId;
    private int messageId;
    private Message.MessageDirection messageDirection;
    private String senderUserId;
    private Message.ReceivedStatus receivedStatus;
    private Message.SentStatus sentStatus;
    private long receivedTime;
    private long sentTime;
    private String objectName;
    private MessageContentVO content;
    private String extra;
    private String UId;
    private int progress;
    private int resultCode;
    private String localId;

    public Conversation.ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType(Conversation.ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Message.MessageDirection getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(Message.MessageDirection messageDirection) {
        this.messageDirection = messageDirection;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public Message.ReceivedStatus getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(Message.ReceivedStatus receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public Message.SentStatus getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(Message.SentStatus sentStatus) {
        this.sentStatus = sentStatus;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public MessageContentVO getContent() {
        return content;
    }

    public void setContent(MessageContentVO content) {
        this.content = content;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }
}
