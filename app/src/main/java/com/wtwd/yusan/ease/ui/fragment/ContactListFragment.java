package com.wtwd.yusan.ease.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.adapter.ContactListSortAdapter;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.ContactListInfo;
import com.wtwd.yusan.ease.ui.ChatActivity;
import com.wtwd.yusan.ease.ui.GroupsActivity;
import com.wtwd.yusan.ease.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XJM on 2018/4/23.
 */

public class ContactListFragment extends BaseFragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    public static ContactListFragment newInstance() {

        Bundle args = new Bundle();

        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_contact_list;
    }

    private ListView listView;
    private ContactListSortAdapter adapter;

    @Override
    public void initView(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        listView = view.findViewById(R.id.listview);
    }

    @Override
    public void initEvent(View view) {

    }

    @Override
    public void initData() {
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        headerView.findViewById(R.id.group_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.group_item:
                        // 进入群聊列表页面
                        startActivity(new Intent(getActivity(), GroupsActivity.class));
                        break;
                }
            }
        });
        listView.addHeaderView(headerView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactListInfo user = (ContactListInfo) listView.getItemAtPosition(i);
                if (user != null) {
                    String username = user.getUser_name() + "";
                    String userid = user.getUser_id() + "";
                    Log.i("changle", "-username-" + username + "-userid-" + userid );
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userName", username)
                            .putExtra("userId", username));
                }
            }
        });
//        swipeRefreshLayout.setRefreshing(true);
        refresh();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void getContactList(String UserId) {
        List<ContactListInfo> infos = null;
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", UserId);
        ApiInterface.getAddressList(params, new StringCallback() {
            @Override
            public void onSuccess(String response) {
                Log.e("changle", response);
                if (!TextUtils.isEmpty(response) && JsonUtil.getJsonUtil().isRequestSeccess(response)) {
                    List<ContactListInfo> infos = getListObject(response, ContactListInfo.class);
                    adapter = new ContactListSortAdapter(getActivity(), infos, false);
                    listView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

                if(adapter.getCount()>0){
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refresh() {
        getContactList(Constant.CONSTANT_USER_ID);
    }
}
