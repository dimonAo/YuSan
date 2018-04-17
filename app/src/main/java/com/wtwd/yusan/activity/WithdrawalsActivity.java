package com.wtwd.yusan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.base.CommonToolBarActivity;

/**
 * 提现
 * time:2018/4/16
 * Created by w77996
 */

public class WithdrawalsActivity extends CommonToolBarActivity implements View.OnClickListener{
    /**
     * 微信支付账号设置
     */
    LinearLayout lin_withdrawals_wechat_set;

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
        addListener();
    }

    private void addListener() {
        lin_withdrawals_wechat_set.setOnClickListener(this);
    }

    private void initView() {
        text_tool_bar_title.setText("提现");
        lin_withdrawals_wechat_set = findViewById(R.id.lin_withdrawals_wechat_set);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.acitivity_withdrawals;
    }

    @Override
    public View getSnackView() {
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_withdrawals_wechat_set:
                break;
        }
    }
}
