package com.wtwd.yusan.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.wtwd.yusan.MyApplication;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.db.DemoDBManager;
import com.wtwd.yusan.ease.ui.*;
import com.wtwd.yusan.entity.UserEntity;
import com.wtwd.yusan.entity.operation.DaoUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.wxapi.WXEntryActivity;

import java.net.URL;

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
        // enter the main activity if already logged in
        if (IMHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            startActivity(new Intent(this, MainActivity.class));

            return;
        }
        img_choose_wechat_land = (ImageView) findViewById(R.id.img_choose_wechat_land);
        img_choose_phone_land = (ImageView) findViewById(R.id.img_choose_phone_land);
        test = (ImageView) findViewById(R.id.test);
        addListener();
        UserEntity userEntity = DaoUtils.getUserManager().queryUserForUserId(Pref.getInstance(this).getUserId());
        if(null == userEntity){

        }else{
            Constant.CONSTANT_USER_ID = userEntity.getUser_id()+"";
            Constant.CONSTANT_USER_NAME = userEntity.getUser_name();
                    login(userEntity.getUser_name());
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
//            WXEntryActivity.loginWeixin(this, MyApplication.api);

            Glide.with(this)
                    .load("https://img-blog.csdn.net/20170428175627934?watermark" +
                            "/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveWVjaGFvYQ==/font/5a6L5L2T" +
                            "/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center")
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(test);

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
                startActivity(intent);

                finish();
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
