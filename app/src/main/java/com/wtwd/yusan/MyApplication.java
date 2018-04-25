package com.wtwd.yusan;

import android.content.Context;

import com.mob.MobApplication;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.wxapi.WXEntryActivity;

/**
 * time:2018/4/23
 * Created by w77996
 */

public class MyApplication extends MobApplication {
    private static MyApplication mInstance;
    public static IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initWeiXin();
    }

    private void initWeiXin() {
//        sApi = WXEntryActivity.initWeiXin(this, AppConst.WEIXIN_APP_ID);
        api = WXAPIFactory.createWXAPI(this, Constans.WX_APP_ID, true);
        api.registerApp(Constans.WX_APP_ID);
    }

    public static Context getContext() {
        return mInstance;
    }
}
