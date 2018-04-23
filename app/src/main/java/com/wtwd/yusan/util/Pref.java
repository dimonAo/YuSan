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
    public void setUserId(int userId) {
        getSharedPreferencesCommon().edit().putInt("user_id", userId).apply();
    }

    /**
     * 获取user_id
     *
     * @return 存储的user_id
     */
    public int getUserId() {
        return getSharedPreferencesCommon().getInt("user_id", 0);
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
}
