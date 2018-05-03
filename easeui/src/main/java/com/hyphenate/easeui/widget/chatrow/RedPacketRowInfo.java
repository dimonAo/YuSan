package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;

/**
 * Created by changle on 2018/4/28.
 */

public class RedPacketRowInfo extends EaseChatRow {

    private ImageView img;

    public RedPacketRowInfo(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    /*
    填充layout
     */
    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.em_activity_reciv_envelopes:
                R.layout.em_activity_send_envelopes, this);
    }

    /*
    查找chatrow中的控件
     */
    @Override
    protected void onFindViewById() {
        img = (ImageView) findViewById(R.id.img_packet);
    }

    /*
    消息状态改变，刷新listView
     */
    @Override
    protected void onViewUpdate(EMMessage msg) {
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onSetUpView() {
//        EMTextMessageBody textMessageBody = (EMTextMessageBody) message.getBody();
//        message.getStringAttribute(RedPacketConstant.RedPacketExtType, null);
//        int drawable=message.getIntAttribute(RedPacketConstant.RedPacketImgUrl,0);
//        String money=message.getStringAttribute(RedPacketConstant.RedPacketMoney,null);
//        img.setImageResource(R.drawable.reb_packet);

        img.setImageResource(R.drawable.reb_packet);
    }

}