package com.wtwd.yusan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.Pref;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import okhttp3.Call;

/**
 * time:2018/4/18
 * Created by w77996
 */

public class MyPacketActivity extends CommonToolBarActivity implements  View.OnClickListener{

    LinearLayout lin_mypacket_recharge;

    LinearLayout lin_mypacket_withdrawals;

    TextView tv_mypacket_detail;

    TextView tv_mypacket_yue;

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
            initView();
    }

    private void initView() {
        text_tool_bar_title.setText(R.string.packet_title);
        lin_mypacket_recharge = findViewById(R.id.lin_mypacket_recharge);
        lin_mypacket_withdrawals = findViewById(R.id.lin_mypacket_withdrawals);
        tv_mypacket_detail = findViewById(R.id.tv_mypacket_detail);
        tv_mypacket_yue = findViewById(R.id.tv_mypacket_yue);
        addListener();
        getUserBalance();
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

    /**
     * 获取余额
     */
    private void getUserBalance(){
        OkHttpUtils.get()
                .url(Constans.GET_BALANCE)
                .addParams("userId", Pref.getInstance(this).getUserId()+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG,response.toString());
                        DecimalFormat df   = new DecimalFormat("######0.00");
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int status = jsonObject.getInt("status");
                            if(1 == status ){
                                JSONObject result = jsonObject.getJSONObject("object");
                                Double balance = Double.parseDouble(result.getString("balance"));
                                String dfBalance = df.format(balance);
                                tv_mypacket_yue.setText(dfBalance);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // tv_yue.setText();

                    }
                });


    }
}
