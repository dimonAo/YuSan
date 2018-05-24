package com.wtwd.yusan.ease.util.swipe;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.wtwd.yusan.MyApplication;


/**
 * Created by pc-20170420 on 2017/10/26.
 */

public class DisplayUtil {
    private static DisplayMetrics displaysMetrics = null;
    private static float scale = -1.0F;
    private static int statusBarHeight = 0;

    public static int dip2px(float paramFloat)
    {
        if (scale < 0.0F)
        {
            if (displaysMetrics == null) {
                getDisplayMetrics();
            }
            scale = displaysMetrics.density;
        }
        return (int)(0.5F + paramFloat * scale);
    }

    @SuppressLint("WrongConstant")
    public static DisplayMetrics getDisplayMetrics()
    {
        if (displaysMetrics == null)
        {
            displaysMetrics = new DisplayMetrics();
            ((WindowManager) MyApplication.applicationContext.getSystemService("window")).getDefaultDisplay().getMetrics(displaysMetrics);
        }
        return displaysMetrics;
    }

    public static int getHeight()
    {
        if (displaysMetrics == null) {
            getDisplayMetrics();
        }
        return displaysMetrics.heightPixels;
    }

    public static int getStatusBarHeight()
    {
        if ((statusBarHeight == 0) && (MyApplication.applicationContext != null)) {
            statusBarHeight = getStatusBarHeight(MyApplication.applicationContext.getResources());
        }
        return statusBarHeight;
    }

    private static int getStatusBarHeight(Resources paramResources)
    {
        try
        {
            Class localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int i = paramResources.getDimensionPixelSize(Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString()));
            return i;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return 0;
    }

    public static int getWidth()
    {
        if (displaysMetrics == null) {
            getDisplayMetrics();
        }
        return displaysMetrics.widthPixels;
    }

    public static int px2dip(float paramFloat)
    {
        if (scale < 0.0F)
        {
            if (displaysMetrics == null) {
                getDisplayMetrics();
            }
            scale = displaysMetrics.density;
        }
        return (int)(0.5F + paramFloat / scale);
    }

    public static int sp2px(float paramFloat)
    {
        if (displaysMetrics == null) {
            getDisplayMetrics();
        }
        return (int)(0.5F + paramFloat * displaysMetrics.scaledDensity);
    }
}
