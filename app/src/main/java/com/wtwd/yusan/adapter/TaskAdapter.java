package com.wtwd.yusan.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.entity.TaskEntity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskAdapter extends BaseQuickAdapter<TaskEntity, BaseViewHolder> {

    public TaskAdapter(int layoutResId, @Nullable List<TaskEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskEntity item) {

        helper.setBackgroundRes(R.id.circle_img_task_publisher, R.mipmap.task_head)
                .setText(R.id.text_task_publisher_nick, item.getTaskName())
                .setText(R.id.text_task_content, item.getTaskContent())
                .setText(R.id.text_task_time, item.getTaskTime())
                .setText(R.id.text_task_date, item.getTaskDate())
                .setText(R.id.text_task_location, item.getTaskLocation())
                .setText(R.id.text_task_cost, item.getTaskCost() + "")
                .setBackgroundRes(R.id.img_task_type, R.mipmap.task_type_1);

        if (1 == item.getTaskType()) {
            helper.setText(R.id.text_task_type, "吃饭");
        } else {
            helper.setText(R.id.text_task_type, "快递");
        }


        if ("0".equals(item.getTaskerSex())) {
            helper.setBackgroundRes(R.id.task_publisher_sex, R.mipmap.task_f);
        } else {
            helper.setBackgroundRes(R.id.task_publisher_sex, R.mipmap.task_m);
        }

        if ("0".equals(item.getTaskCondition())) {
            helper.setText(R.id.text_task_condition, "限女生");
        } else if ("1".equals(item.getTaskCondition())) {
            helper.setText(R.id.text_task_condition, "限男生");
        } else {
            helper.setText(R.id.text_task_condition, "不限男女");
        }

        if ("1".equals(item.getTaskState())) {
            helper.getView(R.id.btn_task).setEnabled(true);
            helper.setBackgroundRes(R.id.btn_task, R.drawable.selector_task_btn)
                    .addOnClickListener(R.id.btn_task)
                    .setText(R.id.btn_task, "领取任务");
        } else {
            helper.setBackgroundRes(R.id.btn_task, R.drawable.shape_task_btn)
                    .setText(R.id.btn_task, "已失效");
            helper.getView(R.id.btn_task).setEnabled(false);
        }

    }
}
