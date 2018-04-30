package com.wtwd.yusan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.ResultEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.util.dialog.DialogUtil;
import com.wtwd.yusan.widget.view.MoneyTextWatcher;
import com.wtwd.yusan.widget.view.SwitchView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class PublishTaskActivity extends CommonToolBarActivity implements View.OnClickListener {
    private static final int RED_PACKET_REQUEST_CODE = 0x01;

    private RelativeLayout relative_sex;
    private TextView text_sex;

    private RelativeLayout relative_location;
    private TextView text_province;

    private RelativeLayout relative_time;
    private TextView text_time;

    private RelativeLayout relative_cost;
    private TextView text_money;

    private EditText edit_detail;
    private TextView text_count;

    private Button btn_publish;
    private SwitchView switchview_anonymous;

    private EditText edit_money;
    private TextView text_unit;


    private RecyclerView recycler_task_type;

    private Dialog mDialog;
    private List<String> mSexChoose = new ArrayList<>();

    private TaskRecyclerAdapter mTaskRecyclerAdapter;

    private int[] mDrawables = {R.drawable.selector_task_car
            , R.drawable.selector_task_food
            , R.drawable.selector_task_singing
            , R.drawable.selector_task_game
            , R.drawable.selector_task_travel
            , R.drawable.selector_task_sport
            , R.drawable.selector_task_wine
            , R.drawable.selector_task_other};

    private String[] mTaskNames;
    private int mSelectPos, mSelectSex;
    private List<TaskRecyclerEntity> mTasks = new ArrayList<>();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_publish_task;
    }

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        text_tool_bar_title.setText(R.string.publish_title);
        mTaskNames = getResources().getStringArray(R.array.task_type);

        getDate();
        initView();
        addListener();
    }

    private void getDate() {
        String[] mSexs = getResources().getStringArray(R.array.task_sex);
        for (int i = 0; i < mSexs.length; i++) {
            mSexChoose.add(i, mSexs[i]);
        }
    }


    private void initView() {
        mDialog = new Dialog(this, R.style.MyCommonDialog);

        relative_sex = (RelativeLayout) findViewById(R.id.relative_sex);
        text_sex = (TextView) findViewById(R.id.text_sex);

        relative_location = (RelativeLayout) findViewById(R.id.relative_location);
        text_province = (TextView) findViewById(R.id.text_province);

        relative_time = (RelativeLayout) findViewById(R.id.relative_time);
        text_time = (TextView) findViewById(R.id.text_time);

        btn_publish = (Button) findViewById(R.id.btn_publish);
        switchview_anonymous = (SwitchView) findViewById(R.id.switchview_anonymous);

        relative_cost = (RelativeLayout) findViewById(R.id.relative_cost);
        text_money = (TextView) findViewById(R.id.text_money);

        text_unit = (TextView) findViewById(R.id.text_unit);
        edit_money = (EditText) findViewById(R.id.edit_money);
        edit_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edit_money.setSelection(s.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2 + 1);
                        edit_money.setText(s);
                        edit_money.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    edit_money.setText(s);
                    edit_money.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        edit_money.setText(s.subSequence(0, 1));
                        edit_money.setSelection(1);
                        return;
                    }
                }

//                if (getString(R.string.publish_choose_time).equals(text_time.getText().toString())
//                        || edit_detail.getText().toString().isEmpty()
//                        || ((!s.toString().isEmpty()) && (Double.parseDouble(s.toString()) > 200))
//                        || ((!s.toString().isEmpty()) && (Double.parseDouble(s.toString()) <= 0))) {
//                    btn_publish.setEnabled(false);
//                } else {
//                    btn_publish.setEnabled(true);
//                }
//
//                if (getString(R.string.publish_choose_time).equals(text_time.getText().toString())) {
//                    showToast("请先选择任务开始时间");
////                    edit_money.setText("");
//                    return;
//                }
//
//                if (edit_detail.getText().toString().isEmpty()) {
////                    showToast("");
//                    return;
//                }
//
                if ((!s.toString().isEmpty()) && (Double.parseDouble(s.toString()) > 200)) {
                    showToast("金额不能大于200");
                    return;
                }

                if ((!s.toString().isEmpty()) && (Double.parseDouble(s.toString()) <= 0)) {
                    showToast("金额不能为0");
                    return;
                }

//                if (getString(R.string.publish_choose_time).equals(text_time.getText().toString())
//                        || edit_detail.getText().toString().isEmpty()
//                        || (Double.parseDouble(s.toString()) > 200)
//                        || (Double.parseDouble(s.toString()) <= 0)){
//                    btn_publish.setEnabled(false);
//                }else{
//                    btn_publish.setEnabled(true);
//                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(edit_money.getText().toString())) {
                    text_unit.setVisibility(View.GONE);
                } else {
                    text_unit.setVisibility(View.VISIBLE);
                }

            }
        });

        edit_detail = (EditText) findViewById(R.id.edit_detail);
        edit_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("TAG", "s : " + s);
