package com.wtwd.yusan.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wtwd.yusan.MyApplication;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.wxapi.WXEntryActivity;

import java.net.URL;

public class LandChooseActivity extends CommonToolBarActivity implements View.OnClickListener {

    private ImageView img_choose_wechat_land;
    private ImageView img_choose_phone_land;

    private ImageView test;

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
        test = (ImageView) findViewById(R.id.test);
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
}
