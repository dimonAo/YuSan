package com.wtwd.yusan.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wtwd.yusan.R;

import com.aigestudio.wheelpicker.WheelPicker;
import com.wtwd.yusan.entity.ResultEntity;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Result;


public class Utils {

    public static ResultEntity getResultEntity(String jsonString) {
        return GsonUtils.GsonToBean(jsonString, ResultEntity.class);
    }


    /**
     * 0 参数为空
     * 1 系统繁忙
     * 2 该用户已经存在
     * 3 密码错误
     * 4 该用户不存在
     * 5 数据有误
     * 6 该任务不适合您
     * 7 无法建立群聊，因为存在不是互关的成员
     * 8 任务id不存在
     * 9 该任务已被领取
     * 10 您不具备操作该任务的权限
     * 11 不能领取自己的任务
     * 12 数据异常，无法确认完成
     * 13 群主id不存在
     * 14 你不是群主
     * 15 成员id不存在
     * <p>
     * 16红包id不存在
     * 17红包被领取
     * 18红包领取失败
     * 19订单生成失败
     * 20订单生成成功
     * 21余额不足
     * 22红包发送失败
     * 23提现额度错误
     * 24图片上传失败
     * 25群id不存在
     * 26群成员账号不存在
     * 27环信群创建失败
     * 28环信群修改失败
     * 29环信群成员删除失败
     * 30成员已存在
     * 31环信群成员添加失败
     * 32群组成员不能小于3个
     * 33该群已存在
     * 34注册失败
     * 35权限错误
     *
     * @param errorCode
     * @return
     */

    public static String getErrorString(Context context, int errorCode) {
        String mErrorInfo = "";
        switch (errorCode) {
            case 0:
                mErrorInfo = context.getString(R.string.util_parameter_null);
                break;
            case 1:
                mErrorInfo = context.getString(R.string.util_system_busy);
                break;
            case 2:
                mErrorInfo = context.getString(R.string.util_user_exists);

                break;
            case 3:
                mErrorInfo = context.getString(R.string.util_password_error);
                break;
            case 4:
                mErrorInfo = context.getString(R.string.util_user_not_exist);
                break;
            case 5:
                mErrorInfo = context.getString(R.string.util_incorrect_data);
                break;
            case 6:
                mErrorInfo = context.getString(R.string.util_task_not_suitable);
                break;

            case 7:
                mErrorInfo = context.getString(R.string.util_not_mutual_concern);
                break;

            case 8:
                mErrorInfo = context.getString(R.string.util_task_id_not_exists);
                break;

            case 9:
                mErrorInfo = context.getString(R.string.util_task_received);
                break;

            case 10:
                mErrorInfo = context.getString(R.string.util_operation_permission);
                break;
            case 11:
                mErrorInfo = context.getString(R.string.util_can_not_get_own_task);
                break;
            case 12:
                mErrorInfo = context.getString(R.string.util_abnormal_data);

                break;
            case 13:
                mErrorInfo = context.getString(R.string.util_group_owner_id_not_exists);
                break;
            case 14:
                mErrorInfo = context.getString(R.string.util_not_group_owner);
                break;
            case 15:
                mErrorInfo = context.getString(R.string.util_group_member_id_not_exists);
                break;
            case 16:
                mErrorInfo = context.getString(R.string.util_red_packet_id_not_exists);
                break;

            case 17:
                mErrorInfo = context.getString(R.string.util_red_packet_picked_up);
                break;

            case 18:
                mErrorInfo = context.getString(R.string.util_failed_receive_red_packet);
                break;

            case 19:
                mErrorInfo = context.getString(R.string.util_order_generation_failed);
                break;

            case 20:
                mErrorInfo = context.getString(R.string.util_order_generation_success);
                break;
            case 21:
                mErrorInfo = context.getString(R.string.util_insufficient_balance);
                break;
            case 22:
                mErrorInfo = context.getString(R.string.util_red_packet_sended_failed);
                break;
            case 23:
                mErrorInfo = context.getString(R.string.util_withdrawal_quota_wrong);
                break;
            case 24:
                mErrorInfo = context.getString(R.string.util_picture_upload_failed);
                break;
            case 25:
                mErrorInfo = context.getString(R.string.util_group_id_not_exists);
                break;
            case 26:
                mErrorInfo = context.getString(R.string.util_group_account_not_exists);
                break;

            case 27:
                mErrorInfo = context.getString(R.string.util_group_create_failed);
                break;

            case 28:
                mErrorInfo = context.getString(R.string.util_group_update_failed);
                break;

            case 29:
                mErrorInfo = context.getString(R.string.util_group_member_deleted_failed);
                break;
            case 30:
                mErrorInfo = context.getString(R.string.util_group_member_exists);
                break;
            case 31:
                mErrorInfo = context.getString(R.string.util_add_group_member_failed);
                break;
            case 32:
                mErrorInfo = context.getString(R.string.util_group_member_can_not_less_than_three);
                break;
            case 33:
                mErrorInfo = context.getString(R.string.util_group_exists);
                break;
            case 34:
                mErrorInfo = context.getString(R.string.util_register_fail);
                break;
            case 35:
                mErrorInfo = context.getString(R.string.util_permission_error);
                break;
        }
        return mErrorInfo;
    }

