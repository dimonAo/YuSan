/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wtwd.yusan.ease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.widget.EaseSidebar;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.adapter.ContactListSortAdapter;
import com.wtwd.yusan.ease.adapter.ViewHolder;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.ContactListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupPickContactsActivity extends BaseActivity {
    /**
     * if this is a new group
     */
    protected boolean isCreatingNewGroup;
    //    private PickContactAdapter contactAdapter;
    private ContactListSortAdapter mContactListSortAdapter;
    /**
     * members already in the group
     */
    private List<String> existMembers;
    private ListView listView;
    private String mGroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_group_pick_contacts);

        String title = getIntent().getStringExtra("title");
        String btnName = getIntent().getStringExtra("btnName");
        mGroupName = getIntent().getStringExtra("groupName");

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(btnName)) {
            ((TextView) findViewById(R.id.title)).setText(title);
            ((TextView) findViewById(R.id.btn_ok)).setText(btnName);
        }

        String groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {// create new group
            isCreatingNewGroup = true;
        } else {
            isCreatingNewGroup = false;
            existMembers = getIntent().getStringArrayListExtra("repeatList");
        }
        if (existMembers == null)
            existMembers = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.list);
//        getContactList(Constant.CONSTANT_USER_ID);
        ((EaseSidebar) findViewById(R.id.sidebar)).setListView(listView);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewHolder viewholder = new ViewHolder(view);
                CheckBox checkBox = viewholder.getView(R.id.cb_cantact);
                checkBox.toggle();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // get contact list
        getContactList(Constant.CONSTANT_USER_ID);
    }

    private void getContactList(String UserId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", UserId);

        ApiInterface.getAddressList(params, new StringCallback() {
            @Override
            public void onSuccess(String response) {
                //Log.i("changle", "response=" + response);
                List<ContactListInfo> infos = getListObject(response, ContactListInfo.class);
                if (infos == null) return;
                updateContactList(infos);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(GroupPickContactsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateContactList(List<ContactListInfo> infos) {
        Log.e("changle", "updateContactList--" + infos.size());
        if (!isCreatingNewGroup) {
            for (int i = 0; i < infos.size(); i++) {
                for (String name : existMembers) {
                    if (infos.get(i).getUser_name().equals(name)) {
                        infos.remove(i);
                    }
                }
            }
        }
        mContactListSortAdapter = new ContactListSortAdapter(GroupPickContactsActivity.this, infos, true);
        listView.setAdapter(mContactListSortAdapter);
    }

    /**
     * 保存勾选的members
     *
     * @param v
     */
    public void save(View v) {
        List<String> var = getToBeAddMembers();
        setResult(RESULT_OK, new Intent().putExtra("groupName", mGroupName)
                .putExtra("newmembers", var.toArray(new String[var.size()])));
        finish();
    }

    /**
     * 获取勾选的members
     *
     * @return
     */
    private List<String> getToBeAddMembers() {
        List<String> members = new ArrayList<String>();
        int length = mContactListSortAdapter.isCheckedArray.length;
        for (int i = 0; i < length; i++) {
            ContactListInfo info = (ContactListInfo) mContactListSortAdapter.getItem(i);
            Long userID = info.getUser_id();
            if (mContactListSortAdapter.isCheckedArray[i] && !existMembers.contains(userID + "")) {
                members.add(userID + "");
            }
        }
        return members;
    }

    public void back(View view) {
        finish();
    }
}
