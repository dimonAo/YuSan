package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.presenter.EaseChatRowPresenter;

/**
 * Created by jin on 2018/4/28.
 */

public class MissionInfoCallPresenter extends EaseChatRowPresenter {
    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new MissionRowInfo(cxt, message, position, adapter);
    }
    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);
        Log.i("changle","MissionInfoCallPresenter");
    }
}

