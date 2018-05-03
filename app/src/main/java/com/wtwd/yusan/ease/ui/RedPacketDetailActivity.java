package com.wtwd.yusan.ease.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.StringCallback;
import com.wtwd.yusan.ease.net.response.RedPacketInfo;
import com.wtwd.yusan.ease.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;


public class RedPacketDetailActivity extends BaseActivity {
    private TextView mTvAmount;
    private String mPacketId;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_envelopes_detail);
        mTvAmount = (TextView) findViewById(R.id.tv_amount);
        mPacketId = getIntent().getStringExtra("packet_id");
        Log.i("IMDemo", "userId:" + Constant.CONSTANT_USER_ID + "--packet_id:" + mPacketId);
        getRedPacket(Constant.CONSTANT_USER_ID, mPacketId);
    }

    /**
     * 抢红包
     *
     * @param userId
     * @param redpacketId
     */
    private void getRedPacket(String userId, String redpacketId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("redpacketId", redpacketId);
        ApiInterface.getRedPacket(params, new StringCallback() {
            @Override
            public void onSuccess(String response) {
                if (JsonUtil.getJsonUtil().isRequestSeccess(response)) {
                    Log.i("IMDemo", "getRedPacket response:" + response);
                    RedPacketInfo info = getListObject(response, RedPacketInfo.class).get(0);
                    mTvAmount.setText(info.getMoney().toString().trim());
                }
            }

            @Override
            public void onError(String error) {
                Log.i("IMDemo", "getRedPacket-error-" + error);
                Toast.makeText(RedPacketDetailActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void save(View view) {
        setResult(RESULT_OK);
        finish();
    }

    public void back(View view) {
        finish();
    }
}
