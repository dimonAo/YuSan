package com.wtwd.yusan.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelAreaPicker;
import com.bumptech.glide.util.Util;
import com.wtwd.yusan.R;
import com.wtwd.yusan.util.Utils;
import com.wtwd.yusan.widget.hscorll.ScrollPickerView;
import com.wtwd.yusan.widget.hscorll.StringScrollPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class DialogUtil {

//    private static String mSex;

    public static void dialogShowPublishSex(Activity mActivity, final Dialog mDialog, final List<String> mSexs, final TextView mTextView) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sex_choose, null, false);
//        final String[] mSexs = mActivity.getResources().getStringArray(R.array.task_sex);

        TextView text_commit = (TextView) view.findViewById(R.id.text_commit);
//        StringScrollPicker string_picker_sex = (StringScrollPicker) view.findViewById(R.id.string_picker_sex);
        final WheelPicker wheel_picker_sex = (WheelPicker) view.findViewById(R.id.wheel_picker_sex);
        wheel_picker_sex.setData(mSexs);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);


        text_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(mSexs.get(wheel_picker_sex.getCurrentItemPosition()));
                mDialog.dismiss();
            }
        });


        mDialog.show();


        setDialogMaxheightLayoutParams(mActivity, mDialog);
    }

    public static void dialogShowProvince(Activity mActivity, final Dialog mDialog, final TextView mTextView) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_province_choose, null, false);

        TextView text_commit = (TextView) view.findViewById(R.id.text_commit);
        final WheelAreaPicker wheel_area_picker = (WheelAreaPicker) view.findViewById(R.id.wheel_area_picker);
        wheel_area_picker.setItemTextSize((int) mActivity.getResources().getDimension(R.dimen.text_14));
        wheel_area_picker.hideArea();
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        text_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(wheel_area_picker.getCity());
                mDialog.dismiss();
            }
        });


        mDialog.show();
        setDialogMaxheightLayoutParams(mActivity, mDialog);

    }


    public static void dialogShowDate(Activity mActivity, final Dialog mDialog, final TextView mTextView) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_day_hour_choose, null, false);
        final WheelPicker wheel_day = (WheelPicker) view.findViewById(R.id.wheel_day);
        final WheelPicker wheel_hour = (WheelPicker) view.findViewById(R.id.wheel_hour);
        final WheelPicker wheel_minute = (WheelPicker) view.findViewById(R.id.wheel_minute);
        TextView text_commit = (TextView) view.findViewById(R.id.text_commit);
        final List<String> mHours = new ArrayList<>();
        final List<String> mMinutes = new ArrayList<>();
        final List<String> mMonth = new ArrayList<>();

        String[] a = mActivity.getResources().getStringArray(R.array.data_array);
        for (int i = 0; i < a.length; i++) {
            mMonth.add(a[i]);
        }


        //日期切换监听
        wheel_day.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                Utils.setData(wheel_hour, wheel_minute, position, mHours, mMinutes);
            }
        });

        //小时切换监听
        wheel_hour.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                Utils.setWheelHour(wheel_day, wheel_hour, wheel_minute, position, mHours, mMinutes);
            }
        });

        wheel_day.setData(mMonth);
        Utils.setData(wheel_hour, wheel_minute, 0, mHours, mMinutes);
//        Utils.setWheelHour(wheel_day, wheel_hour, wheel_minute, 0, mHours, mMinutes);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        text_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTextView.setText(String.format("%s %s:%s"
                        , mMonth.get(wheel_day.getCurrentItemPosition())
                        , mHours.get(wheel_hour.getCurrentItemPosition())
                        , mMinutes.get(wheel_minute.getCurrentItemPosition())));
                mDialog.dismiss();
            }
        });

        mDialog.show();
        setDialogMaxheightLayoutParams(mActivity, mDialog);
    }


    public static void setDialogMaxheightLayoutParams(Activity mActivity, Dialog selectedDialog) {
        Window dialogWindow = selectedDialog.getWindow();
        WindowManager m = mActivity.getWindowManager();

        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();

//        if (p.height > (d.getHeight() * 0.4)) {
        p.height = (int) (d.getHeight() * 0.4);
//        }
        p.width = (int) (d.getWidth());
        p.gravity = Gravity.BOTTOM;
        p.y = 0; //设置Dialog与底部的margin值，与左右一致

        //设置Dialog本身透明度
//        p.alpha = 0.5f;
        dialogWindow.setAttributes(p);
    }


}
