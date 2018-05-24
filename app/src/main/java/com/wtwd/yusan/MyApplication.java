package com.wtwd.yusan;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobApplication;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.util.swipe.ActivityLifeHelper;
import com.wtwd.yusan.entity.operation.DaoUtils;
import com.wtwd.yusan.util.Constans;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * time:2018/4/23
 * Created by w77996
 */

public class MyApplication extends MobApplication {
    private static MyApplication mInstance;
    public static IWXAPI api;

    public static Context applicationContext;
   // private static IMApplication instance;
    // login user name
    public final String PREF_USERNAME = "username";

    public static String getUserId(){
        return "1";
    }
    public static void setUserId(){
    }

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";
    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        applicationContext = this;
        //instance = this;
       /* OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(3000L, TimeUnit.MILLISECONDS)
                .readTimeout(3000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);*/
        //init demo helper
        IMHelper.getInstance().init(applicationContext);
        registerActivityLifecycleCallbacks(ActivityLifeHelper.instance());
        mInstance = this;
        initWeiXin();
        DaoUtils.init(this);
    }

    private void initWeiXin() {
//        sApi = WXEntryActivity.initWeiXin(this, AppConst.WEIXIN_APP_ID);
        api = WXAPIFactory.createWXAPI(this, Constans.WX_APP_ID, true);
        api.registerApp(Constans.WX_APP_ID);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static Context getContext() {
        return mInstance;
    }
}
