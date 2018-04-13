package com.wtwd.yusan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.wtwd.yusan.R;
import com.wtwd.yusan.adapter.NearbyListAdapter;
import com.wtwd.yusan.adapter.TaskAdapter;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.entity.LastVersionEntity;
import com.wtwd.yusan.entity.NearbyEntity;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;

/**
 * time:2018/4/11
 * Created by w77996
 */

public class NearbyListActivity extends BaseActivity {

    private EasyRefreshLayout easy_layout;

    private RecyclerView recycler_nearbylist;

    private NearbyListAdapter mNearbyListAdapter;

    private List<NearbyEntity> mNearbyEntitys = new ArrayList<>();

    private List<LastVersionEntity> mLastVersionEntityList = new ArrayList<>();
    private LatLng mMyLatLng;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_nearbylist;
    }

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        initView();
    }



    @Override
    public View getSnackView() {
        return null;
    }


    private void initView() {
        easy_layout = (EasyRefreshLayout) findViewById(R.id.easy_layout);
        recycler_nearbylist = (RecyclerView) findViewById(R.id.recycler_nearbylist);
        recycler_nearbylist.setLayoutManager(new LinearLayoutManager(this));
       // DividerItemDecoration mDi = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
      //  mDi.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_line));
       // recycler_nearbylist.addItemDecoration(mDi);

        mNearbyListAdapter = new NearbyListAdapter(R.layout.item_nearby_list, null);
        recycler_nearbylist.setAdapter(mNearbyListAdapter);

        //getData();

        getNearbyUser(1,20);
    }

    /**
     * 获取附近的人列表
     */
    private void getNearbyUser(int page,int size) {
        OkHttpUtils.get()
                .addParams("start",page+"")
                .addParams("count",size+"")
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
                       /* try {
                            JSONObject json = new JSONObject(response);
                            int status = json.getInt("status");
                            int errCode = json.getInt("errCode");
                            if (status == 1) {
                                String result = json.getString("object");
                                mLastVersionEntityList = GsonUtils.getInstance().jsonToList(result, LastVersionEntity.class);
                                if (!mLastVersionEntityList.isEmpty()) {

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                });

    }

    private void loadNearbyUserRecycleList() {

        //mNearbyEntitys.clear();
        mNearbyListAdapter.getData().addAll(mLastVersionEntityList);
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
