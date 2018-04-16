package com.wtwd.yusan.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.util.Constans;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

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
        tool_bar.setNavigationIcon(R.mipmap.task_back);
        img_tool_bar_right.setVisibility(View.GONE);
        setTitleToolbarStyle(tool_bar);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        onCreateCommonView(saveInstanceState);
    }

    public abstract void onCreateCommonView(Bundle saveInstanceState);


}
