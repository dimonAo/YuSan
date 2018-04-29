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
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Utils;
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

    /**
     * 加载类型
     * 1：上拉加载
     * 2：下拉刷新
     */
    private int mLoadTYpe;

    /**
     * 上拉加载次数
     */
    private int mLoadCount;


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

        text_tool_bar_title.setText(R.string.nearby_title);
        easy_layout = (EasyRefreshLayout) findViewById(R.id.easy_layout);
        recycler_nearbylist = (RecyclerView) findViewById(R.id.recycler_nearbylist);
        recycler_nearbylist.setLayoutManager(new LinearLayoutManager(this));

        mNearbyListAdapter = new NearbyListAdapter(R.layout.item_nearby_list, null);
        recycler_nearbylist.setAdapter(mNearbyListAdapter);

        Bundle bundle = getIntent().getExtras();
        mMyLocation = bundle.getParcelable("location");
        mScale = bundle.getParcelable("scale");
        Log.e(TAG,mMyLocation.latitude+"");

        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        easy_layout.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 5);
        easy_layout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mLoadTYpe = 1;
                getNearbyUser(mLoadCount * 20, 20);

                mLoadCount++;
            }

            @Override
            public void onRefreshing() {
                mLoadTYpe = 2;
                mLoadCount = 0;
                getNearbyUser(0, 20);

            }
        });
    }

    /**
     * 获取附近的人
     * @param start
     * @param count
     */
    private void getNearbyUser(int start,int count){
        final List<LastVersionEntity> list = new ArrayList<>();
        HashMap<String,String> params = new HashMap<>();
        params.put("start",start+"");
        params.put("count",count+"");

        OkHttpUtils.get()
                .url(Constans.REQUEST_URL)
                .params(params)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (1 == mLoadTYpe) {
                            easy_layout.loadMoreComplete();
                            easy_layout.closeLoadView();

                        } else if (2 == mLoadTYpe) {
                            easy_layout.refreshComplete();
                        }
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        ResultEntity mEn = Utils.getResultEntity(response);
                        if(1 == mEn.getStatus()){
                           // list.addAll(GsonUtils.getInstance().jsonToList(mEn.getObject(),LastVersionEntity.class));
                            if(1 == mLoadTYpe){
                                easy_layout.loadMoreComplete();
                                easy_layout.closeLoadView();
                                int position = mNearbyListAdapter.getData().size();
                                mNearbyListAdapter.getData().addAll(list);
                                mNearbyListAdapter.notifyDataSetChanged();
                                recycler_nearbylist.scrollToPosition(position);
                            }else if(2 == mEn.getStatus()){
                                mNearbyListAdapter.setNewData(list);
                                easy_layout.refreshComplete();
                            }
                        }else{
                            String mError = getErrorString(mEn.getErrCode());
                            Log.e(TAG,mError);
                            if (1 == mLoadTYpe) {
                                easy_layout.loadMoreComplete();
                                easy_layout.closeLoadView();
                            } else if (2 == mLoadTYpe) {
                                easy_layout.refreshComplete();
                            }
                        }
                    }
                });

    }


}
