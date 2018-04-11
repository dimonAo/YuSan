package com.wtwd.yusan.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.fragment.TaskFragment;
import com.wtwd.yusan.fragment.TaskMeFragment;
import com.wtwd.yusan.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView text_publish_task;
    private TabLayout tab_layout_task;
    private ViewPager viewpager_task;

    private List<BaseFragment> mFragments = new ArrayList<>();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_task;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        addFragment();
        initToolbar();
        initView();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitleToolbarStyle(toolbar);


    }

    private void initView() {
        text_publish_task = (TextView) findViewById(R.id.text_publish_task);
        tab_layout_task = (TabLayout) findViewById(R.id.tab_layout_task);
        viewpager_task = (ViewPager) findViewById(R.id.viewpager_task);
//        Utils.setMargins(tab_layout_task, 0, Utils.getStatusBarHeight(this), 0, 0);

        ShortPagerAdapter mAdapter = new ShortPagerAdapter(getSupportFragmentManager());
        viewpager_task.setAdapter(mAdapter);
        tab_layout_task.setupWithViewPager(viewpager_task);

        text_publish_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(PublishTaskActivity.class);
            }
        });

    }

    private void addFragment() {
        mFragments.clear();
        mFragments.add(TaskFragment.getTaskFragment());
        mFragments.add(TaskMeFragment.getTaskMeFragment());
    }

    @Override
    public View getSnackView() {
        return toolbar;
    }

    private class ShortPagerAdapter extends FragmentPagerAdapter {
        public String[] mTilte;

        public ShortPagerAdapter(FragmentManager fm) {
            super(fm);
            mTilte = getResources().getStringArray(R.array.task_title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTilte[position];
        }

        @Override
        public BaseFragment getItem(int position) {
            BaseFragment fragment = mFragments.get(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTilte.length;
        }
    }
}
