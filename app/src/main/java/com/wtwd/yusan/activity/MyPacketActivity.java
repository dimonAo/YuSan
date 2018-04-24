package com.wtwd.yusan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;

/**
 * time:2018/4/18
 * Created by w77996
 */

public class MyPacketActivity extends CommonToolBarActivity implements  View.OnClickListener{

    LinearLayout lin_mypacket_recharge;

    LinearLayout lin_mypacket_withdrawals;

    TextView tv_mypacket_detail;

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
            initView();
    }

    private void initView() {
        text_tool_bar_title.setText("钱包");
        lin_mypacket_recharge = findViewById(R.id.lin_mypacket_recharge);
        lin_mypacket_withdrawals = findViewById(R.id.lin_mypacket_withdrawals);
        tv_mypacket_detail = findViewById(R.id.tv_mypacket_detail);
        addListener();
    }

    /**
     * 监听器
     */
    private void addListener() {
        lin_mypacket_recharge.setOnClickListener(this);
        lin_mypacket_withdrawals.setOnClickListener(this);
        tv_mypacket_detail.setOnClickListener(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_mypacket;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_mypacket_withdrawals:
               /* Intent withdrawalsItnent = new Intent(this,WithdrawalsActivity.class);
                startActivity(withdrawalsItnent);*/
                readyGo(WithdrawalsActivity.class);
                break;
            case R.id.lin_mypacket_recharge:
               /* Intent rechargeIntent = new Intent(this,RechargeActivity.class);
                startActivity(rechargeIntent);*/
                readyGo(RechargeActivity.class);
                break;
            case R.id.tv_mypacket_detail:
               /* Intent mypacketDetailIntent = new Intent(this,PacketDetailActivity.class);
                startActivity(mypacketDetailIntent);*/
               readyGo(PacketDetailActivity.class);
                break;
        }
    }
}
