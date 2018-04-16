package com.wtwd.yusan.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wtwd.yusan.R;
import com.wtwd.yusan.activity.FeedBackActivity;
import com.wtwd.yusan.activity.SettingActivity;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.widget.view.ArcImageView;
import com.wtwd.yusan.widget.view.CircleImageView;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener{
    private static MeFragment mInstance;
    /**
     * 圆形头像
     */
    private CircleImageView img_head_me;
    /**
     * 模糊背景
     */
    private ArcImageView img_me_head_bg;

    private LinearLayout lin_me_money;

    private LinearLayout lin_me_help_feedback;

    private LinearLayout lin_me_setting;

    public static MeFragment getMeFragment() {
        if (null == mInstance) {
            mInstance = new MeFragment();
        }
        return mInstance;
    }

    public MeFragment() {

    }


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_me;
    }

    @Override
    public void initFragmentView(Bundle savedInstanceState, View mView) {

        img_tool_bar_right.setBackgroundResource(R.mipmap.me_fragment_edit);
        img_tool_bar_right.setVisibility(View.VISIBLE);
        img_head_me = (CircleImageView) mView.findViewById(R.id.img_head_me);
        img_head_me.setBorderColor(R.color.color_grey);
        img_head_me.setBorderWidth(10);
        img_me_head_bg = (ArcImageView) mView.findViewById(R.id.img_me_head_bg);
        BitmapDrawable mDrawable = (BitmapDrawable) img_head_me.getBackground();

        Bitmap mBitmap = mDrawable.getBitmap();

        img_me_head_bg.setBitmap(mBitmap);
       // img_me_head_bg.setScaleX(View.SCALE_X);

        lin_me_money = mView.findViewById(R.id.lin_me_money);
        lin_me_help_feedback = mView.findViewById(R.id.lin_me_help_feedback);
        lin_me_setting = mView.findViewById(R.id.lin_me_setting);

        addListener();


    }

    /**
     * 监听器
     */
    private void addListener() {
        lin_me_setting.setOnClickListener(this);
        lin_me_money.setOnClickListener(this);
        lin_me_help_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_me_setting:
                Intent settingIntent = new Intent(getActivity(), SettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.lin_me_money:
                break;
            case R.id.lin_me_help_feedback:
                Intent feedbackIntent = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(feedbackIntent);
                break;
            case R.id.img_tool_bar_right:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
