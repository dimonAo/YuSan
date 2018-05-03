package com.wtwd.yusan.ease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;

import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.permission.PermissionsManager;
import com.wtwd.yusan.ease.util.swipe.SwipeHelper;

/**
 * chat activity，EaseChatFragment was used {@link #}
 */
public class ChatActivity extends BaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    public String toChatUsername;
    public String mUserName;
    public String groupName;
    public String groupId;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userName");
        mUserName = getIntent().getExtras().getString("userId");
        groupId = getIntent().getExtras().getString("groupId");
        groupName = getIntent().getExtras().getString("groupName");
        Log.i("changle", "-toChatUsername-" + toChatUsername + "-mUserName-"
                + mUserName + "-groupId-" + groupId + "--groupName-" + groupName);
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

//        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
//            @Override
//            public void onMessageReceived(List<EMMessage> list) {
//                Log.e("message",list.get(0).getBody().toString());
//            }
//
//            @Override
//            public void onCmdMessageReceived(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageRead(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageDelivered(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageRecalled(List<EMMessage> list) {
//
//            }
//
//            @Override
//            public void onMessageChanged(EMMessage emMessage, Object o) {
//
//            }
//        });

    }

    public String getUserName() {
        return toChatUsername;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public String getmGroudId() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean processTouchEvent = SwipeHelper.instance().processTouchEvent(ev);
        if (processTouchEvent) {
            ((ChatFragment) chatFragment).getSwipeRefreshLayout().setEnabled(false);
            return true;
        }
        ((ChatFragment) chatFragment).getSwipeRefreshLayout().setEnabled(true);
        return super.dispatchTouchEvent(ev);
    }

}