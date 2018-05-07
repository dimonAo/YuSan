package com.wtwd.yusan.ease.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.EaseDingMessageHelper;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentHelper;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.chatrow.MissionInfoCallPresenter;
import com.hyphenate.easeui.widget.chatrow.RedPacketInfoCallPresenter;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EasyUtils;
import com.wtwd.yusan.R;
import com.wtwd.yusan.activity.DetailTaskActivity;
import com.wtwd.yusan.activity.PublishTaskActivity;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.info.EmojiconExampleGroupData;
import com.wtwd.yusan.ease.info.RobotUser;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.util.JsonUtil;
import com.wtwd.yusan.ease.util.MessageUtil;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by ChangLe on 2018/4/11.
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragmentHelper {

    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_REDPACKET = 11;
    private static final int ITEM_TASK = 12;

    private static final int REQUEST_CODE_SEND_PACKET = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;
    private static final int REQUEST_CODE_SEND_TASK = 16;

    private String userID;
    /**
     * if it is chatBot
     */
    private boolean isRobot;

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = getActivity().getIntent().getExtras().getString("userId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState,
                IMHelper.getInstance().getModel().isMsgRoaming() && (chatType != EaseConstant.CHATTYPE_CHATROOM));
    }

    @Override
    protected void setUpView() {
        setChatFragmentHelper(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = IMHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
                isRobot = true;
            }
        }
        super.setUpView();
        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (EasyUtils.isSingleActivity(getActivity())) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                onBackPressed();
            }
        });
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
                                putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
        //extend menu items
        //注册红包和任务的图标
        inputMenu.registerExtendMenuItem(R.string.attach_redpacket, R.drawable.ease_red_packet_bg, ITEM_REDPACKET, extendMenuItemClickListener);
        inputMenu.registerExtendMenuItem(R.string.attach_task, R.drawable.ease_chat_task_normal, ITEM_TASK, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    // To delete the ding-type message native stored acked users.
                    EaseDingMessageHelper.get().delete(contextMenuMessage);
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // 转发任务
                    Intent intent = new Intent(getActivity(), PickContactNoCheckboxActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);
                    break;
                case ContextMenuActivity.RESULT_CODE_RECALL://recall
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMMessage msgNotification = EMMessage.createTxtSendMessage(" ", contextMenuMessage.getTo());
                                EMTextMessageBody txtBody = new EMTextMessageBody(getResources().getString(R.string.msg_recall_by_self));
                                msgNotification.addBody(txtBody);
                                msgNotification.setMsgTime(contextMenuMessage.getMsgTime());
                                msgNotification.setLocalTime(contextMenuMessage.getMsgTime());
                                msgNotification.setAttribute(Constant.MESSAGE_TYPE_RECALL, true);
                                msgNotification.setStatus(EMMessage.Status.SUCCESS);
                                EMClient.getInstance().chatManager().recallMessage(contextMenuMessage);
                                EMClient.getInstance().chatManager().saveMessage(msgNotification);
                                messageList.refresh();
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();

                    // Delete group-ack data according to this message.
                    EaseDingMessageHelper.get().delete(contextMenuMessage);
                    break;

                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                case REQUEST_CODE_GROUP_DETAIL:
                    if (data != null) {
                        String groupName = data.getStringExtra("groupName");
                        if (!TextUtils.isEmpty(groupName)) {
                            titleBar.setTitle(groupName);
                        }
                    }
                    break;
                case REQUEST_CODE_SEND_PACKET:
                    if (data != null) {
                        String money = data.getStringExtra("money");
                        //判断是群聊还是私聊
                        String to = (chatType == EaseConstant.CHATTYPE_SINGLE) ? "1" : "2";

//                        Log.e("to",to);

                        sendRedPacket(Constant.CONSTANT_USER_ID, "1", money, "3", to, toChatUsername);
                    }
                    break;
                case REQUEST_CODE_SEND_TASK:
                    if (data != null) {
                        String money = data.getStringExtra("money");
                        String content = data.getStringExtra("content");
                        String type = data.getStringExtra("type");
                        String address = data.getStringExtra("address");
                        String startTime = data.getStringExtra("startTime");
                        String anonymous = data.getStringExtra("anonymous");
                        String payType = data.getStringExtra("payType");
                        //判断是群聊还是私聊
                        String to = (chatType == EaseConstant.CHATTYPE_SINGLE) ? "1" : "2";

//                        Log.e("to",to);

                        publishMission(Constant.CONSTANT_USER_ID, content, type,"3", money,address,startTime, to, toChatUsername,anonymous,"1");
                    }

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if (isRobot) {
            //set message extension
            message.setAttribute("em_robot_message", isRobot);
        }
    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return null;
    }


    @Override
    public void onEnterToChatDetails() {

        if (chatType == Constant.CHATTYPE_GROUP) {
            String groupId = !TextUtils.isEmpty(mGroupId) ? mGroupId : toChatUsername;
            Log.e("IMDemo", "onEnterToChatDetails:" + toChatUsername + "--mGroupId--" + mGroupId + "-groupId--" + groupId+"_mGroupName_"+mGroupName);
            startActivityForResult(
                    (new Intent(getActivity(), GroupDetailsActivity.class)
                            .putExtra("groupName", mGroupName)
                            .putExtra("groupId", groupId)
                            .putExtra("im_groupId", toChatUsername)),
                    REQUEST_CODE_GROUP_DETAIL);
        }
    }


    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
        Toast.makeText(getActivity(), "头像", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        //消息框点击事件
        if (message.getType() == EMMessage.Type.TXT) {
            String msg = message.getBody().toString();
            if (MessageUtil.getMessageUtil().isRedPacketMsg(msg)) {
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    String packet_id = MessageUtil.getMessageUtil().getRedPacketMsgId(msg);
                    if (!TextUtils.isEmpty(packet_id)) {
//                    Log.i("IMDemo", "msg:" + msg + "packet_id:" + packet_id);
                        startActivity(new Intent(getActivity(), RedPacketDetailActivity.class)
                                .putExtra("packet_id", packet_id));
                    }
                }
            }else if (MessageUtil.getMessageUtil().isMissionMsg(msg)) {
                String mission_id = MessageUtil.getMessageUtil().getMissionMsgId(msg);
                getMission(mission_id);

            } else {
                Toast.makeText(getActivity(), "本人不可打开", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    private void getMission(String mission_id) {

        OkHttpUtils.get()
               .addParams("missionId",mission_id)
                .url(Constans.GET_MISSION)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "publish mission : " + response);
//                        ResultEntity mEn = Utils.getResultEntity(response);
                        try {
                            JSONObject mTaskJson = new JSONObject(response);

                            int status = mTaskJson.optInt("status");
                            //com.alibaba.fastjson.JSONObject taskResult = mTaskJson.get("object");
                            String mTaskJsonArray = mTaskJson.optString("object");
                            Log.e(TAG, "mTaskJsonArray --> " + mTaskJsonArray);
                            List<TaskEntity> list= new ArrayList<>();
                            list = GsonUtils.jsonToList(mTaskJsonArray,TaskEntity.class);
                            Log.e(TAG, "enenenen :----> " + list.get(0).toString());
                           // TaskEntity taskEntity = list.get(0);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("task_entity", list.get(0));
                            startActivity(new Intent(getActivity(), DetailTaskActivity.class)
                                    .putExtras(bundle));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // no message forward when in chat room
        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message", message)
                        .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_REDPACKET:
//                Toast.makeText(getActivity(), "红包", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SendRedPacketActivity.class);
                Bundle bundle = new Bundle();
                String to = (chatType == EaseConstant.CHATTYPE_SINGLE) ? "1" : "2";
                bundle.putString("to",to);
                bundle.putString("toid",toChatUsername);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_SEND_PACKET);
                break;
            case ITEM_TASK:
//            selectFileFromLocal();
                Intent taskIntent = new Intent(getActivity(), PublishTaskActivity.class);
                Bundle taskBundle = new Bundle();
                String to2 = (chatType == EaseConstant.CHATTYPE_SINGLE) ? "1" : "2";
                taskBundle.putString("to",to2);
                taskBundle.putString("toid",toChatUsername);
                taskIntent.putExtras(taskBundle);
                startActivityForResult(taskIntent, REQUEST_CODE_SEND_TASK);
                Toast.makeText(getActivity(), "任务", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
            return 11;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {

            if (message.getType() == EMMessage.Type.TXT) {
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                String msg = body.getMessage().toString();

                Log.e("msg", msg + "");
            }
            return 0;
        }

        @Override
        public EaseChatRowPresenter getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {

            Log.e("IMDemo", getCustomChatRowType(message) + "");

            if (message.getType() == EMMessage.Type.TXT) {
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                String msg = body.getMessage();
                Log.i("IMDemo", "msg--" + msg);
                if (message.direct() == EMMessage.Direct.RECEIVE) {
                    Log.i("IMDemo", "RECEIVE:");
                    if (MessageUtil.getMessageUtil().isRedPacketMsg(msg)) {
                        return new RedPacketInfoCallPresenter();
                    } else if (MessageUtil.getMessageUtil().isMissionMsg(msg)) {
                        return new MissionInfoCallPresenter();
                    }
                } else {
                    Log.i("IMDemo", "SEND:");
                }
//                if (message.getBooleanAttribute(RedPacketConstant.RedPacketExtType, false)) {
//                    return new RedPacketInfoCallPresenter();
//                }

            }
            return null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static final String WTWD_MISSION_TXT = "WtwdMissionTxt";
    public static final String WTWD_REDPACKET_TXT = "WtwdRedPacketTxt";

    /**
     * /**
     * 发送红包
     *
     * @param userId
     * @param payType
     * @param money
     * @param type
     */
    private void sendRedPacket(final String userId, String payType, String money, String type, final String to, String to_id) {
        Log.i("IMDemo", "-payType-" + payType + "-money-" + money + "-to-" + to + "--to_id-" + to_id);
        Log.i("IMDemo", "toChatUsername-" + toChatUsername + "-mUserName-" + mUserName);
        Map<String, String> params = new HashMap<String, String>();
//        params.put("userId", userId);
        params.put("userName", Constant.CONSTANT_USER_NAME);
        params.put("payType", payType);
        params.put("money", money);
        params.put("type", type);
        params.put("to", to);
        params.put("to_id", to_id);
        ApiInterface.payRedPacket(params, new StringCallback() {

            @Override
            public void onSuccess(String response) {
                Log.i("IMDemo", "sendRedPacket response:" + response);
                if("1".equals(to)) {
                  /*  EMMessage msg = EMMessage.createSendMessage(EMMessage.Type.TXT);
                    msg.setDirection(EMMessage.Direct.SEND);
                    msg.setTo(toChatUsername);
                    EMTextMessageBody body = new EMTextMessageBody(WTWD_REDPACKET_TXT+":NULL"+":0");
                    msg.addBody(body);
                    sendLocalMessage(msg);*/
                }
                Toast.makeText(getActivity(), JsonUtil.getJsonUtil().getStatusStr(response), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.i("IMDemo", "sendRedPacket error:" + error);
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void publishMission(String userId,String content,String type,String sex,String money,String address,String startTime,String to,String toId,String anonymous,String payType) {

        //request service for publish mission
//                bundle.putString("userId", userId + "");
//                bundle.putString("content", getTaskDetailContent()); //任务描述
//                bundle.putString("type", getTaskType()); //任务类型
//                bundle.putString("sex", getSexType()); //接受者性别限制
//                bundle.putString("money", getTaskMoney());//任务金额
//                bundle.putString("address", getTaskAddress());//任务地址
//                bundle.putString("startTime", getTaskStartTime());//任务开始时间 yyyy-MM-dd HH:mm格式
//                bundle.putString("to", toId); //发送给谁，0所有人，指定人传用户userid
//                bundle.putString("anonymous", getTaskAnonymous()); //是否匿名，1匿名 ； 0不匿名


       // Bundle bundle = getMissionParameterIntent();

        Map<String, String> mPublishMap = new HashMap<>();
        mPublishMap.put("userId", userId);
        mPublishMap.put("content", content);
        mPublishMap.put("type",type);
        mPublishMap.put("sex", sex);
        mPublishMap.put("money", money);
        mPublishMap.put("address", address);
        mPublishMap.put("startTime", startTime);
        mPublishMap.put("to",to);
        mPublishMap.put("toId", toId);
        mPublishMap.put("anonymous", anonymous);
        mPublishMap.put("payType", payType);

        Log.e(TAG, "publish map : " + mPublishMap.toString());
        OkHttpUtils.get()
                .url(Constans.PUBLISH_MISSION)
                .params(mPublishMap)
                .build()
                .execute(new com.zhy.http.okhttp.callback.StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "publish mission : " + response);
//                        ResultEntity mEn = Utils.getResultEntity(response);
                        try {
                            JSONObject mMissionJson = new JSONObject(response);
                            int status = mMissionJson.optInt("status");
                            if (Constans.REQUEST_SUCCESS == status) {
                                //request success
                               // finish();
                            } else {
                                //showToast("发布任务失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

}