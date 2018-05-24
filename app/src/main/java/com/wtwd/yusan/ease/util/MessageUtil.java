package com.wtwd.yusan.ease.util;


import android.text.TextUtils;
import android.util.Log;

import com.wtwd.yusan.ease.Constant;


/**
 * Created by ChangLe on 2018/4/28.
 */

public class MessageUtil {

    public static MessageUtil MESSAGEUTIL = new MessageUtil();//单例

    public static MessageUtil getMessageUtil() {
        return MESSAGEUTIL;
    }

    public boolean isRedPacketMsg(String msg) {
        if (TextUtils.isEmpty(msg)) return false;
        String content = msg.substring(msg.indexOf(":") + 1, msg.length());
        int postion = content.indexOf(":");
        if (TextUtils.isEmpty(content)) return false;
        if (postion == -1) return false;
        String tag = content.substring(1, postion);
        if (tag.equals(Constant.WTWD_REDPACKET_TXT)) {
            return true;
        }
        return false;
    }

    public String getRedPacketMsgId(String msg) {
        if (TextUtils.isEmpty(msg)) return null;
        String content = msg.substring(msg.indexOf(":") + 1, msg.length());
        int postion = content.indexOf(":");
        if (TextUtils.isEmpty(content) && postion != -1) return null;
        String tag = content.substring(1, postion);
        if (tag.equals(Constant.WTWD_REDPACKET_TXT)) {
            String packet_id = content.substring(content.lastIndexOf(":") + 1, content.length() - 1);
            Log.i("MessageUtil", "packet_id--" + packet_id);
            return packet_id;
        }
        return null;
    }

    public boolean isMissionMsg(String msg) {
        if (TextUtils.isEmpty(msg)) return false;
        String content = msg.substring(msg.indexOf(":") + 1, msg.length());
        if (TextUtils.isEmpty(content)) return false;
        int postion = content.indexOf(":");
        if (postion == -1) return false;
        String tag = content.substring(1, content.indexOf(":"));
        if (tag.equals(Constant.WTWD_MISSION_TXT)) {
            return true;
        }
        return false;
    }

    public String getMissionMsgId(String msg) {
        if (TextUtils.isEmpty(msg)) return null;
        String content = msg.substring(msg.indexOf(":") + 1, msg.length());
        if (TextUtils.isEmpty(content)) return null;
        int postion = content.indexOf(":");
        if (postion == -1) return null;
        String tag = content.substring(1, content.indexOf(":"));
        if (tag.equals(Constant.WTWD_MISSION_TXT)) {
            String mission_id = content.substring(content.lastIndexOf(":") + 1, content.length() - 1);
            Log.i("MessageUtil", "mission_id--" + mission_id);
            return mission_id;
        }
        return null;
    }
}

