package com.wtwd.yusan.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.dialog.DialogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * time:2018/4/18
 * Created by w77996
 */

public class ModifyUserActivity extends CommonToolBarActivity implements View.OnClickListener {

    private RelativeLayout relative_head;
    private RelativeLayout relative_name;
    private RelativeLayout relative_sex;
    private RelativeLayout relative_height;

    private TextView tv_modifyuser_sex;
    private TextView tv_modifyuser_height;
    private TextView text_user_nick;

    //    private String[] mModifySex;
    private List<String> mModifySexList = new ArrayList<>();
    private List<String> mModifyHeight = new ArrayList<>();
    private Dialog mSelectorDialog;

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
    }

    private void initView() {
        mSelectorDialog = new Dialog(this, R.style.MyCommonDialog);

        String[] mModifySex = getResources().getStringArray(R.array.modify_info);
        mModifySexList.addAll(Arrays.asList(mModifySex));

        getHeightData();

        text_tool_bar_title.setText("完善个人信息");
        relative_head = (RelativeLayout) findViewById(R.id.relative_head);
        relative_name = (RelativeLayout) findViewById(R.id.relative_name);
        relative_sex = (RelativeLayout) findViewById(R.id.relative_sex);
        relative_height = (RelativeLayout) findViewById(R.id.relative_height);

        tv_modifyuser_sex = (TextView) findViewById(R.id.tv_modifyuser_sex);
        tv_modifyuser_height = (TextView) findViewById(R.id.tv_modifyuser_height);
        text_user_nick = (TextView) findViewById(R.id.text_user_nick);

        addListener();
    }


    /**
     * 身高数据（20cm-239cm）
     */
    private void addListener() {
        relative_head.setOnClickListener(this);
        relative_name.setOnClickListener(this);
        relative_sex.setOnClickListener(this);
        relative_height.setOnClickListener(this);

    }

    private void getHeightData() {
        for (int i = 0; i < 220; i++) {
            mModifyHeight.add((20 + i) + "");
        }

    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_modifyuser;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_head:


                break;

            case R.id.relative_name:
                readyGoForResult(NickActivity.class, 101);
                break;

            case R.id.relative_height:
                Dialog mDialog = new Dialog(this, R.style.MyCommonDialog);
                DialogUtil.dialogShowPublishSex(ModifyUserActivity.this, mDialog, mModifyHeight, tv_modifyuser_height);
                break;

            case R.id.relative_sex:
                DialogUtil.dialogShowPublishSex(ModifyUserActivity.this, mSelectorDialog, mModifySexList, tv_modifyuser_sex);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (101 == requestCode) {
            if (100 == resultCode) {
                String nick = data.getStringExtra("nick_name");
                text_user_nick.setText(nick);
            }
        }


    }
}
