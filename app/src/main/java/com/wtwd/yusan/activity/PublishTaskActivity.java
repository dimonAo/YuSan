package com.wtwd.yusan.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.DialogUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PublishTaskActivity extends CommonToolBarActivity implements View.OnClickListener {
    private RelativeLayout relative_sex;
    private TextView text_sex;

    private RelativeLayout relative_location;
    private TextView text_province;


    private Dialog mDialog;
    private List<String> mSexChoose = new ArrayList<>();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_publish_task;
    }

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        text_tool_bar_title.setText("发布");
        getDate();
        initView();
        addListener();
    }

    private void getDate() {
        String[] mSexs = getResources().getStringArray(R.array.task_sex);
        for (int i = 0; i < mSexs.length; i++) {
            mSexChoose.add(i, mSexs[i]);
        }
    }


    private void initView() {
        mDialog = new Dialog(this, R.style.MyCommonDialog);

        relative_sex = (RelativeLayout) findViewById(R.id.relative_sex);
        text_sex = (TextView) findViewById(R.id.text_sex);

        relative_location = (RelativeLayout) findViewById(R.id.relative_location);
        text_province = (TextView) findViewById(R.id.text_province);
    }

    private void addListener() {
        relative_sex.setOnClickListener(this);
        relative_location.setOnClickListener(this);
    }

    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_sex:
                DialogUtil.dialogShowPublishSex(this, mDialog, mSexChoose, text_sex);
                break;

            case R.id.relative_location:
                Dialog mDialog1 = new Dialog(this,R.style.MyCommonDialog);
                DialogUtil.dialogShowProvince(this, mDialog1, text_province);
                break;
        }
    }
}
