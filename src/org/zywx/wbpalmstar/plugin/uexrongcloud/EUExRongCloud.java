package org.zywx.wbpalmstar.plugin.uexrongcloud;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BDebug;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.ClearConversationsVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.ConnectResultVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.ConnectVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.ConversationInputVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.ConversationNotificationStatusVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.ConversationVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.DeleteMessagesResultVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.DeleteMessagesVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.GetConversationListResultVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.MessageResultVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.OnMessageReceivedVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.SendMessageVO;
import org.zywx.wbpalmstar.plugin.uexrongcloud.vo.SetMessageReceivedStatusVO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.CommandMessage;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

public class EUExRongCloud extends EUExBase {

    public static final String RC_TXT_MSG = "RC:TxtMsg";
    public static final String RC_VC_MSG = "RC:VcMsg";
    public static final String RC_IMG_MSG = "RC:ImgMsg";
    public static final String RC_IMG_TEXT_MSG = "RC:ImgTextMsg";
    public static final String RC_LBS_MSG = "RC:LBSMsg";
    public static final String RC_CMD_NTF = "RC:CmdMsg";

    public static final int RESULT_CODE_OK = 1;
    public static final int RESULT_CODE_PREPARE = 0;
    public static final int RESULT_CODE_FAILED = 2;
    public static final int RESULT_CODE_PROGRESS = 3;

    private RongIMClient mRongIMClient;

    public EUExRongCloud(Context context, EBrowserView eBrowserView) {
        super(context, eBrowserView);
        registerListeners();
    }

    @Override
    protected boolean clean() {
        return false;
    }

    private void registerListeners() {
        RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
    }

    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

