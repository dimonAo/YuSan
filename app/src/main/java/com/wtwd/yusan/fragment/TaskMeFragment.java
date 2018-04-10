package com.wtwd.yusan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wtwd.yusan.R;
import com.wtwd.yusan.adapter.TaskAdapter;
import com.wtwd.yusan.adapter.TaskMeAdapter;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskMeFragment extends BaseFragment {

    private static TaskMeFragment mInstance;
    private RecyclerView recycler_task;
    private EasyRefreshLayout easy_layout;
    private List<TaskEntity> mTaskEntitys = new ArrayList<>();
    private TaskMeAdapter mAdapter;

    public TaskMeFragment() {

    }

    public static TaskMeFragment getTaskMeFragment() {
        if (null == mInstance) {
            mInstance = new TaskMeFragment();
        }
        return mInstance;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_task_me;
    }

    @Override
    public void initFragmentView(Bundle savedInstanceState, View mView) {
        easy_layout = (EasyRefreshLayout) mView.findViewById(R.id.easy_layout);
        recycler_task = (RecyclerView) mView.findViewById(R.id.recycler_task);
        recycler_task.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration mDi = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mDi.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.shape_line));
        recycler_task.addItemDecoration(mDi);

        mAdapter = new TaskMeAdapter(R.layout.item_fragment_task, null);
        recycler_task.setAdapter(mAdapter);
        getData();

        initListener();
    }


    private void getData() {
        mTaskEntitys.clear();
        for (int i = 0; i < 20; i++) {
            TaskEntity mEn = new TaskEntity();
            mEn.setImgUrl("");
            mEn.setTaskCondition(new Random().nextInt(4) + "");
            mEn.setTaskContent("很长很长很长的任务 : " + (mAdapter.getData().size() + i));
            mEn.setTaskerSex(new Random().nextInt(2) + "");
            mEn.setTaskCost(Float.parseFloat(String.format("%1.1f", (new Random().nextFloat()) * 10)));
            mEn.setTaskLocation("深圳");
            mEn.setTaskType(new Random().nextInt(2));
            mEn.setTaskTime(new SimpleDateFormat("HH:mm").format(new Date()));
            mEn.setTaskDate(new SimpleDateFormat("MM月dd日").format(new Date()));
            mEn.setTaskState(new Random().nextInt(7) + "");
            mEn.setTaskName("张三");
            mTaskEntitys.add(mEn);
        }
        mAdapter.getData().addAll(mTaskEntitys);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        easy_layout.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 5);
        easy_layout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

                final List<TaskEntity> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    TaskEntity mEn = new TaskEntity();
                    mEn.setImgUrl("");
                    mEn.setTaskCondition(new Random().nextInt(4) + "");
                    mEn.setTaskContent("很长很长很长的任务 : " + (mAdapter.getData().size() + i));
                    mEn.setTaskerSex(new Random().nextInt(2) + "");
                    mEn.setTaskCost(Float.parseFloat(String.format("%1.1f", (new Random().nextFloat()) * 10)));
                    mEn.setTaskLocation("深圳");
                    mEn.setTaskType(new Random().nextInt(2));
                    mEn.setTaskTime(new SimpleDateFormat("HH:mm").format(new Date()));
                    mEn.setTaskDate(new SimpleDateFormat("MM月dd日").format(new Date()));
                    mEn.setTaskState(new Random().nextInt(7) + "");
                    mEn.setTaskName("张三");
                    list.add(mEn);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        easy_layout.loadMoreComplete();
//                        easy_layout.closeLoadView();
                        int postion = mAdapter.getData().size();
                        mAdapter.getData().addAll(list);
                        mAdapter.notifyDataSetChanged();
                        recycler_task.scrollToPosition(postion);
                    }
                }, 500);

            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<TaskEntity> list = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            TaskEntity mEn = new TaskEntity();
                            mEn.setImgUrl("");
                            mEn.setTaskCondition(new Random().nextInt(4) + "");
                            mEn.setTaskContent("很长很长很长的任务 : " + (mAdapter.getData().size() + i));
                            mEn.setTaskerSex(new Random().nextInt(2) + "");
                            mEn.setTaskCost(Float.parseFloat(String.format("%1.1f", (new Random().nextFloat()) * 10)));
                            mEn.setTaskLocation("深圳");
                            mEn.setTaskType(new Random().nextInt(2));
                            mEn.setTaskTime(new SimpleDateFormat("HH:mm").format(new Date()));
                            mEn.setTaskDate(new SimpleDateFormat("MM月dd日").format(new Date()));
                            mEn.setTaskState(new Random().nextInt(7) + "");
                            mEn.setTaskName("张三");
                            list.add(mEn);
                        }
                        mAdapter.setNewData(list);
                        easy_layout.refreshComplete();
//                        Toast.makeText(getApplicationContext(), "refresh success", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            }
        });
    }
}
