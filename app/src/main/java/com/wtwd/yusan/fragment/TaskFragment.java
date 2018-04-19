package com.wtwd.yusan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wtwd.yusan.R;
import com.wtwd.yusan.activity.DetailTaskActivity;
import com.wtwd.yusan.adapter.TaskAdapter;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.GsonUtils;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskFragment extends BaseFragment {
    private static final String TAG = "TaskFragment";

    private static TaskFragment mInstance;
    private RecyclerView recycler_task;
    private EasyRefreshLayout easy_layout;
    private List<TaskEntity> mTaskEntitys = new ArrayList<>();
    private TaskAdapter mAdapter;

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

    public TaskFragment() {

    }

    public static TaskFragment getTaskFragment() {
        if (null == mInstance) {
            mInstance = new TaskFragment();
        }
        return mInstance;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_task;
    }

    @Override
    public void initFragmentView(Bundle savedInstanceState, View mView) {
        easy_layout = (EasyRefreshLayout) mView.findViewById(R.id.easy_layout);
        recycler_task = (RecyclerView) mView.findViewById(R.id.recycler_task);
        recycler_task.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration mDi = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mDi.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.shape_line));
        recycler_task.addItemDecoration(mDi);

        mAdapter = new TaskAdapter(R.layout.item_fragment_task, null);
        recycler_task.setAdapter(mAdapter);
        getData();

        initListener();

        addListener();
    }

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<TaskEntity> mList = mAdapter.getData();
                Bundle bundle = new Bundle();
                bundle.putParcelable("task_entity", mList.get(position));
                readyGo(DetailTaskActivity.class, bundle);
            }
        });
    }


    /**
     * 从服务器获取指定的任务信息
     *
     * @param startCount 开始条数
     * @param count      一次获取几条消息
     */
    private void getAllMission(int startCount, int count) {
        Map<String, String> mStartCount = new HashMap<>();
        mStartCount.put("start", startCount + "");
        mStartCount.put("count", count + "");

        final List<TaskEntity> mList = new ArrayList<>();

        OkHttpUtils.get()
                .url(Constans.GET_ALL_MISSION)
                .params(mStartCount)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if (DEBUG) {
                            Log.e(TAG, "okhttp getAllMission e : " + e.toString());
                        }

                        if (1 == mLoadTYpe) {
                            easy_layout.loadMoreComplete();
                            easy_layout.closeLoadView();

                        } else if (2 == mLoadTYpe) {
                            easy_layout.refreshComplete();
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (DEBUG) {
                            Log.e(TAG, "okhttp getAllMission response : " + response);
                        }
                        ResultEntity mEn = Utils.getResultEntity(response);

                        if (1 == mEn.getStatus()) {
                            mList.addAll(GsonUtils.jsonToList(mEn.getObject(), TaskEntity.class));
                            if (1 == mLoadTYpe) {
                                easy_layout.loadMoreComplete();
                                easy_layout.closeLoadView();
                                int postion = mAdapter.getData().size();
                                mAdapter.getData().addAll(mList);
                                mAdapter.notifyDataSetChanged();
                                recycler_task.scrollToPosition(postion);
                            } else if (2 == mLoadTYpe) {
                                mAdapter.setNewData(mList);
                                easy_layout.refreshComplete();
                            }
                        } else {
                            String mError = Utils.getErrorString(mEn.getErrCode());
                            if (1 == mLoadTYpe) {
                                easy_layout.loadMoreComplete();
                                easy_layout.closeLoadView();

                            } else if (2 == mLoadTYpe) {
                                easy_layout.refreshComplete();
                            }
                        }
                    }
                });

//        return mList;
    }


    private void getData() {
        String mStr = Pref.getInstance(getActivity()).getTaskJson();
        if ("0".equals(mStr)) {
            return;
        }
        mAdapter.getData().addAll(GsonUtils.GsonToList(mStr, TaskEntity.class));
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        easy_layout.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 5);
        easy_layout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mLoadTYpe = 1;
                getAllMission(mLoadCount * 20, 20);

                mLoadCount++;
            }

            @Override
            public void onRefreshing() {
                mLoadCount = 0;
                mLoadTYpe = 2;
                getAllMission(0, 20);

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        String mTaskJson = GsonUtils.GsonString(mAdapter.getData());
        Pref.getInstance(getActivity()).setTaskJson(mTaskJson);
    }

}
