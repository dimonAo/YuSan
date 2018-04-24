package com.wtwd.yusan.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.Utils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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
                        showToast("验证码验证成功");

                    } else {
                        showToast("验证码错误");
                    }
                    break;

                case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                    if (result == SMSSDK.RESULT_COMPLETE) {

                        showToast("获取验证码成功");
                    } else {
                        showToast("获取验证码失败，请重试");
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

    }

    private void initToolBar() {
        text_tool_bar_title.setText("登录");
    }

    private void initView() {
        int mCodeTimeCount = 2 * 60 * 1000;
        time = new TimeCount(mCodeTimeCount, 1000);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_verify = (EditText) findViewById(R.id.edit_verify);
        btn_get_verification_code = (Button) findViewById(R.id.btn_get_verification_code);
        btn_land = (Button) findViewById(R.id.btn_land);
        btn_get_verification_code.setText("获取验证码");
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
            showToast("请输入手机号码");
            return false;
        }

        if (!Utils.isMobileNO(getPhone())) {
            showToast("请输入正确手机号码");
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
            showToast("请输入验证码");
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
            if (checkPhone() && checkVerification()) {
                SMSSDK.submitVerificationCode("86", getPhone(), getVerification());
            }
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

}
