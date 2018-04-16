package com.wtwd.yusan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.GlideCacheUtil;
import com.wtwd.yusan.util.Utils;

/**
 * time:2018/4/11
 * Created by w77996
 */

public class SettingActivity extends CommonToolBarActivity implements  View.OnClickListener{

    private static  final String TAG = "SettingActivity";

    /**
     * 清除缓存
     */
    LinearLayout lin_setting_clear_temp;
    /**
     * 退出登录
     */
    LinearLayout lin_setting_loginout;
    /**
     * 关于我们
     */
    LinearLayout lin_setting_about_us;
    /**
     * 缓存大小
     */
    TextView tv_setting_temp_size;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_setting;
    }



    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
        getTemp();
    }


    @Override
    public View getSnackView() {
        return null;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        text_tool_bar_title.setText("设置");
        lin_setting_clear_temp = (LinearLayout)findViewById(R.id.lin_setting_clear_temp);
        lin_setting_loginout = (LinearLayout)findViewById(R.id.lin_setting_loginout);
        lin_setting_about_us = (LinearLayout)findViewById(R.id.lin_setting_about_us);
        tv_setting_temp_size = (TextView)findViewById(R.id.tv_setting_temp_size);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lin_setting_clear_temp:
                clearTemp();
                break;
            case R.id.lin_setting_loginout:
                loginOut();
                break;
            case R.id.lin_setting_about_us:
                Intent aboutUsActivityIntent = new Intent(SettingActivity.this,AboutUsActivity.class);
                startActivity(aboutUsActivityIntent);
                break;
        }
    }

    /**
     * 获取缓存
     */
    private void getTemp() {
        // TODO: 2018/4/11 获取缓存
        tv_setting_temp_size.setText(GlideCacheUtil.getInstance().getCacheSize(this));
        Log.e(TAG, GlideCacheUtil.getInstance().getCacheSize(this)+"M");

    }

    /**
     * 清除缓存
     */
    private void clearTemp() {
        // TODO: 2018/4/11 清除缓存

        showToast("清除成功");
    }

    /**
     * 退出登录
     */
    private void loginOut() {

    }




}
