package com.wtwd.yusan.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.wtwd.yusan.MyApplication;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.db.DemoDBManager;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.entity.UserEntity;
import com.wtwd.yusan.entity.operation.DaoUtils;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;

import static com.github.ybq.android.spinkit.animation.AnimationUtils.start;

public class LandPhoneActivity extends CommonToolBarActivity implements View.OnClickListener {
    private EditText edit_phone;
    private EditText edit_verify;

    private Button btn_get_verification_code;
    private Button btn_land;

    private TimeCount time;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            switch (event) {
                case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {

                        //验证码验证成功，将手机号发送到服务器

//                        readyGo(MainActivity.class);
//                        Intent intent = new Intent(LandPhoneActivity.this, MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);

                        loginUserExist(getPhone());

                    } else {
                        showToast(getString(R.string.land_phone_verify_code_error));
                    }
                    break;

                case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {

                        showToast(getString(R.string.land_phone_get_verify_code_success));
                    } else {
                        showToast(getString(R.string.land_phone_get_verify_code_error));
                    }
                    break;
            }
        }
    };

    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_get_verification_code.setText(String.format(getResources().getString(R.string.register_retest), (millisUntilFinished / 1000)));
            btn_get_verification_code.setTextColor(Color.RED);
            btn_get_verification_code.setEnabled(false);
        }

        @Override
        public void onFinish() {
            btn_get_verification_code.setText(getString(R.string.register_verify_code_text));
            btn_get_verification_code.setTextColor(ContextCompat.getColor(LandPhoneActivity.this, R.color.colorWhite));
            btn_get_verification_code.setEnabled(true);
        }
    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_land_phone;
    }

    @Override
    public View getSnackView() {
        return null;
    }


    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {

        initToolBar();
        initView();
        initEventHandle();
        //initPremission();

    }

  /*  private void initPremission() {
        // 在Activity：
        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_CONTACTS,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .callback(listener)
                .start();
    }

    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if(requestCode == 200) {
                // TODO ...
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if(requestCode == 200) {
                // TODO ...
            }
        }
    };*/
    private void initToolBar() {
        text_tool_bar_title.setText(R.string.land_title);
    }

    private void initView() {
        int mCodeTimeCount = 2 * 60 * 1000;
        time = new TimeCount(mCodeTimeCount, 1000);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_verify = (EditText) findViewById(R.id.edit_verify);
        btn_get_verification_code = (Button) findViewById(R.id.btn_get_verification_code);
        btn_land = (Button) findViewById(R.id.btn_land);
        btn_get_verification_code.setText(R.string.land_get_verify_code);
        addListener();
    }

    private String getPhone() {
        return edit_phone.getText().toString();
    }

    private String getVerification() {
        return edit_verify.getText().toString();
    }

    private void addListener() {
        btn_land.setOnClickListener(this);
        btn_get_verification_code.setOnClickListener(this);
    }

    /**
     * 判断输入手机号码格式是否正确
     *
     * @return
     */
    private boolean checkPhone() {

        if (TextUtils.isEmpty(getPhone())) {
            showToast(getString(R.string.land_input_phone));
            return false;
        }

        if (!Utils.isMobileNO(getPhone())) {
            showToast(getString(R.string.land_input_correct_number));
            return false;
        }

        return true;
    }

    /**
     * 判断是否输入验证码
     *
     * @return
     */
    private boolean checkVerification() {
        if (TextUtils.isEmpty(getVerification())) {
            showToast(getString(R.string.land_input_verify_code));
            return false;
        }

        return true;
    }


    @Override
    public void onClick(View v) {
        if (R.id.btn_get_verification_code == v.getId()) {
            //获取验证码
            if (checkPhone()) {
                time.start();
                SMSSDK.getVerificationCode("86", getPhone());
            }
        } else if (R.id.btn_land == v.getId()) {
            //验证验证码登录
//            if (checkPhone() && checkVerification()) {
//                SMSSDK.submitVerificationCode("86", getPhone(), getVerification());
//            }
//            Intent intent = new Intent(LandPhoneActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

            loginUserExist(getPhone());

        }
    }

    public void initEventHandle() {
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    private void loginUserExist(String phone) {
        OkHttpUtils.get()
                .url(Constans.LOGIN_USER_EXIST)
                .addParams("phone", phone)
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

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
                                    bundle.putString("account", getPhone());
                                    bundle.putBoolean("isFirst", true);
                                    bundle.putBoolean("isPhone", true);
                                    readyGo(ModifyUserActivity.class, bundle);
                                    showToast(getString(R.string.land_phone_verify_code_success));

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


    private class UserExistEntity {
        private boolean isFirst;
        private UserEntity user;

        public boolean isFirst() {
            return isFirst;
        }

        public void setFirst(boolean first) {
            isFirst = first;
        }

        public UserEntity getUser() {
            return user;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "UserExist{" +
                    "isFirst=" + isFirst +
                    ", user=" + user +
                    '}';
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
                UserEntity mUserEntity = DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(LandPhoneActivity.this).getUserId());
                Constant.CONSTANT_USER_ID = mUserEntity.getUser_id()+"";
                Constant.CONSTANT_USER_NAME = mUserEntity.getUser_name();
                readyGoForNewTask(MainActivity.class);
               /* Intent intent = new Intent(ModifyUserActivity.this, MainActivity.class);
                startActivity(intent);

                finish();*/
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

}
