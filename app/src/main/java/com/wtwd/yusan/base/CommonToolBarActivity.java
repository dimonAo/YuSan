package com.wtwd.yusan.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.yusan.R;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public abstract class CommonToolBarActivity extends BaseActivity {
    public Toolbar tool_bar;
    public ImageView img_tool_bar_right;
    public TextView text_tool_bar_title;

    @Override
    public void onCreateView(Bundle saveInstanceState) {
        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
        img_tool_bar_right = (ImageView) findViewById(R.id.img_tool_bar_right);
        text_tool_bar_title = (TextView) findViewById(R.id.text_tool_bar_title);
        setTitleToolbarStyle(tool_bar);

        onCreateCommonView(saveInstanceState);
    }

    public abstract void onCreateCommonView(Bundle saveInstanceState);
}
