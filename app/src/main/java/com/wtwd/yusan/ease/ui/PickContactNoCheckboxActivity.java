package com.wtwd.yusan.ease.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.easeui.widget.EaseSidebar;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.adapter.ContactListSortAdapter;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.ContactListInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("Registered")
public class PickContactNoCheckboxActivity extends BaseActivity {

    protected ContactListSortAdapter contactAdapter;
    private List<ContactListInfo> contactList;
    private ListView listView;
    private String forward_msg_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_pick_contact_no_checkbox);
        listView = (ListView) findViewById(R.id.list);
        EaseSidebar sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        sidebar.setListView(listView);
        forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        // get contactlist
        getContactList(Constant.CONSTANT_USER_ID);
        // set adapter

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(position);
            }
        });

    }

    protected void onListItemClick(int position) {
        String name = contactList.get(position).getUser_name();
        if (TextUtils.isEmpty(name))
            return;
        try {
            ChatActivity.activityInstance.finish();
        } catch (Exception e) {
        }
        Intent intent = new Intent(PickContactNoCheckboxActivity.this, ChatActivity.class);
        // it is single chat
        intent.putExtra("userId", contactList.get(position)
                .getUser_name());
        intent.putExtra("forward_msg_id", forward_msg_id);
        startActivity(intent);
        finish();
    }

    public void back(View view) {
        finish();
    }


    private void getContactList(String UserId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", UserId);

        ApiInterface.getAddressList(params, new StringCallback() {
            @Override
            public void onSuccess(String response) {
                //Log.i("changle", "response=" + response);
                contactList = getListObject(response, ContactListInfo.class);
                if (contactList == null) return;
                updateContactList(contactList);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(PickContactNoCheckboxActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateContactList(List<ContactListInfo> infos) {
        Log.e("changle", "updateContactList--" + infos.size());
        contactAdapter = new ContactListSortAdapter(PickContactNoCheckboxActivity.this, infos, true);
//		contactAdapter = new ContactListSortAdapter(this, R.layout.ease_row_contact, contactList);
        listView.setAdapter(contactAdapter);
    }


}