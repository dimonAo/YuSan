package com.wtwd.yusan.ease.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.net.ApiInterface;
import com.wtwd.yusan.ease.net.callback.BaseResponseCallback;
import com.wtwd.yusan.ease.net.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ChangLe on 2018/4/25.
 */
public class MissionReceiveActivity extends BaseActivity {
    private TextView mTvAmount;
    private String mMissionId;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_mission_receive);
        mTvAmount = (TextView) findViewById(R.id.tv_content);
        mMissionId = getIntent().getStringExtra("mission_id");
        Log.i("IMDemo", "mMissionId:" + mMissionId);
        acceptMission(Constant.CONSTANT_USER_ID, mMissionId);
    }

    /**
     * 领取任务
     *
     * @param userId
     * @param missionId
     */
    private void acceptMission(String userId, String missionId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("missionId", missionId);
        ApiInterface.acceptMission(params, new BaseResponseCallback() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getStatus() == 1) {
                    mTvAmount.setText("领取成功");
                    Toast.makeText(MissionReceiveActivity.this, "领取成功", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Log.i("changle", "acceptMission-error-" + error);
                mTvAmount.setText("领取失败");
                Toast.makeText(MissionReceiveActivity.this, "领取失败"+error, Toast.LENGTH_SHORT).show();
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