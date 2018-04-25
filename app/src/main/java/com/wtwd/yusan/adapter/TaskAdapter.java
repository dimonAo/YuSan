package com.wtwd.yusan.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;

import java.util.Calendar;
import java.util.List;


public class TaskAdapter extends BaseQuickAdapter<TaskEntity, BaseViewHolder> {

    public TaskAdapter(int layoutResId, @Nullable List<TaskEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskEntity item) {
//        Glide.with(mContext)
//                .load(Uri.parse(item.getPublisher().getHead_img()))
//                .into((CircleImageView) helper.getView(R.id.circle_img_task_publisher));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getStart_time());


        helper.setBackgroundRes(R.id.circle_img_task_publisher, R.mipmap.task_head)
//                .setText(R.id.text_task_publisher_nick, item.getPublisher().getUser_name())
                .setText(R.id.text_task_publisher_nick, item.getUser_name())
                .setText(R.id.text_task_content, item.getContent())
//                .setText(R.id.text_task_time, item.getTaskTime())
                .setText(R.id.text_task_date, calendar.get(Calendar.MONTH + 1)
                        + mContext.getResources().getString(R.string.task_month_string)
                        + calendar.get(Calendar.DAY_OF_MONTH)
                        + mContext.getResources().getString(R.string.task_day_string))
                .setText(R.id.text_task_time, calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE))
                .setText(R.id.text_task_location, item.getAddress())
//                .setBackgroundRes(R.id.img_task_type, R.mipmap.task_type_1)
                .setText(R.id.text_task_cost, item.getMoney() + "");

        /**
         * 任务类型
         */
//        if (1 == item.getType()) {
        helper.setText(R.id.text_task_type, Utils.getTaskString(item.getType()))
                .setBackgroundRes(R.id.img_task_type, R.mipmap.task_type_1);
//        } else {
//            helper.setText(R.id.text_task_type, "快递");
//        }

        /**
         * 任务发布者性别标识
         */
//        if (2 == item.getPublisher().getSex()) {
        if (2 == item.getUser_sex()) {
            helper.setBackgroundRes(R.id.task_publisher_sex, R.mipmap.task_f);
//        } else if (1 == item.getPublisher().getSex()) {
        } else if (1 == item.getUser_sex()) {
            helper.setBackgroundRes(R.id.task_publisher_sex, R.mipmap.task_m);
        }

        /**
         * 任务限定接取人性别条件
         */
        if (2 == item.getSex()) {
            helper.setText(R.id.text_task_condition, mContext.getResources().getString(R.string.task_only_f));
        } else if (1 == item.getSex()) {
            helper.setText(R.id.text_task_condition, mContext.getResources().getString(R.string.task_only_man));
        } else {
            helper.setText(R.id.text_task_condition, mContext.getResources().getString(R.string.task_m_and_f));
        }

        /**
         * 发布任务状态
         */

        if (item.getUser_id() == Pref.getInstance(mContext).getUserId()) {
//            helper.getView(R.id.btn_task).setEnabled(false);
            helper.setBackgroundRes(R.id.btn_task, R.drawable.selector_task_btn)
//                    .addOnClickListener(R.id.btn_task)
                    .setText(R.id.btn_task, R.string.task_to_receive);
        } else {
//            helper.getView(R.id.btn_task).setEnabled(true);
            helper.setBackgroundRes(R.id.btn_task, R.drawable.selector_task_btn)
//                    .addOnClickListener(R.id.btn_task)
                    .setText(R.id.btn_task, R.string.task_receive_task);
        }

//        if (0 == item.getStatus()) {
//            helper.getView(R.id.btn_task).setEnabled(true);
//            helper.setBackgroundRes(R.id.btn_task, R.drawable.selector_task_btn)
//                    .addOnClickListener(R.id.btn_task)
//                    .setText(R.id.btn_task, "领取任务");
//        } else if (5 == item.getStatus()) {
//            helper.setBackgroundRes(R.id.btn_task, R.drawable.shape_task_btn)
//                    .setText(R.id.btn_task, "已失效");
//            helper.getView(R.id.btn_task).setEnabled(false);
//        } else if (4 == item.getStatus()) {
//            helper.setBackgroundRes(R.id.btn_task, R.drawable.shape_task_btn)
//                    .setText(R.id.btn_task, "已完成");
//            helper.getView(R.id.btn_task).setEnabled(false);
//        }

    }

}
