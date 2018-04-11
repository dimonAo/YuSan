package com.wtwd.yusan.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.entity.NearbyEntity;

import java.util.List;

/**
 * time:2018/4/11
 * Created by w77996
 */

public class NearbyListAdapter extends BaseQuickAdapter<NearbyEntity,BaseViewHolder> {

    public NearbyListAdapter(int layoutResId, @Nullable List<NearbyEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearbyEntity item) {
        helper.setBackgroundRes(R.id.circle_img_nearbylist_publisher, R.mipmap.task_head)
                .setText(R.id.tv_nearbylist_publisher_nick, item.getName())
                .setText(R.id.tv_nearbylist_distance,"1111");

        if ("0".equals(item.getSex())) {
            helper.setBackgroundRes(R.id.img_nearbylist_publisher_sex, R.mipmap.task_f);
        } else {
            helper.setBackgroundRes(R.id.img_nearbylist_publisher_sex, R.mipmap.task_m);
        }

    }
}
