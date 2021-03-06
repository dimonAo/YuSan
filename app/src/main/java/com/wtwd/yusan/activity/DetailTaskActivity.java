package com.wtwd.yusan.activity;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.widget.view.CircleImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
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

    private Button btn_task_commit;
    private int mTaskSatusType; //1关闭  2确认完成  3领取任务
    private String mMissionId;

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
        text_tool_bar_title.setText(R.string.task_detail_title);

        initView();
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

        btn_task_commit = (Button) findViewById(R.id.btn_task_commit);

        btn_task_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (1 == mTaskSatusType) {
                    //关闭任务

                    closeMission(Pref.getInstance(DetailTaskActivity.this).getUserId() + "", mMissionId);
                } else if (2 == mTaskSatusType) {
                    //确认完成
                    completMission(Pref.getInstance(DetailTaskActivity.this).getUserId() + "", mMissionId);

                } else if (3 == mTaskSatusType) {
                    //领取任务
                    acceptMission(Pref.getInstance(DetailTaskActivity.this).getUserId() + "", mMissionId);
//                    acceptMission(2L + "", mMissionId);
                }


            }
        });

        displayTaskDetailInfo(getTaskEntity());
    }


    private void displayTaskDetailInfo(TaskEntity mTaskEntity) {
        if (null == mTaskEntity) {
            return;
        }
       // circle_img_task_publisher.set(R.mipmap.me_head_bg);
        Log.e(TAG, "mTaskEntity : ---> " + mTaskEntity.toString());

//        Glide.with(this)
////                .load(Uri.parse(mTaskEntity.getPublisher().getHead_img()))
//                .load(mTaskEntity.getHead_img())
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        circle_img_task_publisher.setImageBitmap(resource);
//                    }
//                });
//        Glide.with(this)
//                .load(mTaskEntity.getHead_img())
//                .into(circle_img_task_publisher);
        mMissionId = mTaskEntity.getMission_id() + "";

//        text_task_publisher_nick.setText(mTaskEntity.getPublisher().getUser_name());
        text_task_publisher_nick.setText(mTaskEntity.getNick_name());
//        if (1 == mTaskEntity.getPublisher().getSex()) {
        if (1 == mTaskEntity.getUser_sex()) {
            task_publisher_sex.setImageResource(R.mipmap.task_m);
//        } else if (2 == mTaskEntity.getPublisher().getSex()) {
        } else if (2 == mTaskEntity.getUser_sex()) {
            task_publisher_sex.setImageResource(R.mipmap.task_f);
        }

        text_task_content.setText(mTaskEntity.getContent());
        text_task_cost.setText(mTaskEntity.getMoney() + "");

//        if (1 == mTaskEntity.getType()) {
        img_task_type.setImageResource(R.mipmap.task_type_1);
        text_task_type.setText(Utils.getTaskString(mTaskEntity.getType()));
//        }

        /**
         * 任务开始日期和时间
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((mTaskEntity.getStart_time()));


        text_task_date.setText(calendar.get(Calendar.MONTH )
                + getString(R.string.task_month_string)
                + calendar.get(Calendar.DAY_OF_MONTH)
                + getString(R.string.task_day_string));
        text_task_time.setText(calendar.get(Calendar.HOUR_OF_DAY) + getString(R.string.task_dot) + calendar.get(Calendar.MINUTE));

        if (1 == mTaskEntity.getSex()) {
            text_task_condition.setText(R.string.task_only_man);
        } else if (2 == mTaskEntity.getSex()) {
            text_task_condition.setText(R.string.task_only_f);
        } else if (3 == mTaskEntity.getSex()) {
            text_task_condition.setText(R.string.task_m_and_f);
        }

        text_task_location.setText(mTaskEntity.getAddress());

        /**
         * 任务编号
         */
        text_task_number.setText(mTaskEntity.getMission_id() + "");

        /**
         * 任务创建时间
         */

        calendar.setTimeInMillis(Long.parseLong(mTaskEntity.getCreate_time()));

        text_task_create_time.setText(calendar.get(Calendar.YEAR) + "年"
                + (calendar.get(Calendar.MONTH) + 1) + "月"
                + calendar.get(Calendar.DAY_OF_MONTH) + "日"
                + " " + calendar.get(Calendar.HOUR_OF_DAY) + getString(R.string.task_dot) + calendar.get(Calendar.MINUTE));

        /**
         * 任务领取时间
         */
        if ((null != mTaskEntity.getAccept_time()) && (!"null".equals(mTaskEntity.getAccept_time()))) {
            calendar.setTimeInMillis(Long.parseLong(mTaskEntity.getAccept_time()));
            text_task_receive_time.setText(calendar.get(Calendar.YEAR) + "年"
                    + (calendar.get(Calendar.MONTH) + 1) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日"
                    + " " + calendar.get(Calendar.HOUR_OF_DAY) + getString(R.string.task_dot) + calendar.get(Calendar.MINUTE));
        } else {
            text_task_receive_time.setText("");
        }
        /**
         * 任务完成时间
         */

        if ((null != mTaskEntity.getFinish_time()) && (!"null".equals(mTaskEntity.getFinish_time()))) {


            calendar.setTimeInMillis(Long.parseLong(mTaskEntity.getFinish_time()));
            text_task_close_time.setText(calendar.get(Calendar.YEAR) + "年"
                    + (calendar.get(Calendar.MONTH) + 1) + "月"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "日"
                    + " " + calendar.get(Calendar.HOUR_OF_DAY) + getString(R.string.task_dot) + calendar.get(Calendar.MINUTE));
        } else {
            text_task_close_time.setText("");
        }
//        long publishUserId = mTaskEntity.getPublisher().getUser_id();

        long publishUserId = mTaskEntity.getPublish_id();

        long userId = mPref.getUserId();

        if (DEBUG) {
            Log.e(TAG, "detail task id : " + publishUserId + "---->" + userId);
        }

        if (userId == publishUserId) {
            //我发布的任务
            switch (mTaskEntity.getStatus()) {
                case 0:
                    btn_task_commit.setEnabled(true);
                    btn_task_commit.setText(R.string.task_close);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.selector_close_task));
                    mTaskSatusType = 1;

                    break;

                case 1:
                    btn_task_commit.setEnabled(false);
                    btn_task_commit.setText(R.string.task_running);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.selector_task_btn));
                    break;

                case 2:
                    btn_task_commit.setEnabled(true);
                    btn_task_commit.setText(R.string.task_commit);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.selector_task_btn));
                    mTaskSatusType = 2;
                    break;

                case 3:
                    btn_task_commit.setEnabled(false);
                    btn_task_commit.setText(R.string.task_commited);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_task_btn));
                    break;

                case 4:
                    btn_task_commit.setEnabled(false);
                    btn_task_commit.setText(R.string.task_expired);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_task_btn));
                    break;
            }

        } else {
            //我接收的任务
            switch (mTaskEntity.getStatus()) {
                case 0:
                    btn_task_commit.setEnabled(true);
                    btn_task_commit.setText(R.string.task_receive_task);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.selector_task_btn));
                    mTaskSatusType = 3;
                    break;

                case 1:
                    btn_task_commit.setEnabled(true);
                    btn_task_commit.setText(R.string.task_commit);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.selector_task_btn));
                    mTaskSatusType = 2;
                    break;

                case 2:
                    btn_task_commit.setEnabled(false);
                    btn_task_commit.setText(R.string.task_wait_for_comfirm);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.selector_task_btn));
                    break;

                case 3:
                    btn_task_commit.setEnabled(false);
                    btn_task_commit.setText(R.string.task_commited);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_task_btn));
                    break;

                case 4:
                    btn_task_commit.setEnabled(false);
                    btn_task_commit.setText(R.string.task_expired);
                    btn_task_commit.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_task_btn));
                    break;
            }

        }


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
                        if (DEBUG) {
                            Log.e(TAG, "complet : " + response);
                        }

                        try {
                            JSONObject mCloseJson = new JSONObject(response);
                            int status = mCloseJson.optInt("status");
                            if (Constans.REQUEST_SUCCESS == status) {
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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
                        if (DEBUG) {
                            Log.e(TAG, "accept : " + response);
                        }

                        try {
                            JSONObject mCloseJson = new JSONObject(response);
                            int status = mCloseJson.optInt("status");
                            if (Constans.REQUEST_SUCCESS == status) {
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }


    /**
     * 关闭任务
     *调试完成
     * @param userId
     */
    private void closeMission(String userId, String missionId) {
        Map<String, String> mAcceptMission = new HashMap<>();
        mAcceptMission.put("userId", userId);
        mAcceptMission.put("missionId", missionId);

        OkHttpUtils.get()
                .url(Constans.CLOSE_MISSION)
                .params(mAcceptMission)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (DEBUG) {
                            Log.e(TAG, "close : " + response);
                        }

                        try {
                            JSONObject mCloseJson = new JSONObject(response);
                            int status = mCloseJson.optInt("status");
                            if (Constans.REQUEST_SUCCESS == status) {
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
