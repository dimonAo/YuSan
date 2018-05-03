package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;

/**
 * Created by changle on 2018/4/28.
 */

public class RedPacketInfoCallPresenter extends EaseChatRowPresenter {
    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new RedPacketRowInfo(cxt, message, position, adapter);
    }

}

