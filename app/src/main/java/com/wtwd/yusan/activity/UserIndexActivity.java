package com.wtwd.yusan.activity;

import android.os.Bundle;
import android.view.View;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;

/**
 * time:2018/4/16
 * Created by w77996
 */

public class UserIndexActivity extends CommonToolBarActivity{
    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
    }

    private void initView() {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_userindex;
    }

    @Override
    public View getSnackView() {
        return null;
    }
}
