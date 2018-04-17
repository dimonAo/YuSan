package com.wtwd.yusan.widget.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int count;

    public SpaceItemDecoration(int space, int count) {
        this.space = space;
        this.count = count;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space;
        outRect.bottom = space;
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        if (parent.getChildLayoutPosition(view) % (this.count) == 0) {
            outRect.left = 0;
        }
    }

}
