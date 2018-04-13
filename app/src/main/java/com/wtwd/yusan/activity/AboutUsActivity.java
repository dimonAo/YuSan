package com.wtwd.yusan.activity;

import android.os.Bundle;
import android.view.View;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.base.CommonToolBarActivity;

/**
 * time:2018/4/11
 * Created by w77996
 */

public class AboutUsActivity extends CommonToolBarActivity {
    @Override
    public int getLayoutResourceId() {
        return R.layout.acitvity_aboutus;
    }


    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {

    }

    @Override
    public View getSnackView() {
        return null;
    }
}
