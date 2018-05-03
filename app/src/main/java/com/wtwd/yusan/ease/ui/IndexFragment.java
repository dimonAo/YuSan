package com.wtwd.yusan.ease.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.hyphenate.EMClientListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.db.InviteMessgeDao;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.BaseResponseCallback;
import com.wtwd.yusan.ease.net.response.BaseResponse;
import com.wtwd.yusan.ease.ui.fragment.ContactListFragment;
import com.wtwd.yusan.ease.ui.fragment.ConversationListFragment;
import com.wtwd.yusan.ease.util.StringUtils;
import com.wtwd.yusan.ease.util.swipe.DisplayUtil;
import com.wtwd.yusan.ease.widget.tab.TabLayout;
import com.wtwd.yusan.ease.widget.tab.TabPageIndicatorAdapter;
import com.wtwd.yusan.fragment.MeFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by XJM on 2018/4/12.
 */

public class IndexFragment extends BaseFragment {

	private static final String TAG = IndexFragment.class.getSimpleName();

	private static IndexFragment mInstance;
	public static IndexFragment newInstance() {
		Bundle args = new Bundle();
		IndexFragment fragment = new IndexFragment();
		fragment.setArguments(args);
		return fragment;
	}
	public static IndexFragment getFragment() {
		if (null == mInstance) {
			mInstance = new IndexFragment();
		}
		return mInstance;
	}
	private Fragment[] fragments;

	private ContactListFragment contactListFragment;
	private ConversationListFragment conversationListFragment;

	// user logged into another device
	public boolean isConflict = false;
	// user account was removed
	private boolean isCurrentAccountRemoved = false;

	private TabLayout tabLayout;
	private ViewPager viewPager;
	private TabPageIndicatorAdapter mPagerAdapter;
	private ImageView btnAdd;

	private void initTab(Fragment[] fragments) {
		mPagerAdapter = new TabPageIndicatorAdapter(getChildFragmentManager(), fragments);
		viewPager.setPageMargin(DisplayUtil.sp2px(5));
		viewPager.setPageMarginDrawable(null);
		viewPager.setOffscreenPageLimit(2);
		viewPager.setAdapter(mPagerAdapter);
		tabLayout.setViewPager(viewPager);
		tabLayout.setTextColorResource(R.color.text_gray);
		tabLayout.setIndicatorColorResource(android.R.color.black);
		tabLayout.setDividerColor(Color.TRANSPARENT);
		tabLayout.setTextSelectedColorResource(R.color.text_white);
		tabLayout.setTextSize(getResources().getDimensionPixelSize(R.dimen.text_s18));
//        tabLayout.setTabPaddingLeftRight(0);
		tabLayout.setTextSelectedSize(getResources().getDimensionPixelSize(R.dimen.text_s18));
		tabLayout.setIndicatorHeight(0);
		tabLayout.setUnderlineHeight(0);
		tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 1) {
					btnAdd.setVisibility(View.VISIBLE);
				} else {
					btnAdd.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	/**
	 * 消息监听
	 */
	private EMMessageListener messageListener = new EMMessageListener() {

		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			// notify new message
			for (EMMessage message : messages) {
				IMHelper.getInstance().getNotifier().onNewMsg(message);
			}
			refreshUIWithMessage();
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages) {
			refreshUIWithMessage();
		}

		@Override
		public void onMessageRead(List<EMMessage> messages) {
		}

		@Override
		public void onMessageDelivered(List<EMMessage> message) {
		}

		@Override
		public void onMessageRecalled(List<EMMessage> messages) {
			refreshUIWithMessage();
		}

		@Override
		public void onMessageChanged(EMMessage message, Object change) {
		}
	};

