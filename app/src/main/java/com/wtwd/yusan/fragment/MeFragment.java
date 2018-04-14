package com.wtwd.yusan.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.widget.view.ArcImageView;
import com.wtwd.yusan.widget.view.CircleImageView;

/**
 * Created by Administrator on 2018/4/10 0010.
 */

public class MeFragment extends BaseFragment {
    private static MeFragment mInstance;
    private CircleImageView img_head_me;
    private ArcImageView img_me_head_bg;

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
        img_head_me = (CircleImageView) mView.findViewById(R.id.img_head_me);
        img_me_head_bg = (ArcImageView) mView.findViewById(R.id.img_me_head_bg);
        BitmapDrawable mDrawable = (BitmapDrawable) img_head_me.getBackground();

        Bitmap mBitmap = mDrawable.getBitmap();

        img_me_head_bg.setBitmap(mBitmap);


    }
}
