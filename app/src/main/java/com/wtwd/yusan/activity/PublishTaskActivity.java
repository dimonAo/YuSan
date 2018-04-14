package com.wtwd.yusan.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.util.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.List;

public class PublishTaskActivity extends CommonToolBarActivity implements View.OnClickListener {
    private RelativeLayout relative_sex;
    private TextView text_sex;

    private RelativeLayout relative_location;
    private TextView text_province;

    private RelativeLayout relative_time;
    private TextView text_time;

    private EditText edit_detail;
    private TextView text_count;

    private RecyclerView recycler_task_type;

    private Dialog mDialog;
    private List<String> mSexChoose = new ArrayList<>();

    private TaskRecyclerAdapter mTaskRecyclerAdapter;

    private int[] mDrawables = {R.drawable.selector_task_car
            , R.drawable.selector_task_singing
            , R.drawable.selector_task_game
            , R.drawable.selector_task_travel
            , R.drawable.selector_task_sport
            , R.drawable.selector_task_wine
            , R.drawable.selector_task_other};

    private String[] mTaskNames;
    private int mSelectPos;
    private List<TaskRecyclerEntity> mTasks = new ArrayList<>();

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_publish_task;
    }

    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        text_tool_bar_title.setText("发布");
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

        edit_detail = (EditText) findViewById(R.id.edit_detail);
        edit_detail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.e("TAG", "s : " + s);
//                Log.e("TAG", "count : " + count);
                text_count.setText(String.format("%S/15个字",s.length() + ""));

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


        getTaskTypeData();
    }

    private void addListener() {
        relative_sex.setOnClickListener(this);
        relative_location.setOnClickListener(this);
        relative_time.setOnClickListener(this);


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