	private EMClientListener clientListener = new EMClientListener() {
		@Override
		public void onMigrate2x(boolean success) {
			Toast.makeText(getActivity(), "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
			if (success) {
				refreshUIWithMessage();
			}
		}
	};



	@Override
	public int getLayoutResourceId() {
		return R.layout.fragment_index;
	}

	@Override
	public void initFragmentView(Bundle savedInstanceState, View mView) {
		initView(mView);
		inviteMessgeDao = new InviteMessgeDao(getActivity());
		conversationListFragment = new ConversationListFragment();
		contactListFragment = new ContactListFragment();
		fragments = new Fragment[]{conversationListFragment, contactListFragment};
		initTab(fragments);
		//register broadcast receiver to receive the change of group from YSHelper
		registerBroadcastReceiver();
		EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
		EMClient.getInstance().addClientListener(clientListener);
		EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	/**
	 * init views
	 */
	private void initView(View view) {
		btnAdd = view.findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				creatDialog();
			}
		});
		tabLayout = view.findViewById(R.id.tablayout);
		viewPager = view.findViewById(R.id.viewpager);
	}

	private void refreshUIWithMessage() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				// refresh unread count
				updateUnreadLabel();
				if (viewPager.getCurrentItem() == 0) {
					// refresh conversation list
					if (conversationListFragment != null) {
						conversationListFragment.refresh();
					}
				}
			}
		});
	}

	private void creatDialog() {
		LayoutInflater factory = LayoutInflater.from(getActivity());//提示框
		final View view = factory.inflate(R.layout.em_activity_new_group, null);//这里必须是final的
		final EditText edit = view.findViewById(R.id.edit_group_name);//获得输入框对象

		new AlertDialog.Builder(getActivity()).setTitle("新建群组")
				.setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivityForResult(new Intent(getActivity(), GroupPickContactsActivity.class)
										.putExtra("groupName", edit.getText().toString())
										.putExtra("title", "创建聊天")
										.putExtra("btnName", "完成")
								, 0);
					}
				}).setNegativeButton("取消", null).create().show();
	}


	public class MyContactListener implements EMContactListener {
		@Override
		public void onContactAdded(String username) {
		}

		@Override
		public void onContactDeleted(final String username) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
							username.equals(ChatActivity.activityInstance.toChatUsername)) {
						String st10 = getResources().getString(R.string.have_you_removed);
						Toast.makeText(getActivity(), ChatActivity.activityInstance.getmGroudId() + st10, Toast.LENGTH_LONG)
								.show();
						ChatActivity.activityInstance.finish();
					}
				}
			});
			updateUnreadAddressLable();
		}

		@Override
		public void onContactInvited(String username, String reason) {
		}

		@Override
		public void onFriendRequestAccepted(String username) {
		}

		@Override
		public void onFriendRequestDeclined(String username) {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (exceptionBuilder != null) {
			exceptionBuilder.create().dismiss();
			exceptionBuilder = null;
			isExceptionDialogShow = false;
		}
		unregisterBroadcastReceiver();

		try {
			getActivity().unregisterReceiver(internalDebugReceiver);
		} catch (Exception e) {
		}

	}

	private AlertDialog.Builder exceptionBuilder;
	private boolean isExceptionDialogShow = false;
	private BroadcastReceiver internalDebugReceiver;

	private BroadcastReceiver broadcastReceiver;
	private LocalBroadcastManager broadcastManager;

	private int getExceptionMessageId(String exceptionType) {
		if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
			return R.string.connect_conflict;
		} else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
			return R.string.em_user_remove;
		} else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
			return R.string.user_forbidden;
		}
		return R.string.Network_error;
	}

	/**
	 * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
	 */
	private void showExceptionDialog(String exceptionType) {
		isExceptionDialogShow = true;
		IMHelper.getInstance().logout(false, null);
		String st = getResources().getString(R.string.Logoff_notification);
		if (!getActivity().isFinishing()) {
			// clear up global variables
			try {
				if (exceptionBuilder == null)
					exceptionBuilder = new AlertDialog.Builder(getActivity());
				exceptionBuilder.setTitle(st);
				exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
				exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						exceptionBuilder = null;
						isExceptionDialogShow = false;
						getActivity().finish();
						Intent intent = new Intent(getActivity(), LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				});
				exceptionBuilder.setCancelable(false);
				exceptionBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
			}
		}
	}

	private void showExceptionDialogFromIntent(Intent intent) {
		EMLog.e(TAG, "showExceptionDialogFromIntent");
		if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
			showExceptionDialog(Constant.ACCOUNT_CONFLICT);
		} else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
			showExceptionDialog(Constant.ACCOUNT_REMOVED);
		} else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
			showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
		} else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
				intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
			getActivity().finish();
			startActivity(new Intent(getActivity(), LoginActivity.class));
		}
	}


	public class MyMultiDeviceListener implements EMMultiDeviceListener {

		@Override
		public void onContactEvent(int event, String target, String ext) {

		}

		@Override
		public void onGroupEvent(int event, String target, final List<String> username) {
			switch (event) {
				case EMMultiDeviceListener.GROUP_LEAVE:
					ChatActivity.activityInstance.finish();
					break;
				default:
					break;
			}
		}
	}

	private void unregisterBroadcastReceiver() {
		broadcastManager.unregisterReceiver(broadcastReceiver);
	}

	private void registerBroadcastReceiver() {
		broadcastManager = LocalBroadcastManager.getInstance(getActivity());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
		intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
		broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				updateUnreadLabel();
				updateUnreadAddressLable();
				if (viewPager.getCurrentItem() == 0) {
					// refresh conversation list
					if (conversationListFragment != null) {
						conversationListFragment.refresh();
					}
				} else if (viewPager.getCurrentItem() == 1) {
					if (contactListFragment != null) {
						contactListFragment.refresh();
					}
				}
				String action = intent.getAction();
				if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
					if (EaseCommonUtils.getTopActivity(getActivity()).equals(GroupsActivity.class.getName())) {
						GroupsActivity.instance.onResume();
					}
				}
			}
		};
		broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
	}

	/**
	 * update unread message count
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
//        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
//            unreadLabel.setVisibility(View.VISIBLE);
//        } else {
//            unreadLabel.setVisibility(View.INVISIBLE);
//        }
	}

	/**
	 * update the total unread count
	 */
	public void updateUnreadAddressLable() {
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
//                if (count > 0) {
//                    unreadAddressLable.setVisibility(View.VISIBLE);
//                } else {
//                    unreadAddressLable.setVisibility(View.INVISIBLE);
//                }
			}
		});

	}


	/**
	 * get unread event notification count, including application, accepted, etc
	 *
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
		return unreadAddressCountTotal;
	}

	/**
	 * get unread message count
	 *
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		return EMClient.getInstance().chatManager().getUnreadMsgsCount();
	}

	private InviteMessgeDao inviteMessgeDao;

	@Override
	public void onResume() {
		super.onResume();

		if (!isConflict && !isCurrentAccountRemoved) {
			updateUnreadLabel();
			updateUnreadAddressLable();
		}

		// unregister this event listener when this activity enters the
		// background
		IMHelper sdkHelper = IMHelper.getInstance();
		sdkHelper.pushActivity(getActivity());

		EMClient.getInstance().chatManager().addMessageListener(messageListener);
	}

	@Override
	public void onStop() {
		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		EMClient.getInstance().removeClientListener(clientListener);
		IMHelper sdkHelper = IMHelper.getInstance();
		sdkHelper.popActivity(getActivity());
		super.onStop();
	}

	private ProgressDialog progressDialog;

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
		final String st2 = getResources().getString(R.string.Failed_to_create_groups);
		if (resultCode == RESULT_OK) {
			final String[] members = data.getStringArrayExtra("newmembers");
			final String groupName = data.getStringExtra("groupName");
			Log.e("chat", members.length + "");

			if (members.length <= 1) {
				Log.e("chat", "single");
				startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", members[0]));
				return;
			}
			//new group
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(st1);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			creatGroup(Constant.CONSTANT_USER_ID, StringUtils.arrayToStrWithComma(members), groupName);
		}
	}

	private void creatGroup(String userId, String members, final String groupName) {
//        Log.e("changle", "members--" + members + "-groupName-" + groupName);
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("members", members);
		params.put("groupName", groupName);
		ApiInterface.addGroup(params, new BaseResponseCallback() {
			@Override
			public void onSuccess(BaseResponse response) {
				if (response.getStatus() == 1) {
					Toast.makeText(getActivity(), "创建群聊:"+groupName+"成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "创建群聊失败", Toast.LENGTH_SHORT).show();
				}
				progressDialog.dismiss();
			}

			@Override
			public void onError(String error) {
				progressDialog.dismiss();
			}
		});
	}
}