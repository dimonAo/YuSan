package com.wtwd.yusan.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amap.api.maps.AMapUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.entity.LastVersionEntity;
import com.wtwd.yusan.entity.NearbyEntity;
import com.wtwd.yusan.util.Utils;

import java.util.List;

/**
 * time:2018/4/11
 * Created by w77996
 */

public class NearbyListAdapter extends BaseQuickAdapter<LastVersionEntity,BaseViewHolder> {

    public NearbyListAdapter(int layoutResId, @Nullable List<LastVersionEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, LastVersionEntity item) {
        helper .setText(R.id.tv_nearbylist_publisher_nick, item.getNick_name())
                .setText(R.id.tv_nearbylist_distance,(int) item.getDistance()+"");

        if ("2".equals(item.getSex()+"")){
            helper.setBackgroundRes(R.id.img_nearbylist_publisher_sex, R.mipmap.task_f);
        } else {
            helper.setBackgroundRes(R.id.img_nearbylist_publisher_sex, R.mipmap.task_m);
        }

//        Glide.with(mContext)
//                .load(Uri.parse(item.getHead_img()))
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        helper.setImageBitmap(R.id.circle_img_task_publisher,resource);
//                    }
//                });
//        Glide.with(mContext)
//                .load(Uri.parse(item.getHead_img()))
//                .into((ImageView) helper.getView(R.id.circle_img_task_publisher));
        Glide.with(mContext)
                .load(Uri.parse(item.getHead_img()))
                .into((ImageView) helper.getView(R.id.circle_img_nearbylist_publisher));
    }
}
