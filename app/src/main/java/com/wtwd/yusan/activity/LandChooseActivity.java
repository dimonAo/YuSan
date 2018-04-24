package com.wtwd.yusan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;

public class LandChooseActivity extends CommonToolBarActivity implements View.OnClickListener {

    private ImageView img_choose_wechat_land;
    private ImageView img_choose_phone_land;

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

        img_choose_wechat_land = (ImageView) findViewById(R.id.img_choose_wechat_land);
        img_choose_phone_land = (ImageView) findViewById(R.id.img_choose_phone_land);
        addListener();

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




        }

    }
}
