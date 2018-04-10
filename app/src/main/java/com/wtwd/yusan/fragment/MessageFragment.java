package com.wtwd.yusan.fragment;

import android.os.Bundle;
import android.view.View;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseFragment;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class MessageFragment extends BaseFragment {
    private static MessageFragment mInstance;

    public static MessageFragment getMessageFragment() {
        if (null == mInstance) {
            mInstance = new MessageFragment();
        }
        return mInstance;
    }

    public MessageFragment() {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initFragmentView(Bundle savedInstanceState, View mView) {

    }
}
