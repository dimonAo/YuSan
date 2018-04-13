package com.wtwd.yusan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wtwd.yusan.R;
import com.wtwd.yusan.adapter.NearbyListAdapter;
import com.wtwd.yusan.adapter.TaskAdapter;
import com.wtwd.yusan.base.BaseActivity;
import com.wtwd.yusan.entity.LastVersionEntity;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.NearbyEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

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


    private static final String TAG = "NearbyListActivity";

    private EasyRefreshLayout easy_layout;

    private RecyclerView recycler_nearbylist;

    private NearbyListAdapter mNearbyListAdapter;

    private List<NearbyEntity> mNearbyEntitys = new ArrayList<>();

    private List<LastVersionEntity> mLastVersionEntityList = new ArrayList<>();

    private LatLng mMyLatLng;

    private LatLng mMyLocation;

    private Double mScale;

    private int start = 0;

    private static final int count = 20;

    private Double mScaleTemp;


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

        Bundle bundle = getIntent().getExtras();
        mMyLocation = bundle.getParcelable("location");
        mScale = bundle.getParcelable("scale");
        Log.e(TAG,mMyLocation.latitude+"");
        //getData();

      //  getNearbyUser(1,20);
    }

    /**
     * 获取附近的人列表
     */
    private void getNearbyUser() {
        String param = "?lat="+mMyLocation.latitude+"&lng="+mMyLocation.longitude+"&start="+start+"&count="+count;
        OkHttpUtils.get()
                .url(Constans.REQUEST_URL+Constans.PORT)
                .build()
                .connTimeOut(3000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("ff",response);

                        try {
                            JSONObject json = new JSONObject(response);
                            int status = json.getInt("status");
                            int errCode = json.getInt("errCode");
                            if (status == 1) {
                                String result = json.getString("object");
                                mLastVersionEntityList = GsonUtils.getInstance().jsonToList(result, LastVersionEntity.class);
                                if (!mLastVersionEntityList.isEmpty()) {
                                    start+=count;
                                    mLastVersionEntityList.clear();
                                    mNearbyListAdapter.getData().addAll(mLastVersionEntityList);
                                    mNearbyListAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void loadNearbyUserRecycleList() {
        easy_layout.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 5);
        easy_layout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mScale = mScaleTemp + 1L;
                getNearbyUser();
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
                       // mNearbyListAdapter.getData().addAll(list);
                        mNearbyListAdapter.notifyDataSetChanged();
                        recycler_nearbylist.scrollToPosition(postion);
                    }
                }, 500);

            }

            @Override
            public void onRefreshing() {
                start = 0;
                mScaleTemp = mScale;
                getNearbyUser();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<LastVersionEntity> list = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            LastVersionEntity mEn = new LastVersionEntity();
                            mEn.getUser().setSex(new Random().nextInt(1) );
                            mEn.getUser().setUser_name("张三" + i);
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