//                Log.e("TAG", "count : " + count);
                text_count.setText(String.format(getString(R.string.publish_content_count), s.length() + ""));

//                if (getString(R.string.publish_choose_time).equals(text_time.getText().toString())
//                        || edit_detail.getText().toString().isEmpty()
//                        || (Double.parseDouble(edit_money.getText().toString()) > 200)
//                        || (Double.parseDouble(edit_money.getText().toString()) <= 0)) {
//                    btn_publish.setEnabled(false);
//                } else {
//                    btn_publish.setEnabled(true);
//                }
//
//                if (getString(R.string.publish_choose_time).equals(text_time.getText().toString())) {
//                    showToast("请先选择任务开始时间");
////                    edit_money.setText("");
//                    return;
//                }
//
//                if (edit_detail.getText().toString().isEmpty()) {
////                    showToast("");
//                    return;
//                }
//
//                if ((Double.parseDouble(edit_money.getText().toString().trim()) > 200)) {
//                    showToast("金额不能大于200");
//                    return;
//                }
//
//                if ((Double.parseDouble(edit_money.getText().toString().trim()) <= 0)) {
//                    showToast("金额不能为0");
//                    return;
//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        text_count = (TextView) findViewById(R.id.text_count);

        recycler_task_type = (RecyclerView) findViewById(R.id.recycler_task_type);
        recycler_task_type.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTaskRecyclerAdapter = new TaskRecyclerAdapter(R.layout.item_task_type_choose, mTasks);
        recycler_task_type.setAdapter(mTaskRecyclerAdapter);

        //设置性别条件缺省值
        text_sex.setText(mSexChoose.get(2));
        text_province.setText(Pref.getInstance(this).getCity());

        getTaskTypeData();
    }

    private void addListener() {
        relative_sex.setOnClickListener(this);
        relative_location.setOnClickListener(this);
        relative_time.setOnClickListener(this);
//        relative_cost.setOnClickListener(this);
        btn_publish.setOnClickListener(this);


        mTaskRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mSelectPos = position;
                mTaskRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getTaskTypeData() {
        mTasks.clear();
        for (int i = 0; i < mDrawables.length; i++) {
            TaskRecyclerEntity en = new TaskRecyclerEntity();
            en.setmDrawableId(mDrawables[i]);
            en.setmTaskName(mTaskNames[i]);
            mTasks.add(en);
        }
        mTaskRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public View getSnackView() {
        return null;
    }


    private int setColor(int color) {
        return ContextCompat.getColor(this, color);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_sex:
                DialogUtil.dialogShowPublishSex(this, mDialog, mSexChoose, text_sex);
                break;

            case R.id.relative_location:
                Dialog mDialog1 = new Dialog(this, R.style.MyCommonDialog);
                DialogUtil.dialogShowProvince(this, mDialog1, text_province);
                break;

            case R.id.relative_time:
                Dialog mDialog2 = new Dialog(this, R.style.MyCommonDialog);
                DialogUtil.dialogShowDate(this, mDialog2, text_time);
                break;

            case R.id.btn_publish:
                checkInputContent();
                break;

            case R.id.relative_cost:
                readyGoForResult(RedPacketActivity.class, RED_PACKET_REQUEST_CODE);
                break;
        }
    }

    /**
     * 1.判断任务条件所填写项是否填写完成
     * 2.请求服务器
     */
    private void checkInputContent() {

        if (checkTaskDetail()) {
            publishTask(Pref.getInstance(this).getUserId(), "0");
        }
//        publishTask(1L, "0");
    }

    private boolean checkTaskDetail() {

        if ("选择时间".equals(text_time.getText().toString())) {
            showToast(getString(R.string.publish_choose_start_time));
            return false;
        }

        if (edit_money.getText().toString().isEmpty()) {
            showToast("金额不能为空");
            return false;
        }

        if ((!edit_money.getText().toString().isEmpty()) && (Double.parseDouble(edit_money.getText().toString().trim()) > 200)) {
            showToast("金额不能大于200");
            return false;
        }

        if ((!edit_money.getText().toString().isEmpty()) && (Double.parseDouble(edit_money.getText().toString().trim()) <= 0)) {
            showToast("金额不能为0");
            return false;
        }

        if (TextUtils.isEmpty(getTaskDetailContent())) {
            showToast(getString(R.string.publish_input_task_content));
            return false;
        }


        return true;
    }


    //获取任务描述
    private String getTaskDetailContent() {
        return edit_detail.getText().toString();
    }

    /**
     * 获取任务类型
     * 0:拼车
     * 1：美食
     * 2：唱K
     * 3：游戏
     * 4：出游
     * 5：运动
     * 6：品酒
     * 7：其他
     *
     * @return
     */
    public String getTaskType() {
        return mSelectPos + "";
    }

    /**
     * 获取任务性别限制
     *
     * @return
     */
    private String getSexType() {
        int mSelectPos = 0;
        String mSexStr = text_sex.getText().toString();
        for (int i = 0; i < mSexChoose.size(); i++) {
            if (mSexStr.equals(mSexChoose.get(i))) {
                mSelectPos = i;
                break;
            }
        }

        return (mSelectPos + 1) + "";
    }

    /**
     * 获取任务金额
     *
     * @return
     */
    private String getTaskMoney() {
        return edit_money.getText().toString();
    }

    /**
     * 获取任务地址
     *
     * @return
     */
    private String getTaskAddress() {
        return text_province.getText().toString();
    }

    /**
     * 是否匿名
     *
     * @return
     */
    private String getTaskAnonymous() {
        int mAnony = 0;
        if (switchview_anonymous.isOpened()) {
            mAnony = 1;
        } else {
            mAnony = 0;
        }

        return mAnony + "";
    }

    /**
     * 将今天、明天、后天转成日期
     *
     * @return
     */
    private String getTaskStartTime() {
        String mStartDateAndTime = null;
        String time = text_time.getText().toString();
        String mStartDate = time.substring(0, 2);
        String mStartTime = time.substring(2, time.length());

        String[] mm = getResources().getStringArray(R.array.data_array);
        int mPosition = 0;

        for (int i = 0; i < mm.length; i++) {
            if (mStartDate.equals(mm[i])) {
                mPosition = i;
                break;
            }
        }


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, mPosition);

        mStartDateAndTime = calendar.get(Calendar.YEAR) + "-"
                + Utils.addZeroBeforeString((calendar.get(Calendar.MONTH) + 1) + "")
                + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + mStartTime;


        return mStartDateAndTime;
    }

    private void publishTask(long userId, String toId) {
        Bundle bundle = new Bundle();
//        Map<String, String> mPublishMap = new HashMap<>();
        bundle.putString("userId", userId + "");
        bundle.putString("content", getTaskDetailContent()); //任务描述
        bundle.putString("type", getTaskType()); //任务类型
        bundle.putString("sex", getSexType()); //接受者性别限制
        bundle.putString("money", getTaskMoney());//任务金额
        bundle.putString("address", getTaskAddress());//任务地址
        bundle.putString("startTime", getTaskStartTime());//任务开始时间 yyyy-MM-dd HH:mm格式
        bundle.putString("to", toId); //发送给谁，0所有人，指定人传用户userid
        bundle.putString("anonymous", getTaskAnonymous()); //是否匿名，1匿名 ； 0不匿名
        if (DEBUG) {
            Log.e(TAG, "mPublishMap : " + bundle.toString());
        }

        readyGo(RedPacketActivity.class, bundle);

//        OkHttpUtils.get()
//                .url(Constans.PUBLISH_MISSION)
//                .params(mPublishMap)
//                .build()
//                .connTimeOut(Constans.TIME_OUT)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        if (DEBUG) {
//                            Log.e(TAG, "publish task : " + response);
//                        }
//
//                        ResultEntity mEn = Utils.getResultEntity(response);
//
//                        if (1 == mEn.getStatus()) {
//                            showToast(getString(R.string.publish_commit));
//                            finish();
//                        } else {
//                            String mError = Utils.getErrorString(mEn.getErrCode());
//                            showToast(mError);
//                        }
//
//
//                    }
//                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (null != data) {
            if (RED_PACKET_REQUEST_CODE == requestCode && RedPacketActivity.RED_PACKET_RESULT_CODE == resultCode) {

                Bundle bundle = data.getExtras();
                if (null != bundle) {
                    String money = bundle.getString("money");
                    text_money.setText(money);
                }
            }
        }

    }

    private class TaskRecyclerAdapter extends BaseQuickAdapter<TaskRecyclerEntity, BaseViewHolder> {

        public TaskRecyclerAdapter(int layoutResId, @Nullable List<TaskRecyclerEntity> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TaskRecyclerEntity item) {
            helper.setText(R.id.text_task_recycler, item.getmTaskName())
                    .setImageDrawable(R.id.img_task_recycler, ContextCompat.getDrawable(PublishTaskActivity.this, item.getmDrawableId()));

            if (mSelectPos == helper.getLayoutPosition()) {
                helper.setTextColor(R.id.text_task_recycler, setColor(R.color.colorWhite));
                helper.getView(R.id.img_task_recycler).setSelected(true);
                helper.setBackgroundColor(R.id.lin_recycler, setColor(R.color.colorTask));
            } else {
                helper.setTextColor(R.id.text_task_recycler, setColor(R.color.colorTaskContent));
                helper.getView(R.id.img_task_recycler).setSelected(false);
                helper.setBackgroundColor(R.id.lin_recycler, setColor(R.color.colorWhite));
            }
        }
    }


    private class TaskRecyclerEntity {

        private String mTaskName;
        private int mDrawableId;

        public String getmTaskName() {
            return mTaskName;
        }

        public void setmTaskName(String mTaskName) {
            this.mTaskName = mTaskName;
        }

        public int getmDrawableId() {
            return mDrawableId;
        }

        public void setmDrawableId(int mDrawableId) {
            this.mDrawableId = mDrawableId;
        }
    }


}