        /**
         * 收到消息的处理。
         *
         * @param message 收到的消息实体。
         * @param left    剩余未拉取消息数目。
         * @return
         */
        @Override
        public boolean onReceived(io.rong.imlib.model.Message message, int left) {
            //开发者根据自己需求自行处理

            final OnMessageReceivedVO onMessageReceivedVO = new OnMessageReceivedVO();
            onMessageReceivedVO.setLeft(left);
            onMessageReceivedVO.setMessage(ModelTranslation.translateMessageVO(message));
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callBackJsObject(JsConst.ON_MESSAGE_RECEIVED, DataHelper.gson.toJsonTree(onMessageReceivedVO));
                }
            });
            return false;
        }


    }

    @Override
    public void onHandleMessage(Message message) {
        if (message == null) {
            return;
        }
        Bundle bundle = message.getData();
        switch (message.what) {

            default:
                super.onHandleMessage(message);
        }
    }

    public static void onApplicationCreate(Context context) {

        /**
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (context.getApplicationInfo().packageName.equals(getCurProcessName(context.getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(context.getApplicationContext()))) {
            RongIMClient.init(context);
        }
    }

    public void init(String[] params) {
        String json = params[0];
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("result", true);
        } catch (JSONException e) {
        }
        callBackJsObject(JsConst.CALLBACK_INIT, jsonResult);
    }

    public void connect(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        ConnectVO connectVO = DataHelper.gson.fromJson(json, ConnectVO.class);
        final ConnectResultVO resultVO = new ConnectResultVO();
        if (mContext.getApplicationInfo().packageName.equals(
                getCurProcessName(mContext.getApplicationContext()))) {

            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            mRongIMClient = RongIMClient.connect(connectVO.getToken(), new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {
                    resultVO.setResultCode(-1);
                    cbConnect(resultVO);
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    resultVO.setUserId(userid);
                    resultVO.setResultCode(0);
                    cbConnect(resultVO);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    resultVO.setResultCode(errorCode.getValue());
                    cbConnect(resultVO);
                }
            });
        }

    }

    private void cbConnect(ConnectResultVO resultVO) {
        callBackJsObject(JsConst.CALLBACK_CONNECT, DataHelper.gson.toJsonTree(resultVO));
    }

    public void sendMessage(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        if (mRongIMClient == null) {
            errorCallback(0, 0, "not connect!");
            return;
        }
        String json = params[0];
        SendMessageVO sendMessageVO = DataHelper.gson.fromJson(json, SendMessageVO.class);
        if (sendMessageVO.getObjectName().equals(RC_TXT_MSG)) {
            sendTextMessage(sendMessageVO);

        } else if (sendMessageVO.getObjectName().equals(RC_IMG_MSG)) {
            sendImgMessage(sendMessageVO);

        } else if (sendMessageVO.getObjectName().equals(RC_VC_MSG)) {
            sendVoiceMessage(sendMessageVO);

        } else if (sendMessageVO.getObjectName().equals(RC_IMG_TEXT_MSG)) {
            sendImgTextMessage(sendMessageVO);

        } else if (sendMessageVO.getObjectName().equals(RC_LBS_MSG)) {
            sendLBSMessage(sendMessageVO);

        } else if (sendMessageVO.getObjectName().equals(RC_CMD_NTF)) {
            sendCmdMessage(sendMessageVO);

        }
    }

    private void sendCmdMessage(final SendMessageVO sendMessageVO) {
        final MessageResultVO resultVO = new MessageResultVO();
        CommandMessage commandMessage = CommandMessage.obtain(sendMessageVO.getName(), sendMessageVO.getData());
        String localId=sendMessageVO.getLocalId();
        resultVO.setLocalId(localId);
        mRongIMClient.sendMessage(sendMessageVO.getConversationType(), sendMessageVO.getTargetId(),
                commandMessage, "", "",
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onSuccess(Integer id) {
                        resultVO.setResultCode(RESULT_CODE_OK);
                        resultVO.setMessageId(id);
                        cbSendMessage(resultVO);
                    }
                },
                new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        resultVO.setResultCode(RESULT_CODE_PREPARE);
                        ModelTranslation.translateMessageVO(resultVO, message);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }
                });
    }

    private void sendLBSMessage(final SendMessageVO sendMessageVO) {
        final MessageResultVO resultVO = new MessageResultVO();
        String localId=sendMessageVO.getLocalId();
        resultVO.setLocalId(localId);
        String imagePath = BUtility.getRealPathWithCopyRes(mBrwView, sendMessageVO.getImgPath());
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            BDebug.e("file not exist:", imagePath);
            resultVO.setResultCode(RESULT_CODE_FAILED);
        }
        Uri imageUri = Uri.fromFile(imageFile);
        LocationMessage locationMessage = LocationMessage.obtain(Double.valueOf(sendMessageVO.getLatitude()), Double.valueOf
                (sendMessageVO.getLongitude()), sendMessageVO.getPoi(), imageUri);
        if (!TextUtils.isEmpty(sendMessageVO.getExtra())) {
            locationMessage.setExtra(sendMessageVO.getExtra());
        }
        mRongIMClient.sendMessage(sendMessageVO.getConversationType(), sendMessageVO.getTargetId(),
                locationMessage, "", "",
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onSuccess(Integer id) {
                        resultVO.setResultCode(RESULT_CODE_OK);
                        resultVO.setMessageId(id);
                        cbSendMessage(resultVO);
                    }
                },
                new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        resultVO.setResultCode(RESULT_CODE_PREPARE);
                        ModelTranslation.translateMessageVO(resultVO, message);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }
                });
    }

    private void sendImgTextMessage(final SendMessageVO sendMessageVO) {
        final MessageResultVO resultVO = new MessageResultVO();
        String localId=sendMessageVO.getLocalId();
        resultVO.setLocalId(localId);
        RichContentMessage richContentMessage = RichContentMessage.obtain(sendMessageVO.getTitle(), sendMessageVO.getDescription(), sendMessageVO
                .getImgPath());
        if (!TextUtils.isEmpty(sendMessageVO.getExtra())) {
            richContentMessage.setExtra(sendMessageVO.getExtra());
        }
        mRongIMClient.sendMessage(sendMessageVO.getConversationType(),
                sendMessageVO.getTargetId(),
                richContentMessage, "", "",
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onSuccess(Integer id) {
                        resultVO.setResultCode(RESULT_CODE_OK);
                        resultVO.setMessageId(id);
                        cbSendMessage(resultVO);
                    }
                },
                new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        resultVO.setResultCode(RESULT_CODE_PREPARE);
                        ModelTranslation.translateMessageVO(resultVO, message);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }
                }
        );

    }

    private void sendVoiceMessage(final SendMessageVO sendMessageVO) {
        final MessageResultVO resultVO = new MessageResultVO();
        String localId=sendMessageVO.getLocalId();
        resultVO.setLocalId(localId);
        String voicePath = BUtility.getRealPathWithCopyRes(mBrwView, sendMessageVO.getVoicePath());
        File voiceFile = new File(voicePath);
        if (!voiceFile.exists()) {
            BDebug.e("file not exist:", voicePath);
            resultVO.setResultCode(RESULT_CODE_FAILED);
        }
        Uri voiceUri = Uri.fromFile(voiceFile);
        VoiceMessage voiceMessage = VoiceMessage.obtain(voiceUri, sendMessageVO.getDuration());
        if (!TextUtils.isEmpty(sendMessageVO.getExtra())) {
            voiceMessage.setExtra(sendMessageVO.getExtra());
        }
        mRongIMClient.sendMessage(sendMessageVO.getConversationType(),
                sendMessageVO.getTargetId(), voiceMessage, "", "",
                new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onSuccess(Integer id) {
                        resultVO.setResultCode(RESULT_CODE_OK);
                        resultVO.setMessageId(id);
                        cbSendMessage(resultVO);
                    }
                },
                new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        resultVO.setResultCode(RESULT_CODE_PREPARE);
                        ModelTranslation.translateMessageVO(resultVO, message);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }
                }
        );

    }

    private void sendImgMessage(final SendMessageVO sendMessageVO) {
        final MessageResultVO resultVO = new MessageResultVO();
        String localId=sendMessageVO.getLocalId();
        resultVO.setLocalId(localId);
        String imagePath = BUtility.getRealPathWithCopyRes(mBrwView, sendMessageVO.getImgPath());
        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            BDebug.e("file not exist:", imagePath);
            resultVO.setResultCode(RESULT_CODE_FAILED);
        }
        Uri imageUri = Uri.fromFile(imageFile);
        ImageMessage imageMessage=ImageMessage.obtain(imageUri, imageUri);
        if (!TextUtils.isEmpty(sendMessageVO.getExtra())) {
            imageMessage.setExtra(sendMessageVO.getExtra());
        }
        mRongIMClient.sendMessage(sendMessageVO.getConversationType(),
                sendMessageVO.getTargetId(),imageMessage
                , "", "",
                new RongIMClient.SendImageMessageCallback() {
                    @Override
                    public void onAttached(io.rong.imlib.model.Message message) {

                    }

                    @Override
                    public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        resultVO.setResultCode(RESULT_CODE_OK);
                        resultVO.setMessageId(message.getMessageId());
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onProgress(io.rong.imlib.model.Message message, int i) {
                        resultVO.setResultCode(RESULT_CODE_PROGRESS);
                        resultVO.setMessageId(message.getMessageId());
                        resultVO.setProgress(i);
                        cbSendMessage(resultVO);
                    }

                }
                , new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        resultVO.setResultCode(RESULT_CODE_PREPARE);
                        ModelTranslation.translateMessageVO(resultVO, message);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }
                }
        );

    }

    private void sendTextMessage(final SendMessageVO sendMessageVO) {
        final MessageResultVO resultVO = new MessageResultVO();
        String localId=sendMessageVO.getLocalId();
        resultVO.setLocalId(localId);
        TextMessage textMessage=TextMessage.obtain(sendMessageVO.getText());
        if (!TextUtils.isEmpty(sendMessageVO.getExtra())) {
            textMessage.setExtra(sendMessageVO.getExtra());
        }
        mRongIMClient.sendMessage(sendMessageVO
                        .getConversationType(),
                sendMessageVO.getTargetId(),
                textMessage, "", "",
                new RongIMClient.SendMessageCallback() {

                    @Override
                    public void onSuccess(Integer id) {
                        resultVO.setResultCode(RESULT_CODE_OK);
                        resultVO.setMessageId(id);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onError(Integer id, RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }

                },
                new RongIMClient.ResultCallback<io.rong.imlib.model.Message>() {
                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                        resultVO.setResultCode(RESULT_CODE_PREPARE);
                        ModelTranslation.translateMessageVO(resultVO, message);
                        cbSendMessage(resultVO);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        resultVO.setResultCode(RESULT_CODE_FAILED);
                        cbSendMessage(resultVO);
                    }
                }
        );
    }


    private void cbSendMessage(MessageResultVO resultVO) {
        callBackJsObject(JsConst.CALLBACK_SEND_MESSAGE, DataHelper.gson.toJsonTree(resultVO));

    }

    public String getConversationList(String[] params) {
        GetConversationListResultVO resultVO = new GetConversationListResultVO();
        if (mRongIMClient == null) {
            resultVO.setResultCode(RESULT_CODE_FAILED);
        } else {
            resultVO.setResultCode(0);
            List<Conversation> conversations=mRongIMClient.getConversationList();
            if (conversations!=null){
                List<ConversationVO> conversationVOs=new ArrayList<ConversationVO>();
                for (Conversation conversation:conversations) {
                    if (conversation!=null){
                        conversationVOs.add(ModelTranslation.translateConversation(conversation));
                    }
                }
                resultVO.setConversations(conversationVOs);
            }
        }
        return DataHelper.gson.toJson(resultVO);
    }

    public String getConversation(String[] params) {
        ConversationVO conversationVO=new ConversationVO();
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return null;
        }
        String json = params[0];
        ConversationInputVO inputVO = DataHelper.gson.fromJson(json, ConversationInputVO.class);
        Conversation conversation = mRongIMClient.getConversation(inputVO.getConversationType(), inputVO.getTargetId());
        if (conversation!=null) {
            ModelTranslation.translateConversation(conversationVO,conversation);
            conversationVO.setResultCode(0);
        }else{
            conversationVO.setResultCode(2);
        }
        return DataHelper.gson.toJson(conversationVO);
    }

    public void removeConversation(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        ConversationInputVO inputVO = DataHelper.gson.fromJson(json, ConversationInputVO.class);
        mRongIMClient.removeConversation(inputVO.getConversationType(), inputVO.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                callBackResultCode(JsConst.CALLBACK_REMOVE_CONVERSATION, 0);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                callBackResultCode(JsConst.CALLBACK_REMOVE_CONVERSATION, RESULT_CODE_FAILED);
            }
        });

    }

    private void callBackResultCode(String methodName, int resultCode) {
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("resultCode", resultCode);
        } catch (JSONException e) {
        }
        callBackJsObject(methodName, jsonResult);
    }

    public void clearConversations(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        ClearConversationsVO inputVO = DataHelper.gson.fromJson(json, ClearConversationsVO.class);
        mRongIMClient.clearConversations(new RongIMClient.ResultCallback() {
            @Override
            public void onSuccess(Object o) {
                callBackResultCode(JsConst.CALLBACK_CLEAR_CONVERSATIONS, 0);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                callBackResultCode(JsConst.CALLBACK_CLEAR_CONVERSATIONS, 2);
            }
        }, inputVO.getConversationTypes());
    }

    public void setConversationToTop(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        ConversationInputVO inputVO = DataHelper.gson.fromJson(json, ConversationInputVO.class);
        mRongIMClient.setConversationToTop(inputVO.getConversationType(), inputVO.getTargetId(),
                inputVO.isTop(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        callBackResultCode(JsConst.CALLBACK_SET_CONVERSATION_TO_TOP, 0);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        callBackResultCode(JsConst.CALLBACK_SET_CONVERSATION_TO_TOP, 2);
                    }
                });
    }

    public void getConversationNotificationStatus(String[] params) {
        final ConversationNotificationStatusVO statusVO = new ConversationNotificationStatusVO();
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }

        String json = params[0];
        ConversationInputVO inputVO = DataHelper.gson.fromJson(json, ConversationInputVO.class);
        mRongIMClient.getConversationNotificationStatus(inputVO.getConversationType(), inputVO.getTargetId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            @Override
            public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                statusVO.setResultCode(0);
                statusVO.setStatus(conversationNotificationStatus.getValue());
                callBackJsObject(JsConst.CALLBACK_GET_CONVERSATION_NOTIFICATION_STATUS, DataHelper.gson.toJsonTree(statusVO));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                statusVO.setResultCode(2);
                callBackJsObject(JsConst.CALLBACK_GET_CONVERSATION_NOTIFICATION_STATUS, DataHelper.gson.toJsonTree(statusVO));
            }
        });
    }

    public void setConversationNotificationStatus(String[] params) {
        final ConversationNotificationStatusVO statusVO = new ConversationNotificationStatusVO();
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        ConversationInputVO inputVO=DataHelper.gson.fromJson(json,ConversationInputVO.class);
        mRongIMClient.setConversationNotificationStatus(inputVO.getConversationType(), inputVO.getTargetId(),
                Conversation.ConversationNotificationStatus.setValue(inputVO.getStatus()), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        statusVO.setResultCode(0);
                        statusVO.setStatus(conversationNotificationStatus.getValue());
                        callBackJsObject(JsConst.CALLBACK_SET_CONVERSATION_NOTIFICATION_STATUS, DataHelper.gson.toJsonTree
                                (statusVO));
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        statusVO.setResultCode(2);
                        callBackJsObject(JsConst.CALLBACK_SET_CONVERSATION_NOTIFICATION_STATUS, DataHelper.gson.toJsonTree
                                (statusVO));
                    }
                });
     }

    public void getLatestMessages(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        final List<MessageResultVO> messageResultVOList=new ArrayList<MessageResultVO>();
        ConversationInputVO inputVO=DataHelper.gson.fromJson(json,ConversationInputVO.class);
        mRongIMClient.getLatestMessages(inputVO.getConversationType(), inputVO.getTargetId(), inputVO.getCount(), new RongIMClient.ResultCallback<List<io.rong.imlib.model.Message>>() {
            @Override
            public void onSuccess(List<io.rong.imlib.model.Message> messages) {
                  if (messages!=null){
                    for (io.rong.imlib.model.Message message:messages) {
                        messageResultVOList.add(ModelTranslation.translateMessageVO(message));
                    }
                }
                callBackJsObject(JsConst.CALLBACK_GET_LATEST_MESSAGES, DataHelper.gson.toJsonTree(messageResultVOList));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                callBackJsObject(JsConst.CALLBACK_GET_LATEST_MESSAGES, DataHelper.gson.toJsonTree(messageResultVOList));
            }
        });


    }

    public void getHistoryMessages(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        final List<MessageResultVO> messageResultVOList=new ArrayList<MessageResultVO>();
        ConversationInputVO inputVO=DataHelper.gson.fromJson(json,ConversationInputVO.class);
        mRongIMClient.getHistoryMessages(inputVO.getConversationType(), inputVO.getTargetId(), inputVO
                .getOldestMessageId(), inputVO.getCount(), new RongIMClient.ResultCallback<List<io.rong.imlib.model.Message>>() {
            @Override
            public void onSuccess(List<io.rong.imlib.model.Message> messages) {
                if (messages!=null){
                    for (io.rong.imlib.model.Message message:messages) {
                        messageResultVOList.add(ModelTranslation.translateMessageVO(message));
                    }
                }
                callBackJsObject(JsConst.CALLBACK_GET_HISTORY_MESSAGES, DataHelper.gson.toJsonTree(messageResultVOList));

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                callBackJsObject(JsConst.CALLBACK_GET_HISTORY_MESSAGES, DataHelper.gson.toJsonTree(messageResultVOList));
            }
        });

     }

    public void deleteMessages(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        final DeleteMessagesResultVO resultVO=new DeleteMessagesResultVO();
        DeleteMessagesVO deleteMessagesVO=DataHelper.gson.fromJson(json,DeleteMessagesVO.class);
        mRongIMClient.deleteMessages(deleteMessagesVO.getMessageIds(), new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                resultVO.setResultCode(0);
                callBackJsObject(JsConst.CALLBACK_DELETE_MESSAGES, DataHelper.gson.toJsonTree(resultVO));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                resultVO.setResultCode(2);
                callBackJsObject(JsConst.CALLBACK_DELETE_MESSAGES, DataHelper.gson.toJsonTree(resultVO));
            }
        });
    }

    public void clearMessages(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        final DeleteMessagesResultVO resultVO=new DeleteMessagesResultVO();
        ConversationInputVO inputVO=DataHelper.gson.fromJson(json,ConversationInputVO.class);
        mRongIMClient.clearMessages(inputVO.getConversationType(), inputVO.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                resultVO.setResultCode(0);
                callBackJsObject(JsConst.CALLBACK_CLEAR_MESSAGES,DataHelper.gson.toJsonTree(resultVO));
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                resultVO.setResultCode(2);
                callBackJsObject(JsConst.CALLBACK_CLEAR_MESSAGES,DataHelper.gson.toJsonTree(resultVO));
            }
        });

    }

    public int getTotalUnreadCount(String[] params) {
        return  mRongIMClient.getTotalUnreadCount();
    }

    public int getUnreadCount(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return -1;
        }
        String json = params[0];
        ConversationInputVO inputVO=new ConversationInputVO();
        return mRongIMClient.getUnreadCount(inputVO.getConversationType(),inputVO.getTargetId());
    }

    public int getUnreadCountByConversationTypes(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return -1;
        }
        String json = params[0];
        ConversationInputVO inputVO=new ConversationInputVO();
        return mRongIMClient.getUnreadCount(inputVO.getConversationTypes());
    }

    public void setMessageReceivedStatus(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        SetMessageReceivedStatusVO statusVO=DataHelper.gson.fromJson(json,SetMessageReceivedStatusVO.class);
        String status=statusVO.getReceivedStatus();
        int value=0;
        if(status.equals("UNREAD"))
            value = 0;
        else if(status.equals("READ"))
            value = 1;
        else if(status.equals("LISTENED"))
            value = 2;
        else if(status.equals("DOWNLOADED"))
            value = 4;
        mRongIMClient.setMessageReceivedStatus(statusVO.getMessageId(), new io.rong.imlib.model.Message.ReceivedStatus
                (value), new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                callBackJsObject(JsConst.CALLBACK_SET_MESSAGE_RECEIVED_STATUS, "");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
     }

    public void clearMessagesUnreadStatus(String[] params) {
        if (params == null || params.length < 1) {
            errorCallback(0, 0, "error params!");
            return;
        }
        String json = params[0];
        ConversationInputVO inputVO=DataHelper.gson.fromJson(json,ConversationInputVO.class);
        mRongIMClient.clearMessagesUnreadStatus(inputVO.getConversationType(), inputVO.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                callBackJsObject(JsConst.CALLBACK_CLEAR_MESSAGES_UNREAD_STATUS, "");
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
     }

    private void callBackPluginJs(String methodName, String jsonData) {
        String js = SCRIPT_HEADER + "if(" + methodName + "){"
                + methodName + "('" + jsonData + "');}";
        onCallback(js);
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


}
