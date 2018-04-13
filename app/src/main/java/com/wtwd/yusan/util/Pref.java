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
    public void setUserId(String userId) {
        getSharedPreferencesCommon().edit().putString("user_id", userId).apply();
    }

    /**
     * 获取user_id
     *
     * @return 存储的user_id
     */
    public String getUserId() {
        return getSharedPreferencesCommon().getString("user_id", "0");
    }

}
