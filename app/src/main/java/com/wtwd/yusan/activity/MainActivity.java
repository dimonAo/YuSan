package com.wtwd.yusan.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.fragment.MeFragment;
import com.wtwd.yusan.fragment.MessageFragment;
import com.wtwd.yusan.fragment.NearbyMapFragment;
import com.wtwd.yusan.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final int MAIN_MESSAGE = 0;
    private static final int MAIN_NEAR = 1;
    private static final int MAIN_ME = 2;


    private Button btn_message;
    private Button btn_near;
    private Button btn_me;


    private List<Button> mButtons = new ArrayList<>();
    private List<BaseFragment> mFragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }


    private void initView() {
        btn_me = (Button) findViewById(R.id.btn_me);
        btn_message = (Button) findViewById(R.id.btn_message);
        btn_near = (Button) findViewById(R.id.btn_near);

        addListener();
        initPage();
    }

    private void addListener() {
        mButtons.clear();
        mButtons.add(btn_message);
        mButtons.add(btn_near);
        mButtons.add(btn_me);

        btn_me.setOnClickListener(this);
        btn_message.setOnClickListener(this);
        btn_near.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.btn_message == id) {
            changePage(MAIN_MESSAGE);
        } else if (R.id.btn_near == id) {
            changePage(MAIN_NEAR);
        } else if (R.id.btn_me == id) {
            changePage(MAIN_ME);
        }
    }

    private void initPage() {
        prepareFragment();
        changePage(MAIN_NEAR);
    }

    public void changePage(int page) {
        updateFragment(page);
    }

    private void prepareFragment() {
        mFragments.clear();
        // TODO: 2018/a1/26 0026 添加fragment实例到mFragments集合中
        mFragments.add(MessageFragment.getMessageFragment());
        mFragments.add(NearbyMapFragment.newInstance());
        mFragments.add(MeFragment.getMeFragment());
        for (BaseFragment fragment : mFragments) {
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).hide(fragment).commit();
        }
    }

    /**
     * 切换fragment,且设置button selector状态
     */
    private void updateFragment(int position) {
        if (position > mFragments.size() - 1) {
            return;
        }
        for (int i = 0; i < mFragments.size(); i++) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BaseFragment fragment = mFragments.get(i);
            if (i == position) {
                mButtons.get(i).setSelected(true);
//                mButtons.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorMainAtyBottomTextSelect));

                transaction.show(fragment);
            } else {
                mButtons.get(i).setSelected(false);
//                mButtons.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorMainAtyBottomTextDefault));
                transaction.hide(fragment);
            }
            transaction.commit();
        }
    }


}