    /**
     * 获取任务类型
     * 0:拼车
     * 1：美食
     * 2：唱K
     * 3：游戏
     * 4：出游
     * 5：运动
     * 6：品酒
     * 7：其他
     *
     * @return
     */
    public static String getTaskString(int type) {
        String mErrorInfo = "";
        switch (type) {
            case 0:
                mErrorInfo = "拼车";
                break;
            case 1:
                mErrorInfo = "美食";
                break;
            case 2:
                mErrorInfo = "唱K";

                break;
            case 3:
                mErrorInfo = "游戏";
                break;
            case 4:
                mErrorInfo = "出游";
                break;
            case 5:
                mErrorInfo = "运动";
                break;
            case 6:
                mErrorInfo = "品酒";
                break;
            case 7:
                mErrorInfo = "其他";
                break;
        }
        return mErrorInfo;
    }


    /**
     * 手机号
     */
    public static boolean isMobileNO(String mobileNums) {
//        String telRegex = "[1][34578]\\d{9}";
        return !TextUtils.isEmpty(mobileNums) && mobileNums.matches("[1][34578]\\d{9}");
    }


    /**
     * 设置ToolBar Title居中
     *
     * @param context 上线文
     * @param title   title文字
     * @param toolbar ToolBar对象
     */
    public static void addMiddleTitle(Context context, CharSequence title, Toolbar toolbar) {

        TextView textView = new TextView(context);
        textView.setText(title);

        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        toolbar.addView(textView, params);

    }

