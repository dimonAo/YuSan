package com.wtwd.yusan.ease.ui.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseDingMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.NetUtils;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.db.InviteMessgeDao;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.Message;
import com.wtwd.yusan.ease.ui.ChatActivity;
import com.wtwd.yusan.ease.ui.IndexFragment;
import com.wtwd.yusan.ease.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XJM on 2018/4/23.
 */

public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;
    private View headerView, headerView2;
    private TextView tvSystemMessage, tvNoticeMessage;

    @Override
    protected void initView() {
        super.initView();
        hideTitleBar();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.em_row_system_msg, null);
        headerView.setTag(1);
        tvSystemMessage = headerView.findViewById(R.id.message);
        ((TextView) headerView.findViewById(R.id.time)).setText(DateUtils.getTimestampString(new Date(System.currentTimeMillis())));
        headerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.em_row_system_header_hight)));

        headerView2 = LayoutInflater.from(getContext()).inflate(R.layout.em_row_guanfang_msg, null);
        headerView2.setTag(2);
        tvNoticeMessage = headerView2.findViewById(R.id.message);
        ((TextView) headerView2.findViewById(R.id.time)).setText(DateUtils.getTimestampString(new Date(System.currentTimeMillis())));
        headerView2.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.em_row_system_header_hight)));
//        getNotice();
//        getSystemMessage();

    }

    @Override
    public void onResume() {
        super.onResume();
        getNotice();
        getSystemMessage();
    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.addHeaderView(headerView2);
        conversationListView.addHeaderView(headerView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (headerView.getId() == view.getId()) {
                    if (!StringUtils.isEmpty(tvSystemMessage.getText().toString())) {
                        Message message = (Message) headerView.getTag();
                        Toast.makeText(getActivity(), message.getContent() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "暂无消息" + "", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                if (headerView2.getId() == view.getId()) {
                    if (!StringUtils.isEmpty(tvNoticeMessage.getText().toString())) {
                        Message message = (Message) headerView2.getTag();
                        Toast.makeText(getActivity(), message.getContent() + "", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "暂无消息" + "", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                EMConversation conversation = conversationListView.getItem(position - conversationListView.getHeaderViewsCount());
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_NAME, username);
                    intent.putExtra(Constant.EXTRA_USER_ID, username);
                    startActivity(intent);
                }
            }
        });
//        super.setUpView();
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }

        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position - conversationListView.getHeaderViewsCount();

        if (position < 0) {
            Toast.makeText(getActivity(), "不能删除", Toast.LENGTH_SHORT).show();
            return true;
        }

        EMConversation tobeDeleteCons = conversationListView.getItem(position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
            // To delete the native stored adked users in this conversation.
            if (deleteMessage) {
                EaseDingMessageHelper.get().delete(tobeDeleteCons);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((IndexFragment) getParentFragment()).updateUnreadLabel();
        return true;
    }

    /**
     * 获取官方公告
     */
    private void getNotice() {
        Map<String, String> params = new HashMap<>();
        ApiInterface.getNotice(params, new StringCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("response", response);
                Message message = getObject(response, Message.class);
                if (message != null) {
                    if (!StringUtils.isEmpty(message.getContent())) {
                        tvNoticeMessage.setText(message.getContent());
                        headerView2.setTag(message);
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.e("response", error);
            }
        });
    }

    /**
     * 获取系统消息
     */
    private void getSystemMessage() {
        Map<String, String> params = new HashMap<>();
        ApiInterface.getSystemMessage(params, new StringCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("response_", response);
                Message message = getObject(response, Message.class);
                if (message != null) {
                    if (!StringUtils.isEmpty(message.getContent())) {
                        tvSystemMessage.setText(message.getContent());
                        headerView.setTag(message);
                    }
                }

            }

            @Override
            public void onError(String error) {
                Log.e("response_", error);
            }
        });

    }

    public <T> T getObject(String response, Class<T> clazz) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if (status == 1) {
                String obj = jsonObject.has("object") ? jsonObject.getString("object") : "";
                if (TextUtils.isEmpty(obj)) {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getActivity(), msg + "", Toast.LENGTH_SHORT);
                    return null;
                }
                return JSON.parseObject(obj, clazz);
            } else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getActivity(), msg + "", Toast.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> List<T> getListObject(String response, Class<T> clazz) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if (status == 1) {
                String obj = jsonObject.has("object") ? jsonObject.getString("object") : "";
                if (TextUtils.isEmpty(obj)) {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getActivity(), msg + "", Toast.LENGTH_SHORT);
                    return null;
                }
                return JSON.parseArray(obj, clazz);
            } else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getActivity(), msg + "", Toast.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
