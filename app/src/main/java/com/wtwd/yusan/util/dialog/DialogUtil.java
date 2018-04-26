package com.wtwd.yusan.util.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
//        wheel_picker_sex.setSelectedItemPosition();
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


    /**
     * 滚轮选择日期
     *
     * @param mActivity
     * @param mDialog
     * @param mTextView
     */
    public static void dialogChooseBirthday(Activity mActivity, final Dialog mDialog, final TextView mTextView) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_date_choose, null, false);
        final WheelPicker wheel_day = (WheelPicker) view.findViewById(R.id.wheel_day);
        final WheelPicker wheel_year = (WheelPicker) view.findViewById(R.id.wheel_year);
        final WheelPicker wheel_month = (WheelPicker) view.findViewById(R.id.wheel_month);

        TextView text_commit = (TextView) view.findViewById(R.id.text_commit);

        final List<String> mYears = new ArrayList<>();
        final List<String> mMonths = new ArrayList<>();
        final List<String> mDays = new ArrayList<>();

        final Calendar mCalendar = Calendar.getInstance();
        final int year = mCalendar.get(Calendar.YEAR);
        final int month = mCalendar.get(Calendar.MONTH) + 1;
        final int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        //                                     2018                       03                        26
        Log.e("TAG", "year : --> " + year + "   month : --> " + month + "   day : --> " + day);

        //year data
        for (int i = (year - 100); i <= year; i++) {
            mYears.add(Utils.addZeroBeforeString(i + ""));
        }

        for (int i = 1; i <= 12; i++) {
            mMonths.add(Utils.addZeroBeforeString(i + ""));
        }

        for (int i = 1; i <= getDaysByYearMonth(2000, 1); i++) {
            mDays.add(Utils.addZeroBeforeString(i + ""));
        }

        wheel_year.setData(mYears);
        wheel_month.setData(mMonths);
        wheel_day.setData(mDays);

        //month data
        wheel_year.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mMonths.clear();
                if (Integer.parseInt(mYears.get(position)) == year) {

                    for (int i = 1; i <= month; i++) {
                        mMonths.add(Utils.addZeroBeforeString(i + ""));
                    }
                    wheel_month.setData(mMonths);

                    mDays.clear();
                    Log.e("month", "month : ---> " + Integer.parseInt(mMonths.get(wheel_month.getCurrentItemPosition())));
                    if ((Integer.parseInt(mYears.get(position)) == year)
                            && (Integer.parseInt(mMonths.get(wheel_month.getCurrentItemPosition())) == month)) {
                        for (int i = 1; i <= day; i++) {
                            mDays.add(Utils.addZeroBeforeString(i + ""));
                        }

                    } else {
                        Log.e("days", "days : ---> " + getDaysByYearMonth(Integer.parseInt(mYears.get(wheel_year.getCurrentItemPosition()))
                                , Integer.parseInt(mMonths.get(wheel_month.getCurrentItemPosition()))));
                        for (int i = 1;
                             i <= getDaysByYearMonth(Integer.parseInt(mYears.get(position))
                                     , Integer.parseInt(mMonths.get(wheel_month.getCurrentItemPosition())));
                             i++) {
                            mDays.add(Utils.addZeroBeforeString(i + ""));
                        }
                    }

                    wheel_day.setData(mDays);
                    wheel_day.setSelectedItemPosition(0, false);

                } else {
                    mDays.clear();
                    for (int i = 1; i <= 12; i++) {
                        mMonths.add(Utils.addZeroBeforeString(i + ""));
                    }

                    for (int i = 1;
                         i <= getDaysByYearMonth(Integer.parseInt(mYears.get(position))
                                 , Integer.parseInt(mMonths.get(wheel_month.getCurrentItemPosition())));
                         i++) {
                        mDays.add(Utils.addZeroBeforeString(i + ""));
                    }

                    wheel_month.setData(mMonths);
                    wheel_day.setData(mDays);
                }


            }
        });

        //day data
        wheel_month.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mDays.clear();
                if ((Integer.parseInt(mYears.get(wheel_year.getCurrentItemPosition())) == year)
                        && (Integer.parseInt(mMonths.get(position)) == month)) {
                    for (int i = 1; i <= day; i++) {
                        mDays.add(Utils.addZeroBeforeString(i + ""));
                    }

                } else {
                    for (int i = 1; i <= getDaysByYearMonth(Integer.parseInt(mYears.get(wheel_year.getCurrentItemPosition())), Integer.parseInt(mMonths.get(position))); i++) {
                        mDays.add(Utils.addZeroBeforeString(i + ""));
                    }
                }

                wheel_day.setData(mDays);
//                wheel_day.setSelectedItemPosition(0);
            }


        });

        wheel_year.setSelectedItemPosition(82, false);

        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        text_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(String.format("%s-%s-%s"
                        , mYears.get(wheel_year.getCurrentItemPosition())
                        , mMonths.get(wheel_month.getCurrentItemPosition())
                        , mDays.get(wheel_day.getCurrentItemPosition())));
                mDialog.dismiss();
            }
        });

        mDialog.show();
        setDialogMaxheightLayoutParams(mActivity, mDialog);
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
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
        Utils.setWheelHour(wheel_day, wheel_hour, wheel_minute, 0, mHours, mMinutes);
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
