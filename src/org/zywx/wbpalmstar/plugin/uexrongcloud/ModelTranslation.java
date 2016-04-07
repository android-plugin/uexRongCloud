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

package org.zywx.wbpalmstar.plugin.uexrongcloud;

import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.MessageContentVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.MessageResultVO;

import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.CommandMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by ylt on 16/3/30.
 */
public class ModelTranslation {

    public static MessageResultVO translateMessageVO(Message message) {
        return translateMessageVO(null,message);
    }

    public static MessageResultVO translateMessageVO(MessageResultVO resultVO,Message message) {
        if (resultVO==null){
            resultVO=new MessageResultVO();
        }
        if (message==null){
            return resultVO;
        }
        resultVO.setMessageId(message.getMessageId());
        MessageContentVO contentVO=new MessageContentVO();
        translateMessageContent(contentVO,message.getContent());
        resultVO.setContent(contentVO);
        resultVO.setExtra(message.getExtra());
        resultVO.setConversationType(message.getConversationType());
        resultVO.setMessageDirection(message.getMessageDirection());
        resultVO.setTargetId(message.getTargetId());
        resultVO.setObjectName(message.getObjectName());
        resultVO.setSentStatus(message.getSentStatus());
        resultVO.setSenderUserId(message.getSenderUserId());
        resultVO.setMessageId(message.getMessageId());
        resultVO.setSentTime(message.getSentTime());
        resultVO.setReceivedTime(message.getReceivedTime());
        return resultVO;
    }

    /**
     * 把融云的MessageContent 转换成插件的 MessageContentVO
     * @param contentVO
     * @param content
     */
    public static void translateMessageContent(MessageContentVO contentVO, MessageContent content){
        if (content instanceof TextMessage){
            contentVO.setText(((TextMessage) content).getContent());
            contentVO.setExtra(((TextMessage) content).getExtra());
        }else if (content instanceof VoiceMessage){
            contentVO.setVoicePath(((VoiceMessage) content).getUri()==null?null:((VoiceMessage) content).getUri().getPath());
            contentVO.setDuration(((VoiceMessage) content).getDuration());
            contentVO.setExtra(((VoiceMessage) content).getExtra());
        }else if (content instanceof ImageMessage){
            ImageMessage imageMessage= (ImageMessage) content;
            contentVO.setImgPath(imageMessage.getLocalUri()!=null?imageMessage.getLocalUri().getPath():
                    (imageMessage.getRemoteUri()!=null?imageMessage.getRemoteUri().getPath():""));
            contentVO.setThumbPath(imageMessage.getThumUri()!=null?imageMessage.getThumUri().getPath():"");
            contentVO.setExtra(imageMessage.getExtra());
        }else if (content instanceof RichContentMessage){
            RichContentMessage richMessage= (RichContentMessage) content;
            contentVO.setTitle(richMessage.getTitle());
            contentVO.setDescription(richMessage.getContent());
            contentVO.setExtra(richMessage.getExtra());
            contentVO.setImgPath(richMessage.getImgUrl());
            contentVO.setUrl(richMessage.getUrl());
            contentVO.setExtra(richMessage.getExtra());
        }else if (content instanceof LocationMessage){
            LocationMessage locationMessage= (LocationMessage) content;
            contentVO.setLatitude(String.valueOf(locationMessage.getLat()));
            contentVO.setLongitude(String.valueOf(locationMessage.getLng()));
            contentVO.setPoi(locationMessage.getPoi());
            contentVO.setImgPath(locationMessage.getImgUri()!=null?locationMessage.getImgUri().getPath():null);
            contentVO.setExtra(locationMessage.getExtra());
        }else if (content instanceof CommandMessage){
            CommandMessage commandMessage= (CommandMessage) content;
            contentVO.setName(commandMessage.getName());
            contentVO.setData(commandMessage.getData());
        }
    }

}
