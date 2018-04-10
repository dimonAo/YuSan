package com.wtwd.yusan.fragment;

import android.os.Bundle;
import android.view.View;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseFragment;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class MeFragment extends BaseFragment {
    private static MeFragment mInstance;

    public static MeFragment getMeFragment() {
        if (null == mInstance) {
            mInstance = new MeFragment();
        }
        return mInstance;
    }

    public MeFragment() {

    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_me;
    }

    @Override
    public void initFragmentView(Bundle savedInstanceState, View mView) {

    }
}
