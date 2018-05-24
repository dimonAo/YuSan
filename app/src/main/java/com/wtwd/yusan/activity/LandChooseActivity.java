package com.wtwd.yusan.activity;

import android.content.Intent;
<<<<<<< HEAD
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
=======
>>>>>>> 104bf428540af71a6d255d6234551290aeefcffa
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.wtwd.yusan.MyApplication;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.db.DemoDBManager;
import com.wtwd.yusan.entity.UserEntity;
import com.wtwd.yusan.entity.WxUserEntity;
import com.wtwd.yusan.entity.operation.DaoUtils;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.wxapi.WXEntryActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class LandChooseActivity extends CommonToolBarActivity implements View.OnClickListener {

    private ImageView img_choose_wechat_land;
    private ImageView img_choose_phone_land;

    private ImageView test;

    private boolean autoLogin = false;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_land_choose;
    }

    @Override
    public View getSnackView() {
        return null;
    }

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
<<<<<<< HEAD
//        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }
=======
        // enter the main activity if already logged in
      /*  UserEntity userEntity = DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(this).getUserId());
        if(null == userEntity&&IMHelper.getInstance().isLoggedIn()){

        }else if(IMHelper.getInstance().isLoggedIn()&& null != userEntity){

        }*/


      /*  if (IMHelper.getInstance().isLoggedIn()) {

            autoLogin = true;
            Log.e("LandChooseActivity", userEntity.getUser_id()+"");
            Log.e("LandChooseActivity", userEntity.getUser_name());
            Constant.CONSTANT_USER_ID = userEntity.getUser_id()+"";
            Constant.CONSTANT_USER_NAME = userEntity.getUser_name();
            //login(userEntity.getUser_name());
            readyGo(MainActivity.class);
            finish();

            return;
        }*/
>>>>>>> 104bf428540af71a6d255d6234551290aeefcffa
        img_choose_wechat_land = (ImageView) findViewById(R.id.img_choose_wechat_land);
        img_choose_phone_land = (ImageView) findViewById(R.id.img_choose_phone_land);
        test = (ImageView) findViewById(R.id.test);
        addListener();
        UserEntity userEntity = DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(this).getUserId());
        if(null == userEntity&&!IMHelper.getInstance().isLoggedIn()){

        }else{
            Log.e("LandChooseActivity", userEntity.getUser_id()+"");
            Log.e("LandChooseActivity", userEntity.getUser_name());
            Constant.CONSTANT_USER_ID = userEntity.getUser_id()+"";
            Constant.CONSTANT_USER_NAME = userEntity.getUser_name();
                  //  login(userEntity.getUser_name());
            readyGo(MainActivity.class);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        Bundle bundle = getIntent().getExtras();
        if(null != bundle){

            // TODO: 2018/5/9 请求服务器是否是第一次登录
            WxUserEntity wxUserEntity = bundle.getParcelable("user");
            Log.e(TAG,wxUserEntity.toString());
            loginUserExist(wxUserEntity.getOpenid());

        }
    }

    private void addListener() {
        img_choose_wechat_land.setOnClickListener(this);
        img_choose_phone_land.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.img_choose_phone_land == v.getId()) {
            readyGo(LandPhoneActivity.class);
        } else if (R.id.img_choose_wechat_land == v.getId()) {
            //微信登录
            WXEntryActivity.loginWeixin(this, MyApplication.api);

//            Glide.with(this)
//                    .load("https://img-blog.csdn.net/20170428175627934?watermark" +
//                            "/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveWVjaGFvYQ==/font/5a6L5L2T" +
//                            "/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center")
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(test);

        }

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       Log.e(TAG,"LandChoose----------------------onActivityResult");
        if(Constans.LOGIN_WECHAT == resultCode){
            String wxResult = Pref.getInstance(this).getWxEntity();
            if(null != wxResult && !"".equals(wxResult)){
               Bundle bundle = new Bundle();
                //readyGo();
//               WxUserEntity wxUserEntity = GsonUtils.GsonToBean(wxResult, WxUserEntity.class);
//                loginUserExist(wxUserEntity.getOpenid());
            }else{
                showToast("微信登录失败");
            }
        }



    }

    /**
     * login
     *
     * @param
     */
    public void login(String username) {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
     /*   String currentUsername = usernameEditText.getText().toString().trim();
        String currentPassword = passwordEditText.getText().toString().trim();*/


       /* if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }*/

       /* //progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "EMClient.getInstance().onCancel");
               // progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();*/

        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        DemoDBManager.getInstance().closeDB();

        // reset current user name before login
        IMHelper.getInstance().setCurrentUserName(username);

        final long start = System.currentTimeMillis();
        // call login method
        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(username, "123456", new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "login: onSuccess");

                // ** manually load all local groups and conversation
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                // update current user's display name for APNs
                boolean updatenick = EMClient.getInstance().pushManager().updatePushNickname(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }

              /*  if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }*/
                // get user's info (this should be get from App's server or 3rd party service)
                IMHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                Intent intent = new Intent(LandChooseActivity.this, MainActivity.class);
                readyGoForNewTask(MainActivity.class);

               // finish();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG, "login: onError: " + code);
              /*  if (!progressShow) {
                    return;
                }*/
                runOnUiThread(new Runnable() {
                    public void run() {
                        //pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loginUserExist(final String openId) {
        Log.e(TAG,"login exist");
        OkHttpUtils.post()
                .url(Constans.LOGIN_USER_EXIST)
                .addParams("openId", openId)
                .addParams("type", "2")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG,"error"+id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "land phont : " + response);
                        try {
                            JSONObject mLoginResponse = new JSONObject(response);

                            int mStatus = mLoginResponse.optInt("status");

                            if (Constans.REQUEST_SUCCESS == mStatus) {
                                String mObject = mLoginResponse.optString("object");

                                JSONObject mObjectJson = new JSONObject(mObject);

                                boolean mIsFirst = mObjectJson.optBoolean("isFirst");
                                if (mIsFirst) {
                                    //第一次登录，需要跳转到ModifyUserActivity界面
                                    //phone 手机号码
                                    //isFirst 是否第一次登录
                                    //isPhone 是否是手机号码
                                    Bundle bundle = new Bundle();
                                    bundle.putString("account", openId);
                                    bundle.putBoolean("isFirst", true);
                                    bundle.putBoolean("isPhone", false);
                                    readyGo(ModifyUserActivity.class, bundle);
                                    showToast(getString(R.string.land_phone_verify_code_success));
                                    finish();
                                } else {
                                    //直接登录到主界面，需要解析UserEntity对象，存在本地
                                    UserEntity mUserEntity = GsonUtils.GsonToBean(mObjectJson.optString("user"), UserEntity.class);
                                    Log.e(TAG, "mUserEntity : " + mUserEntity.toString());
                                    mPref.setUserId(mUserEntity.getUser_id());
                                    //User插入数据库
                                    UserEntity mOldEn = DaoUtils.getUserManager().queryUserForUserId(mUserEntity.getUser_id());

                                    if ((null != mOldEn) && ((mUserEntity.getUser_id()).equals(mOldEn.getUser_id()))) {
                                        DaoUtils.getUserManager().updateObject(mUserEntity);
                                    } else {
                                        DaoUtils.getUserManager().insertObject(mUserEntity);
                                    }
                                    login(mUserEntity.getUser_name());
                                }
                                mPref.setIsPhone(true);
                            } else {
                                showToast(getErrorString(mStatus));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
