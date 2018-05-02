package com.wtwd.yusan.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.amap.api.maps.AMapUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.entity.LastVersionEntity;
import com.wtwd.yusan.entity.NearbyEntity;

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
    protected void convert(BaseViewHolder helper, LastVersionEntity item) {
        helper.setBackgroundRes(R.id.circle_img_nearbylist_publisher, R.mipmap.task_head)
                .setText(R.id.tv_nearbylist_publisher_nick, item.getUser_name())
                .setText(R.id.tv_nearbylist_distance,"fsadf");

        if ("0".equals(item.getSex()+"")){
            helper.setBackgroundRes(R.id.img_nearbylist_publisher_sex, R.mipmap.task_f);
        } else {
            helper.setBackgroundRes(R.id.img_nearbylist_publisher_sex, R.mipmap.task_m);
        }

    }
}
