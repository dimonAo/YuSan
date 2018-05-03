
package com.wtwd.yusan.ease.ui;
/**
 * Created by ChangLe on 2018/4/20.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseExpandGridView;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.hyphenate.util.EMLog;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.BaseResponseCallback;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.BaseResponse;
import com.wtwd.yusan.ease.net.response.GroupDetailInfo;
import com.wtwd.yusan.ease.util.JsonUtil;
import com.wtwd.yusan.ease.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailsActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "GroupDetailsActivity";
    private static final int REQUEST_CODE_ADD_USER = 0;
    private static final int REQUEST_CODE_EXIT = 1;
    private static final int REQUEST_CODE_EXIT_DELETE = 2;
    private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;

    private String mGroupId;
    private String mGroupName, mImGroupId;
    private ProgressBar loadingPB;
    private Button exitBtn;
    private Button deleteBtn;
    private TextView tv_groupName;
    private GridAdapter membersAdapter;
    private ProgressDialog progressDialog;

    public static GroupDetailsActivity instance;
    private EaseSwitchButton switchButton;

    private List<String> memberList = Collections.synchronizedList(new ArrayList<String>());
    private ArrayList<String> repeatMemberList = new ArrayList<String>();
    private ArrayList<Long> repeatUserIdList = new ArrayList<Long>();
    private List<GroupDetailInfo> mInfosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupId = getIntent().getStringExtra("groupId");
        mGroupName = getIntent().getStringExtra("groupName");
        mImGroupId = getIntent().getStringExtra("im_groupId");
        Log.e("IMDemo", "GroupDetailsActivity--groupId--" + mGroupId + "mGroupName" + mGroupName + "--mImGroupId" + mImGroupId);
        setContentView(R.layout.em_activity_group_details);
        instance = this;
        findView();
        tv_groupName.setText(mGroupName);
        //get push configs
        membersAdapter = new GridAdapter(this, R.layout.em_grid_owner, new ArrayList<String>());

        // 保证每次进详情看到的都是最新的group
        updateGroupDetail(Constant.CONSTANT_USER_ID, mImGroupId);
    }

    private void findView() {
        loadingPB = (ProgressBar) findViewById(R.id.progressBar);
        exitBtn = (Button) findViewById(R.id.btn_exit_grp);
        deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
        RelativeLayout changeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);
        RelativeLayout rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
        switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);
        tv_groupName = findViewById(R.id.sm_groud_name);
        changeGroupNameLayout.setOnClickListener(this);
        rl_switch_block_groupmsg.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String st1 = getResources().getString(R.string.being_added);
        String st2 = getResources().getString(R.string.is_quit_the_group_chat);
        final String st3 = getResources().getString(R.string.is_modify_the_group_name);

        if (resultCode == RESULT_OK) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailsActivity.this);
                progressDialog.setMessage(st1);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            switch (requestCode) {
                case REQUEST_CODE_ADD_USER:// 添加群成员
                    final String[] members = data.getStringArrayExtra("newmembers");
                    progressDialog.setMessage(st1);
                    progressDialog.show();
                    addMember(Constant.CONSTANT_USER_ID, StringUtils.arrayToStrWithComma(members), mGroupId, members);
                    break;
                case REQUEST_CODE_EXIT: // 退出群
                    progressDialog.setMessage(st2);
                    progressDialog.show();
                    exitGrop(Constant.CONSTANT_USER_ID, mGroupId, isAdminStatus());
                    break;

                case REQUEST_CODE_EDIT_GROUPNAME: //修改群名称
                    final String returnData = data.getStringExtra("data");
                    if (!TextUtils.isEmpty(returnData) && !containsEmoji(returnData)) {
                        progressDialog.setMessage(st3);
                        progressDialog.show();
                        changeGroupName(Constant.CONSTANT_USER_ID, mGroupId, returnData);
                    } else {
                        Toast.makeText(GroupDetailsActivity.this, "暂不支持表情", Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {

        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    /**
     * 点击退出群组按钮
     *
     * @param view
     */
    public void exitGroup(View view) {
        exitGrop(Constant.CONSTANT_USER_ID, mGroupId, isAdminStatus());
    }

    /**
     * 退出群组
     */
    private void exitGrop(String userId, String groupId, String isAdmin) {
        Log.e("IMDemo", "exitGrop groupId" + groupId + "=isAdmin=" + isAdmin);
        final String st1 = getResources().getString(R.string.Exit_the_group_chat_failure);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("groupId", groupId);
        params.put("isAdmin", isAdmin);
        ApiInterface.exitGroup(params, new BaseResponseCallback() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    setResult(RESULT_OK);
                    GroupDetailsActivity.this.finish();
                    if (ChatActivity.activityInstance != null) {
                        ChatActivity.activityInstance.finish();
                    }
                } else {
                    Toast.makeText(GroupDetailsActivity.this, st1, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(GroupDetailsActivity.this, st1 + "——" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 增加群成员
     *
     * @param member
     */
    private void addMember(final String userId, final String member, final String groupId, final String[] members) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("member", member);
        params.put("groupId", groupId);
        ApiInterface.addMember(params, new BaseResponseCallback() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    updateGroupDetail(userId, mImGroupId);
                    Toast.makeText(GroupDetailsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupDetailsActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(GroupDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    /**
     * 删除群成员
     *
     * @param member
     */
    private void delMember(final String userId, final String member, final String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("member", member);
        params.put("groupId", groupId);
        ApiInterface.delMember(params, new BaseResponseCallback() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    updateGroupDetail(userId, mImGroupId);
                    Toast.makeText(GroupDetailsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GroupDetailsActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Log.e("IMDemo", "delMember-error-" + error);
                Toast.makeText(GroupDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void forNameList(List<String> list) {
        list.clear();
        for (GroupDetailInfo info : mInfosList) {
            String name = info.getUser_name();
            if (!list.contains(name)) {
                list.add(name);
            }
        }
    }

    private void forUserIdList(List<Long> list) {
        list.clear();
        for (GroupDetailInfo info : mInfosList) {
            Long userId = info.getUser_id();
            if (!list.contains(userId)) {
                list.add(userId);
            }
        }
    }

    /**
     * 判断是否为管理员身份
     *
     * @return
     */
    private String isAdminStatus() {
        for (GroupDetailInfo info : mInfosList) {
            Long userId = info.getUser_id();
            long isAdmin = info.getIs_admin();
            if (userId == Long.parseLong(Constant.CONSTANT_USER_ID)) {
                return String.valueOf(isAdmin);
            }
        }
        return null;
    }

    private void refreshMembersAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                membersAdapter = new GridAdapter(GroupDetailsActivity.this, R.layout.em_grid_owner, new ArrayList<String>());
                membersAdapter.clear();
                synchronized (memberList) {
                    membersAdapter.addAll(memberList);
                }
                membersAdapter.notifyDataSetChanged();

                EaseExpandGridView userGridview = (EaseExpandGridView) findViewById(R.id.gridview);
                userGridview.setAdapter(membersAdapter);
                loadingPB.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_block_groupmsg: // 屏蔽或取消屏蔽群组
                toggleBlockGroup();
                break;

            case R.id.rl_change_group_name:
                startActivityForResult(new Intent(this, EditActivity.class)
                                .putExtra("groupName", mGroupName),
                        REQUEST_CODE_EDIT_GROUPNAME);
                break;
            default:
                break;
        }

    }

    /**
     * 获取群成员信息
     *
     * @param userId
     * @param groupId
     */
    private void updateGroupDetail(String userId, String groupId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("groupId", groupId);
        ApiInterface.getGroupDetail(params, new StringCallback() {
            @Override
            public void onSuccess(String response) {
                if (!TextUtils.isEmpty(response) && JsonUtil.getJsonUtil().isRequestSeccess(response)) {
                    mInfosList = getListObject(response, GroupDetailInfo.class);
                    Log.e("IMDemo", "updateGroupDetail--" + response);
                    mGroupId=String.valueOf(mInfosList.get(0).getGroup_id());
                    forNameList(memberList);
                    refreshMembersAdapter();
                } else {
                    Toast.makeText(GroupDetailsActivity.this, R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onError(String error) {
                Log.e("IMDemo", "updateGroupDetail-error-" + error);
                Toast.makeText(GroupDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /**
     * 修改群昵称
     *
     * @param userId
     * @param groupId
     * @param groupName
     */
    private void changeGroupName(final String userId, final String groupId, final String groupName) {
//        Log.i("IMDemo", "changeGroupName userId-" + userId + "-groupId-" + groupId + "--groupName" + groupName);
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("groupId", groupId);
        params.put("groupName", groupName);
        ApiInterface.updateGroup(params, new BaseResponseCallback() {
            @Override
            public void onSuccess(BaseResponse response) {
                final String st6 = getResources().getString(R.string.Modify_the_group_name_successful);
                int status = response.getStatus();
                if (status == 1) {
                    Toast.makeText(GroupDetailsActivity.this, st6, Toast.LENGTH_SHORT).show();
                }
                if (!mGroupName.equals(groupName)) {
                    mGroupName = groupName;
                    tv_groupName.setText(mGroupName);
                }
                updateGroupDetail(userId, mImGroupId);
                progressDialog.dismiss();
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
                Toast.makeText(GroupDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private ProgressDialog createProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(GroupDetailsActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        return progressDialog;
    }

    private void toggleBlockGroup() {
        if (switchButton.isSwitchOpen()) {
            EMLog.d(TAG, "change to unblock group msg");
            createProgressDialog();
            progressDialog.setMessage(getString(R.string.Is_unblock));
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().unblockGroupMessage(mImGroupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                switchButton.closeSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.remove_group_of, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }).start();

        } else {
            String st8 = getResources().getString(R.string.group_is_blocked);
            final String st9 = getResources().getString(R.string.group_of_shielding);
            EMLog.d(TAG, "change to block group msg");
            createProgressDialog();
            progressDialog.setMessage(st8);
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().blockGroupMessage(mImGroupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                switchButton.openSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            }).start();
        }
    }


    /**
     * 群组成员gridadapter
     *
     * @author admin_new
     */
    private class GridAdapter extends ArrayAdapter<String> {

        private int res;

        public GridAdapter(Context context, int textViewResourceId, List<String> objects) {
            super(context, textViewResourceId, objects);
            res = textViewResourceId;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(res, null);
                holder.imageView = (ImageView) convertView.findViewById(R.id.iv_avatar);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
                holder.badgeDeleteView = convertView.findViewById(R.id.badge_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final LinearLayout button = convertView.findViewById(R.id.button_avatar);
            // add button
            if (position == getCount() - 1) {
                holder.textView.setText("");
                holder.imageView.setImageResource(R.drawable.em_smiley_add_btn);
                convertView.setVisibility(View.VISIBLE);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forNameList(repeatMemberList);
                        // 进入选人页面
                        startActivityForResult(
                                new Intent(GroupDetailsActivity.this, GroupPickContactsActivity.class)
                                        .putExtra("groupId", mGroupId)
                                        .putStringArrayListExtra("repeatList", repeatMemberList),
                                REQUEST_CODE_ADD_USER);
                    }
                });
                return convertView;
            } else {
                // members
                final String username = getItem(position);
                EaseUserUtils.setUserNick(username, holder.textView);
                EaseUserUtils.setUserAvatar(getContext(), username, holder.imageView);
                LinearLayout id_background = (LinearLayout) convertView.findViewById(R.id.l_bg_id);
                if (memberList.size() > 2) {
                    if (position == 0) {
                        holder.badgeDeleteView.setVisibility(View.GONE);
                    } else {
                        holder.badgeDeleteView.setVisibility(View.VISIBLE);
                    }
                    holder.badgeDeleteView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forUserIdList(repeatUserIdList);
                            delMember(Constant.CONSTANT_USER_ID, String.valueOf(repeatUserIdList.get(position)), mGroupId);
                        }
                    });
                }
            }

            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }
    }

    public void back(View view) {
        setResult(RESULT_OK, new Intent().putExtra("groupName", mGroupName));
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra("groupName", mGroupName));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;

    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView badgeDeleteView;
    }
}
