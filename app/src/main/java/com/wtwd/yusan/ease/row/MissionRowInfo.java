package com.wtwd.yusan.ease.row;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.wtwd.yusan.R;

/**
 * Created by changle on 2018/4/28.
 */

public class MissionRowInfo extends EaseChatRow {

    private ImageView img;
    private TextView tv_money;
    public MissionRowInfo(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    /*
    ���layout
     */
    @Override
    protected void onInflateView() {
        inflater.inflate(
                R.layout.em_activity_send_envelopes, this);
    }

    @Override
    protected void onFindViewById() {
//        img = findViewById(R.id.img_packet);
        tv_money =  findViewById(R.id.tv_money);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onSetUpView() {
//        img.setImageResource(R.drawable.mission);
    }
}

