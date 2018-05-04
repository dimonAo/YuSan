package com.wtwd.yusan.ease.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.adapter.NewGroupAdapter;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.GroupInfo;
import com.wtwd.yusan.ease.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ChangLe on 2018/4/13.
 */

public class GroupsActivity extends BaseActivity {
	public static final String TAG = "GroupsActivity";
	private ListView groupListView;
	protected List<EMGroup> grouplist;
	protected List<GroupInfo> grouplist_new;
	private NewGroupAdapter newAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;
	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_fragment_groups);
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		grouplist = EMClient.getInstance().groupManager().getAllGroups();
		groupListView = (ListView) findViewById(R.id.list);

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
				R.color.holo_orange_light, R.color.holo_red_light);
		//pull down to refresh
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				refresh();
			}
		});

		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// enter group chat
				Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
				// it is group chat
				intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
				intent.putExtra("groupId", String.valueOf(newAdapter.getItem(position).getGroup_id()));
				intent.putExtra("userId", String.valueOf(newAdapter.getItem(position).getIm_groupId()));
				intent.putExtra("groupName", String.valueOf(newAdapter.getItem(position).getGroupIdStr()));
				startActivityForResult(intent, 0);
			}

		});

		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});
	}

	private void getGroupList(final String UserId) {
		Map<String, String> map = new HashMap<>();
		map.put("userId", UserId);
		ApiInterface.getGroupList(map, new StringCallback() {
			@Override
			public void onSuccess(String response) {
				Log.i(TAG, "getGroupList+" + response);
				grouplist_new = JsonUtil.getJsonUtil().JsonGroup(response);
				newAdapter = new NewGroupAdapter(GroupsActivity.this, 1, grouplist_new);
				groupListView.setAdapter(newAdapter);
				swipeRefreshLayout.setRefreshing(false);
			}

			@Override
			public void onError(String error) {
				Log.i(TAG, "getGroupList+" + error);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
		refresh();
		super.onResume();
	}

	private void refresh() {
		getGroupList(Constant.CONSTANT_USER_ID);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	public void back(View view) {
		finish();
	}
}
