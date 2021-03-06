package com.wtwd.yusan.util;


import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private Context cx;
    /**
     * SharedPreferences存储xml文件名字
     */
    private static final String shareXml = "cheekat";
    private static Pref instance;

    private Pref(Context context) {
        this.cx = context;
    }

    /**
     * 单例对象
     *
     * @param context context
     * @return Pref对象
     */
    public static Pref getInstance(Context context) {
        if (instance == null) {
            synchronized (Pref.class) {
                if (instance == null) {
                    instance = new Pref(context.getApplicationContext());
                }
            }
        }
        return instance;

    }

    /**
     * 获得SharedPreferences对象，并设置存储权限为私有
     *
     * @return
     */
    public SharedPreferences getSharedPreferencesCommon() {

        return cx.getSharedPreferences(shareXml, Context.MODE_PRIVATE);
    }

    /**
     * 存储电话号码
     *
     * @param phoneNumber 电话号码
     */
    public void setPhoneNumber(String phoneNumber) {

        getSharedPreferencesCommon().edit().putString("phone_number", phoneNumber).apply();
    }

    /**
     * 获取存储的电话号码
     *
     * @return
     */
    public String getPhoneNumber() {
        return getSharedPreferencesCommon().getString("phone_number", "0");
    }

    /**
     * 存储user_id
     *
     * @param userId
     */
    public void setUserId(long userId) {
        getSharedPreferencesCommon().edit().putLong("user_id", userId).apply();
    }

    /**
     * 获取user_id
     *
     * @return 存储的user_id
     */
    public long getUserId() {
//        return getSharedPreferencesCommon().getLong("user_id", 1);
        return getSharedPreferencesCommon().getLong("user_id", 0L);
    }


    /**
     * 存储退出任务广场界面时，已存在的任务数据
     *
     * @param mTaskJson
     */
    public void setTaskJson(String mTaskJson) {
        getSharedPreferencesCommon().edit().putString("task_json", mTaskJson).apply();
    }

    /**
     * 获取最后一次浏览的任务数据
     *
     * @return
     */
    public String getTaskJson() {
        return getSharedPreferencesCommon().getString("task_json", "0");
    }

    /**
     * 存储退出我的任务界面时，已存在的任务数据
     *
     * @param mTaskJson
     */
    public void setMeTaskJson(String mTaskJson) {
        getSharedPreferencesCommon().edit().putString("task_me_json", mTaskJson).apply();
    }

    /**
     * 获取最后一次浏览的我的任务数据
     *
     * @return
     */
    public String getMeTaskJson() {
        return getSharedPreferencesCommon().getString("task_me_json", "0");
    }

    /**
     * 获取存储的城市
     *
     * @return
     */
    public String getCity() {
        return getSharedPreferencesCommon().getString("city", "深圳");
    }


    /**
     * 存储城市
     *
     * @param city 城市
     */
    public void setCity(String city) {

        getSharedPreferencesCommon().edit().putString("city", city).apply();
    }

    /**
     * 存储微信登录的Token值
     *
     * @param accessToken
     */
    public void setLoginAccessToken(String accessToken) {
        getSharedPreferencesCommon().edit().putString("accessToken", accessToken).apply();
    }

    /**
     * 获取存储的Token值
     *
     * @return
     */
    public String getLoginAccessToken() {
        return getSharedPreferencesCommon().getString("accessToken", "none");
    }


    public void setIsPhone(boolean flag){
        getSharedPreferencesCommon().edit().putBoolean("phone",flag).apply();
    }

    public boolean getIsPhone(){
        return getSharedPreferencesCommon().getBoolean("phone",false);
    }

    /**
     * 存储open_id
     *
     * @param openId
     */
    public void setOpenId(long openId) {
        getSharedPreferencesCommon().edit().putLong("open_id", openId).apply();
    }

    /**
     * 获取open_id
     *
     * @return 存储的open_id
     */
    public long getOpenId() {
        return getSharedPreferencesCommon().getLong("open_id", 0L);
    }

    /**
     * 存储wxEntity
     *
     * @param wxEntity
     */
    public void setWxEntity(String wxEntity) {
        getSharedPreferencesCommon().edit().putString("wx", wxEntity).apply();
    }

    /**
     * 获取wxEntity
     *
     * @return 存储的wxEntity
     */
    public String getWxEntity() {
//        return getSharedPreferencesCommon().getLong("user_id", 1);
        return getSharedPreferencesCommon().getString("wx", "");
    }
}
