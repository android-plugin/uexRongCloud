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
 * Created by ylt on 16/3/26.
 */
public class SendMessageVO implements Serializable {

    private static final long serialVersionUID = -8555246723334670290L;

    /**
     * 消息类型  "RC:TxtMsg"：文字消息 "RC:VcMsg"：语音消息
     * "RC:ImgMsg"：图片消息 "RC:ImgTextMsg"：图文消息 "RC:LBSMsg"：位置消息 "RC:CmdNtf"：命令消息
     */
    private String objectName;

    /**
     * 会话类型
     *
     * PRIVATE // 单聊
     * DISCUSSION // 讨论组
     * GROUP // 群组
     * CHATROOM // 聊天室
     * CUSTOMER_SERVICE // 客服
     * SYSTEM // 系统
     */
    private Conversation.ConversationType conversationType;

    /**
     * 消息的接收方 Id。根据不同的 conversationType，可能是用户Id、讨论组Id、群组Id或聊天室Id等
     */
    private String targetId;

    /**
     * 消息的唯一id，用于标识接收发送回调的处理
     */
    private String localId;

    /**
     * 消息的附加字段
     */
    private String extra;


    private String text;

    private String voicePath;

    private int duration;

    private String imgPath;

    private String title;

    private String description;

    private String imgUrl;

    private String latitude;
    private String longitude;
    private String poi;
    private String name;
    private String data;

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

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

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
