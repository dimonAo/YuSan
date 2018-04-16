package com.wtwd.yusan.activity;

import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.widget.view.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class DetailTaskActivity extends CommonToolBarActivity {

    private CircleImageView circle_img_task_publisher;
    private TextView text_task_publisher_nick;
    private ImageView task_publisher_sex;
    private TextView text_task_content;
    private TextView text_task_cost;
    private ImageView img_task_type;
    private TextView text_task_type;
    private TextView text_task_date;
    private TextView text_task_time;
    private TextView text_task_condition;
    private TextView text_task_location;
    private TextView text_task_number;
    private TextView text_task_create_time;
    private TextView text_task_receive_time;
    private TextView text_task_close_time;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_detail_task;
    }

    @Override
    public View getSnackView() {
        return tool_bar;
    }

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        text_tool_bar_title.setText("任务详情");


    }

    private void initView() {
        circle_img_task_publisher = (CircleImageView) findViewById(R.id.circle_img_nearbylist_publisher);
        text_task_publisher_nick = (TextView) findViewById(R.id.text_task_publisher_nick);
        task_publisher_sex = (ImageView) findViewById(R.id.task_publisher_sex);
        text_task_content = (TextView) findViewById(R.id.text_task_content);
        text_task_cost = (TextView) findViewById(R.id.text_task_cost);
        img_task_type = (ImageView) findViewById(R.id.img_task_type);
        text_task_type = (TextView) findViewById(R.id.text_task_type);
        text_task_date = (TextView) findViewById(R.id.text_task_date);
        text_task_time = (TextView) findViewById(R.id.text_task_time);
        text_task_condition = (TextView) findViewById(R.id.text_task_condition);
        text_task_location = (TextView) findViewById(R.id.text_task_location);
        text_task_number = (TextView) findViewById(R.id.text_task_number);
        text_task_create_time = (TextView) findViewById(R.id.text_task_create_time);
        text_task_receive_time = (TextView) findViewById(R.id.text_task_receive_time);
        text_task_close_time = (TextView) findViewById(R.id.text_task_close_time);
    }


    private void displayTaskDetailInfo(TaskEntity mTaskEntity) {
        if (null == mTaskEntity) {
            return;
        }

        Glide.with(this)
                .load(Uri.parse(mTaskEntity.getPublisher().getHead_img()))
                .into(circle_img_task_publisher);

        text_task_publisher_nick.setText(mTaskEntity.getPublisher().getUser_name());
        if (1 == mTaskEntity.getPublisher().getSex()) {
            task_publisher_sex.setImageResource(R.mipmap.task_m);
        } else if (2 == mTaskEntity.getPublisher().getSex()) {
            task_publisher_sex.setImageResource(R.mipmap.task_f);
        }

        text_task_content.setText(mTaskEntity.getContent());
        text_task_cost.setText(mTaskEntity.getMoney() + "");

        if (1 == mTaskEntity.getType()) {
            img_task_type.setImageResource(R.mipmap.task_type_1);
            text_task_type.setText("快递");
        }

        /**
         * 任务开始日期和时间
         */
        text_task_date.setText(mTaskEntity.getStart_time());
        text_task_time.setText(mTaskEntity.getStart_time());

        if (1 == mTaskEntity.getSex()) {
            text_task_condition.setText("限男生");
        } else if (2 == mTaskEntity.getSex()) {
            text_task_condition.setText("限女生");
        } else if (3 == mTaskEntity.getSex()) {
            text_task_condition.setText("不限男女");
        }

        text_task_location.setText(mTaskEntity.getAddress());

        /**
         * 任务编号
         */
        text_task_number.setText("");

        /**
         * 任务创建时间
         */
        text_task_create_time.setText(mTaskEntity.getCreate_time());

        /**
         * 任务领取时间
         */
        text_task_receive_time.setText("");

        /**
         * 任务完成时间
         */
        text_task_close_time.setText("");


    }

    /**
     * 获取从TaskFragment和TaskMeFragment传过来的TaskEntity
     *
     * @return
     */
    private TaskEntity getTaskEntity() {
        if (null == getIntent()) {
            return null;
        }

        Bundle bundle = getIntent().getExtras();
        TaskEntity mTask = bundle.getParcelable("task_entity");
        return mTask;
    }


    /**
     * 确认完成任务
     *
     * @param userId    用户ID
     * @param missionId 任务ID
     */
    private void completMission(String userId, String missionId) {
        Map<String, String> mAcceptMission = new HashMap<>();
        mAcceptMission.put("userId", userId);
        mAcceptMission.put("missionId", missionId);

        OkHttpUtils.get()
                .url(Constans.COMPLET_MISSION)
                .params(mAcceptMission)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

    }

    /**
     * 接取任务
     *
     * @param userId    用户ID
     * @param missionId 任务ID
     */
    private void acceptMission(String userId, String missionId) {
        Map<String, String> mAcceptMission = new HashMap<>();
        mAcceptMission.put("userId", userId);
        mAcceptMission.put("missionId", missionId);

        OkHttpUtils.get()
                .url(Constans.ACCEPT_MISSION)
                .params(mAcceptMission)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                    }
                });

    }
}
