package com.wtwd.yusan.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wtwd.yusan.util.Constans;

/**
 * time:2018/4/19
 * Created by w77996
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册
        mIWXAPI = WXAPIFactory.createWXAPI(this, Constans.WX_APP_ID);
        mIWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mIWXAPI.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

        int code = baseResp.errCode;
        Log.e(TAG,"返回code:"+code);
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

            if (baseResp.errCode == 0) {
                Log.e(TAG,"支付成功");
                finish();
            } else if (baseResp.errCode == -2) {
                Toast.makeText(this, "您已取消付款!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            finish();
        }
    }
        //EvenB


}
