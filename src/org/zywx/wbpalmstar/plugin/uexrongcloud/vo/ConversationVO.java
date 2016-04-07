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
 * Created by ylt on 16/3/30.
 */
public class ConversationVO implements Serializable {

    private static final long serialVersionUID = 8937994211477552334L;

    private Conversation.ConversationType conversationType;
    private String targetId;
    private String conversationTitle;
    private String portraitUrl;
    private int unreadMessageCount;
    private boolean isTop;
    private String receivedStatus;
    private Message.SentStatus sentStatus;
    private long receivedTime;
    private long sentTime;
    private String objectName;
    private String senderUserId;
    private String senderUserName;
    private int latestMessageId;
    private MessageContentVO latestMessage;
    private String draft;
    private Conversation.ConversationNotificationStatus notificationStatus;
    private int resultCode;
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

    public String getConversationTitle() {
        return conversationTitle;
    }

    public void setConversationTitle(String conversationTitle) {
        this.conversationTitle = conversationTitle;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public String getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(String receivedStatus) {
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

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public int getLatestMessageId() {
        return latestMessageId;
    }

    public void setLatestMessageId(int latestMessageId) {
        this.latestMessageId = latestMessageId;
    }

    public MessageContentVO getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(MessageContentVO latestMessage) {
        this.latestMessage = latestMessage;
    }

    public String getDraft() {
        return draft;
    }

    public void setDraft(String draft) {
        this.draft = draft;
    }

    public Conversation.ConversationNotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Conversation.ConversationNotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