    /**
     * 设置状态栏的颜色
     *
     * @param activity   当前界面
     * @param colorResId 颜色ID
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            return actionBarHeight;
        }
        return 0;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }


    /**
     * 反射设置TabLayout下划线的长度
     */
    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, int pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);  //+0.5是为了向上取整
    }

    /**
     * 判断邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param activity
     * @param colorId
     */
    public static void setStatusBarColor(Activity activity, int colorId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
//      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //Then call setStatusBarColor.
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(colorId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(colorId);
        }
    }

    /**
     * 状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MIUISetStatusBarLightMode(activity, true)) {
                result = 1;
            } else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     *
     * @param activity
     * @param type     1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity, true);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), true);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }

    /**
     * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标
     */
    public static void StatusBarDarkMode(Activity activity, int type) {
        if (type == 1) {
            MIUISetStatusBarLightMode(activity, false);
        } else if (type == 2) {
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        } else if (type == 3) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

    public static void setData(WheelPicker mHour, WheelPicker mMinute, int position, List<String> mHours, List<String> mMinutes) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        //当天时间
        if (0 == position) {
            mHours.clear();
            mMinutes.clear();
            int hour = date.getHours();
            int minute = date.getMinutes();

            //取余
            int minuteC = minute / 10;
            //取模
            int minuteY = minute % 10;

            if (0 == minuteY) {
                for (int i = minuteC; i < 6; i++) {
                    mMinutes.add(addZeroBeforeString((i * 10) + ""));
                }

                for (int i = 0; i < 6; i++) {
                    mMinutes.add(addZeroBeforeString((i * 10) + ""));
                }

            } else {
                if (5 == minuteC) {
                    for (int i = (hour + 1); i < 24; i++) {
                        mHours.add(addZeroBeforeString(i + ""));
                    }
                } else {
                    for (int i = hour; i < 24; i++) {
                        mHours.add(addZeroBeforeString(i + ""));
                    }

                    for (int i = (minuteC + 1); i < 6; i++) {
                        mMinutes.add(addZeroBeforeString((i * 10) + ""));
                    }
                }
            }
        } else {
            //明天或后天时间
            mMinutes.clear();
            mHours.clear();
            for (int i = 0; i < 24; i++) {
                mHours.add(addZeroBeforeString(i + ""));
            }

            for (int i = 0; i < 6; i++) {
                mMinutes.add(addZeroBeforeString((i * 10) + ""));
            }
        }

        mHour.setData(mHours);
        mMinute.setData(mMinutes);
    }


    public static void setWheelHour(WheelPicker wheel_day, WheelPicker wheel_hour, WheelPicker wheel_minute
            , int position, List<String> mHours, List<String> mMinutes) {

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        int posi = wheel_day.getCurrentItemPosition();
        int hour = date.getHours();
        int minute = date.getMinutes();

        if (0 == position) {
            if (0 == posi) {
                mMinutes.clear();
                //取余
                int minuteC = minute / 10;
                //取模
                int minuteY = minute % 10;

                if (0 == minuteY) {
                    for (int i = minuteC; i < 6; i++) {
                        mMinutes.add(addZeroBeforeString((i * 10) + ""));
                    }
                } else {
                    if (5 == minuteC) {
                        mHours.clear();
                        for (int i = (hour + 1); i < 24; i++) {
                            mHours.add(addZeroBeforeString(i + ""));
                        }

                        for (int i = 0; i < 6; i++) {
                            mMinutes.add(addZeroBeforeString((i * 10) + ""));
                        }

                        wheel_hour.setData(mHours);

                    } else {
                        for (int i = (minuteC + 1); i < 6; i++) {
                            mMinutes.add(addZeroBeforeString((i * 10) + ""));
                        }
                    }
                }


            } else {
                mMinutes.clear();
                for (int i = 0; i < 6; i++) {
                    mMinutes.add(addZeroBeforeString((i * 10) + ""));
                }
            }
        } else {
            mMinutes.clear();
            for (int i = 0; i < 6; i++) {
                mMinutes.add(addZeroBeforeString((i * 10) + ""));
            }
        }

        wheel_minute.setData(mMinutes);
        wheel_minute.setSelectedItemPosition(0);
    }


    public static String addZeroBeforeString(String string) {
        if (1 == string.length()) {
            string = "0" + string;
        }
        return string;
    }

    /**
     * 获取用户IP地址
     *
     * @param context
     * @return
     */
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        Date d = new Date();
        System.out.println(d);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(d);
        System.out.println("格式化后的日期：" + dateNowStr);
        return dateNowStr;
    }

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public static String Bitmap2StrByBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


    public static String getStorageDirectory(Context context) {
        Log.e("TAG", "Environment.MEDIA_MOUNTED : ---> " + Environment.MEDIA_MOUNTED);
        String appRootPath = context.getCacheDir().getPath();
        String sdRootPath = Environment.getExternalStorageDirectory().getPath();
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                sdRootPath : appRootPath;
    }


}
