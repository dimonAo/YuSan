package com.wtwd.yusan.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskMeAdapter extends BaseQuickAdapter<TaskEntity, BaseViewHolder> {

    public TaskMeAdapter(int layoutResId, @Nullable List<TaskEntity> data) {
        super(layoutResId, data);
    }

    private String getDate(long mi) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((mi));
        String date = calendar.get(Calendar.MONTH + 1)
                + mContext.getResources().getString(R.string.task_month_string)
                + calendar.get(Calendar.DAY_OF_MONTH)
                + mContext.getResources().getString(R.string.task_day_string);
        return date;
    }

    private String getTime(long mi) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((mi));
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return time;
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskEntity item) {

        helper
                .setText(R.id.text_task_publisher_nick, item.getNick_name())
                .setText(R.id.text_task_content, item.getContent())
                .setText(R.id.text_task_date, getDate(item.getStart_time()))
                .setText(R.id.text_task_time, getTime(item.getStart_time()))
                .setText(R.id.text_task_location, item.getAddress())
//                .setBackgroundRes(R.id.img_task_type, R.mipmap.task_type_1)
                .setText(R.id.text_task_cost, item.getMoney() + "");

        Glide.with(mContext)
                .load(Uri.parse(item.getHead_img()))
                .into((ImageView) helper.getView(R.id.circle_img_task_publisher));
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
            helper.setText(R.id.text_task_condition, R.string.task_only_f);
        } else if (1 == item.getSex()) {
            helper.setText(R.id.text_task_condition, R.string.task_only_man);
        } else {
            helper.setText(R.id.text_task_condition, R.string.task_m_and_f);
        }

        /**
         * 任务状态
         */
        switch (item.getStatus()) {
            case 0:
                //待领取
                helper.getView(R.id.btn_task).setEnabled(false);
                helper.setTextColor(R.id.btn_task, ContextCompat.getColor(mContext, R.color.colorTask));
                helper.setBackgroundRes(R.id.btn_task, R.drawable.shape_stroke_btn)
                        .setText(R.id.btn_task, R.string.task_to_receive);
                break;

            case 1:
//                //进行中
//                if (item.getPublisher().getUser_id() == getUserId()) {
                if (item.getUser_id() == getUserId()) {
                    //我发布的任务
                    helper.setText(R.id.btn_task, R.string.task_running)
                            .setBackgroundRes(R.id.btn_task, R.drawable.shape_stroke_btn)
                            .setTextColor(R.id.btn_task, ContextCompat.getColor(mContext, R.color.colorTask));
                } else {
                    //我接收的任务
                    helper.setBackgroundRes(R.id.btn_task, R.drawable.selector_task_btn)
                            .setText(R.id.btn_task, R.string.task_commit)
                            .setTextColor(R.id.btn_task, ContextCompat.getColor(mContext, R.color.colorWhite));
                }

                break;

            case 2:
                //确认完成

//                if (item.getPublisher().getUser_id() == getUserId()) {
                if (item.getUser_id() == getUserId()) {
                    //我发布的任务
                    helper.setBackgroundRes(R.id.btn_task, R.drawable.selector_task_btn)
                            .setText(R.id.btn_task, R.string.task_commit)
                            .setTextColor(R.id.btn_task, ContextCompat.getColor(mContext, R.color.colorWhite));
                } else {
                    //我接收的任务
                    helper.setText(R.id.btn_task, R.string.task_wait_for_comfirm)
                            .setBackgroundRes(R.id.btn_task, R.drawable.shape_stroke_btn)
                            .setTextColor(R.id.btn_task, ContextCompat.getColor(mContext, R.color.colorTask));
                }
                break;

            case 3:
                //已完成
//                helper.getView(R.id.btn_task).setEnabled(false);
                helper.setTextColor(R.id.btn_task, ContextCompat.getColor(mContext, R.color.colorWhite));
                helper.setBackgroundRes(R.id.btn_task, R.drawable.shape_task_btn)
                        .addOnClickListener(R.id.btn_task)
                        .setText(R.id.btn_task, R.string.task_commited);
                break;

            case 4:
                //失效
//                helper.getView(R.id.btn_task).setEnabled(false);
                helper.setTextColor(R.id.btn_task, ContextCompat.getColor(mContext, R.color.colorWhite));
                helper.setBackgroundRes(R.id.btn_task, R.drawable.shape_task_btn)
                        .setText(R.id.btn_task, R.string.task_expired);
                break;
        }


    }

    private long getUserId() {
        return Pref.getInstance(mContext).getUserId();
    }
}
