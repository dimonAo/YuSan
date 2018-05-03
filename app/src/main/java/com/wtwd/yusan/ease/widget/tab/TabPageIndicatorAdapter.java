package com.wtwd.yusan.ease.widget.tab;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pc-20170420 on 2017/5/28.
 */

public class TabPageIndicatorAdapter extends FragmentPagerAdapter {
    private static final String TAG = TabPageIndicatorAdapter.class.getSimpleName();

    private static final int TYPE_XX = 0;
    private static final int TYPE_TXL = 1;

    public static final String[] TITLE = new String[]{"消息","通讯录"};
    public static final int[] TYPE = new int[]{TYPE_XX,TYPE_TXL};

    private Fragment[] fragments;

    public TabPageIndicatorAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }


}
