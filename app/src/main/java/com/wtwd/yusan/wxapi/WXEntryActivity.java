package com.wtwd.yusan.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wtwd.yusan.MyApplication;
import com.wtwd.yusan.activity.LandChooseActivity;
import com.wtwd.yusan.activity.ModifyUserActivity;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.entity.AccessTokenEntity;
import com.wtwd.yusan.entity.UserEntity;
import com.wtwd.yusan.entity.WxUserEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    //private IWXAPI mIWXAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WXEntryActivity", "onCreate");
       // mIWXAPI = WXAPIFactory.createWXAPI(this, Constans.WX_APP_ID);
        MyApplication.api.handleIntent(getIntent(), this);

    }

    /**
     * 登录微信
     *
     * @param api 微信服务api
     */
    public static void loginWeixin(Context context, IWXAPI api) {
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context.getApplicationContext(), "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";

        Log.e("req.toString","req.toString : ---> "+req.toString());
        Log.e("api","api : ---> "+api);

        api.sendReq(req);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("WXEntryActivity", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        setIntent(intent);
       MyApplication.api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        Log.e("WXEntryActivity", "onReq");
    }


    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Log.e("WXEntryActivity", "onResp");
        // TODO Auto-generated method stub
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            // 用户同意
            Log.e("WXEntryActivity", "onResp errCode" + resp .errCode);
            Log.e("WXEntryActivity", "onResp errStr" + resp.errStr);
            Log.e("WXEntryActivity", "onResp openId" + resp.openId);

            String code = ((SendAuth.Resp) resp).code;

            // 从手机本地获取存储的授权口令信息，判断是否存在access_token，不存在请求获取，存在就判断是否过期
            String accessToken = Pref.getInstance(this).getLoginAccessToken();
            String openid = "";
            Log.e("tag none",accessToken);
            if (!"none".equals(accessToken)) {

                // 有access_token，判断是否过期有效
                isExpireAccessToken(accessToken, openid);
            } else {
                // 没有access_token
                getAccessToken(code);
            }

        } else {
            Log.e("errCode", "errCode : ---> " + resp.errCode);
            Log.e("errStr", "errStr : --->" + resp.errStr);

        }
    }

    /**
     * 判断accesstoken是否过期
     *
     * @param accessToken token
     * @param openid      授权用户唯一标识
     */
    private void isExpireAccessToken(final String accessToken, final String openid) {
        String url = "https://api.weixin.qq.com/sns/auth?" +
                "access_token=" + accessToken +
                "&openid=" + openid;
        Log.e("isExpireAccessToken",url);
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("isExpireAccessToken","error"+id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("isExpireAccessToken",response);

                        if (validateSuccess(response)) {
                            // accessToken没有过期，获取用户信息
                            getUserInfo(accessToken, openid);
                        } else {
                            // 过期了，使用refresh_token来刷新accesstoken
                            refreshAccessToken();
                        }
                    }
                });

    }


    private void refreshAccessToken() {
        // 从本地获取以存储的refresh_token
        final String refreshToken = "";
        if (TextUtils.isEmpty(refreshToken)) {
            return;
        }
        // 拼装刷新access_token的url请求地址
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?" +
                "appid=" + Constans.WX_APP_ID +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;


        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        loginWeixin(WXEntryActivity.this.getApplicationContext(), MyApplication.api);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                        Log.e("refreshAccessToken",response);
                        processGetAccessTokenResult(response);
                    }
                });

        // 请求执行
//        httpRequest(url, new ApiCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                Logger.e("refreshAccessToken: " + response);
//                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
//                processGetAccessTokenResult(response);
//            }
//            @Override
//            public void onError(int errorCode, final String errorMsg) {
//                Logger.e(errorMsg);
//                showMessage("错误信息: " + errorMsg);
//                // 重新请求授权
//                loginWeixin(WXEntryActivity.this.getApplicationContext(),        GeneralAppliction.sApi);
//            }
//            @Override
//            public void onFailure(IOException e) {
//                Logger.e(e.getMessage());
//                showMessage("登录失败");
//                // 重新请求授权
//                loginWeixin(WXEntryActivity.this.getApplicationContext(),        GeneralAppliction.sApi);
//            }
//        });
    }


    private void getUserInfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token +
                "&openid=" + openid;

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("getUserInfo",response);
                        Pref.getInstance(getApplicationContext()).setWxEntity(response);
                        WxUserEntity userEntity = GsonUtils.GsonToBean(response,WxUserEntity.class);
                        Intent intent = new Intent(WXEntryActivity.this, LandChooseActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user",userEntity);
                        intent.putExtras(bundle);
                        startActivity(intent);
//                        setResult(Constans.LOGIN_WECHAT);
                        Log.e("user",userEntity.toString());
                        finish();
                    }
                });
//        httpRequest(url, new ApiCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                // 解析获取的用户信息
//                WXUserInfo userInfo = mGson.fromJson(response, WXUserInfo.class);
//                Logger.e("用户信息获取结果：" + userInfo.toString());
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMsg) {
//                showMessage("错误信息: " + errorMsg);
//            }
//
//            @Override
//            public void onFailure(IOException e) {
//                showMessage("获取用户信息失败");
//            }
//        });
    }

    /**
     * 获取授权口令
     */
    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + Constans.WX_APP_ID +
                "&secret=" + Constans.WX_APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        Log.e("getAccessToken",url);
        // 网络请求获取access_token
        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                        Log.e("getAccessToken",response);
                        processGetAccessTokenResult(response);

//                        AccessTokenEntity mEn = GsonUtils.GsonToBean(response, AccessTokenEntity.class);
                    }
                });

    }


    /**
     * 处理获取的授权信息结果
     *
     * @param response 授权信息结果
     */
    private void processGetAccessTokenResult(String response) {
        // 验证获取授权口令返回的信息是否成功
        if (validateSuccess(response)) {
            // 使用Gson解析返回的授权口令信息
            AccessTokenEntity tokenInfo = GsonUtils.GsonToBean(response, AccessTokenEntity.class);
            // 保存信息到手机本地
//            saveAccessInfotoLocation(tokenInfo);
            // 获取用户信息
            getUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        } else {
            // 授权口令获取失败，解析返回错误信息
            ErrorCode wxErrorInfo = GsonUtils.GsonToBean(response, ErrorCode.class);
            // 提示错误信息
//            showMessage("错误信息: " + wxErrorInfo.getErrmsg());
        }
    }

    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        String errFlag = "errmsg";
//        return (errFlag.contains(response) && !"ok".equals(response)) || (!"errcode".contains(response) && !errFlag.contains(response));
        return (!response.contains(errFlag));
    }


    private class ErrorCode {
        private int errorcode;
        private String errmsg;

        public int getErrorcode() {
            return errorcode;
        }

        public void setErrorcode(int errorcode) {
            this.errorcode = errorcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }
    }



}