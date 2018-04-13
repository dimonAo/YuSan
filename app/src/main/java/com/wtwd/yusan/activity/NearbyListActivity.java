package com.wtwd.yusan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.wtwd.yusan.R;
import com.wtwd.yusan.adapter.NearbyListAdapter;
import com.wtwd.yusan.adapter.TaskAdapter;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.NearbyEntity;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import okhttp3.Call;

/**
 * time:2018/4/11
 * Created by w77996
 */

public class NearbyListActivity extends CommonToolBarActivity {

    private EasyRefreshLayout easy_layout;

    private RecyclerView recycler_nearbylist;

    private NearbyListAdapter mNearbyListAdapter;

    private List<NearbyEntity> mNearbyEntitys = new ArrayList<>();


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_nearbylist;
    }



    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        initView();
    }


    @Override
    public View getSnackView() {
        return null;
    }


    private void initView() {
        text_tool_bar_title.setText("附近");
        easy_layout = (EasyRefreshLayout) findViewById(R.id.easy_layout);
        recycler_nearbylist = (RecyclerView) findViewById(R.id.recycler_nearbylist);
        recycler_nearbylist.setLayoutManager(new LinearLayoutManager(this));
       // DividerItemDecoration mDi = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
      //  mDi.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_line));
       // recycler_nearbylist.addItemDecoration(mDi);

        mNearbyListAdapter = new NearbyListAdapter(R.layout.item_nearby_list, null);
        recycler_nearbylist.setAdapter(mNearbyListAdapter);

        getData();
    }

    private void getData() {

        OkHttpUtils.get()
                .url("https://www.baidu.com/")
                .build()
                .connTimeOut(3000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("ff",response);
                    }
                });
        mNearbyEntitys.clear();
        for (int i = 0; i < 20; i++) {
            NearbyEntity mEn = new NearbyEntity();
            mEn.setSex(new Random().nextInt(1) + "");

            mEn.setName("张三" + i);
            mNearbyEntitys.add(mEn);
        }
        mNearbyListAdapter.getData().addAll(mNearbyEntitys);
        mNearbyListAdapter.notifyDataSetChanged();

        easy_layout.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 5);
        easy_layout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

                final List<NearbyEntity> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    NearbyEntity mEn = new NearbyEntity();
                    mEn.setSex(new Random().nextInt(1) + "");

                    mEn.setName("张三" + i);
                    list.add(mEn);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        easy_layout.loadMoreComplete();
//                        easy_layout.closeLoadView();
                        int postion = mNearbyListAdapter.getData().size();
                        mNearbyListAdapter.getData().addAll(list);
                        mNearbyListAdapter.notifyDataSetChanged();
                        recycler_nearbylist.scrollToPosition(postion);
                    }
                }, 500);

            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<NearbyEntity> list = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            NearbyEntity mEn = new NearbyEntity();
                            mEn.setSex(new Random().nextInt(1) + "");

                            mEn.setName("张三" + i);
                            list.add(mEn);
                            list.add(mEn);
                        }
                        mNearbyListAdapter.setNewData(list);
                        easy_layout.refreshComplete();
//                        Toast.makeText(getApplicationContext(), "refresh success", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            }
        });
    }
}
