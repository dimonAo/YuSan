package com.wtwd.yusan.util;

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

import com.aigestudio.wheelpicker.widgets.WheelAreaPicker;
import com.wtwd.yusan.R;
import com.wtwd.yusan.widget.hscorll.ScrollPickerView;
import com.wtwd.yusan.widget.hscorll.StringScrollPicker;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class DialogUtil {

    private static String mSex;

    public static void dialogShowPublishSex(Activity mActivity, final Dialog mDialog, final List<String> mSexs, final TextView mTextView) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_sex_choose, null, false);

        TextView text_commit = (TextView) view.findViewById(R.id.text_commit);
        StringScrollPicker string_picker_sex = (StringScrollPicker) view.findViewById(R.id.string_picker_sex);
        string_picker_sex.setData(mSexs);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);

        string_picker_sex.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
            @Override
            public void onSelected(ScrollPickerView scrollPickerView, int position) {
                mSex = mSexs.get(position);

            }
        });
        text_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextView.setText(mSex);
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
