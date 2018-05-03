package com.wtwd.yusan.ease.net;


import com.wtwd.yusan.ease.net.callback.BaseResponseCallback;
import com.wtwd.yusan.ease.net.callback.StringCallback;

import java.util.Map;

/**
 * Created by XJM on 2018/4/17.
 */

public class ApiInterface {

    /**
     * 获取联系人列表
     */
    public static void getAddressList(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("getFriend", params, callback);
    }

    /**
     * 获取群聊列表
     */
    public static void getGroupList(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("getGroupList", params, callback);
    }

    /**
     * 获取群聊详情
     */
    public static void getGroupDetail(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("getGroupDetailInfo", params, callback);
    }

    /**
     * 建立群聊
     */
    public static void addGroup(Map<String, String> params, final BaseResponseCallback callback) {
        NetUtil.post("addGroup", params, callback);
    }

    /**
     * 添加群成员
     */
    public static void addMember(Map<String, String> params, final BaseResponseCallback callback) {
        NetUtil.post("addMember", params, callback);
    }


    /**
     * 退出群聊
     */
    public static void exitGroup(Map<String, String> params, final BaseResponseCallback callback) {
        NetUtil.post("exitGroup", params, callback);
    }

    /**
     * 删除群成员
     */
    public static void delMember(Map<String, String> params, final BaseResponseCallback callback) {
        NetUtil.post("delMember", params, callback);
    }


    /**
     * 修改群信息
     */
    public static void updateGroup(Map<String, String> params, final BaseResponseCallback callback) {
        NetUtil.post("updateGroup", params, callback);
    }

    /**
     * 用户发送红包
     */
    public static void payRedPacket(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("payRedPacket", params, callback);
    }

    /**
     * 用户抢红包
     */
    public static void getRedPacket(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("getRedPacket", params, callback);
    }

    /**
     * 领取任务
     */
    public static void acceptMission(Map<String, String> params, final BaseResponseCallback callback) {
        NetUtil.post("acceptMission", params, callback);
    }

    /**
     * 我的任务
     */
    public static void getMyMission(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("getMyMission", params, callback);
    }

    /**
     * 获取系统消息
     */

    public static void getSystemMessage(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("getSystemMessage", params, callback);
    }

    /**
     * 获取系统消息
     */
    public static void getNotice(Map<String, String> params, final StringCallback callback) {
        NetUtil.post("getNotice", params, callback);
    }

}

