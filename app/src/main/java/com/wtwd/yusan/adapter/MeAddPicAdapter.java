package com.wtwd.yusan.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.imagepicker.bean.ImageItem;
import com.wtwd.yusan.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MeAddPicAdapter extends BaseQuickAdapter<ImageItem, BaseViewHolder> {



    public MeAddPicAdapter(int layoutResId, @Nullable List<ImageItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageItem item) {

        Glide.with(mContext)
                .load(Uri.parse(item.path))
                .into((ImageView) helper.getView(R.id.img_item_recycler));

    }
}
