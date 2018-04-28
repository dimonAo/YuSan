package com.wtwd.yusan.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.wtwd.yusan.R;
import com.wtwd.yusan.util.Pref;
import com.wtwd.yusan.util.Utils;


/**
 * Created by Administrator on 2018/1/26 0026.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    public static final boolean DEBUG = true;
    public static final int PURE_PICTURE_TITLE = 1;
    public static final int SOLID_COLOR_TITLE = 2;
    public Pref mPref;

    Toast toast;

//    public Toolbar tool_bar;
//    public ImageView img_tool_bar_left;
//    public ImageView img_tool_bar_right;
//    public TextView text_tool_bar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutResourceId() != 0) {
            setContentView(getLayoutResourceId());
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        mPref = Pref.getInstance(this);

//        tool_bar = (Toolbar) findViewById(R.id.tool_bar);
//        img_tool_bar_right = (ImageView) findViewById(R.id.img_tool_bar_right);
//        text_tool_bar_title = (TextView) findViewById(R.id.text_tool_bar_title);
//        initToolBar(tool_bar);
//        changeTitleBarColor();
        onCreateView(savedInstanceState);
    }

//    public abstract void initToolBar(Toolbar toolbar);

    public abstract int getLayoutResourceId();

    public abstract void onCreateView(Bundle saveInstanceState);

    public abstract View getSnackView();


    /**
     * show the success message,the text color is white
     */
    public void showSnackBarShort(String msg) {
        if (null != msg && !TextUtils.isEmpty(msg)) {
//            Snackbar.make(getSnackView(), msg, Snackbar.LENGTH_SHORT).show();
            customSuccessSnackBar(msg).show();
        }
    }

    /**
     * show the error or fail message,the text color is red
     */
    public void showSnackBarLong(String msg) {
        if (null != msg && !TextUtils.isEmpty(msg)) {
//            Snackbar.make(getSnackView(), msg, Snackbar.LENGTH_LONG).show();
            customFailSnackBar(msg).show();
        }
    }

    private Snackbar customSuccessSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(getSnackView(), msg, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textView.setTextColor(Color.WHITE);

        return snackbar;
    }

    private Snackbar customFailSnackBar(String msg) {
        Snackbar snackbar = Snackbar.make(getSnackView(), msg, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        textView.setTextColor(Color.RED);

        return snackbar;
    }

    public void setTitleToolbarStyle(int type, int colorId, Toolbar tool_bar) {

        if (type == PURE_PICTURE_TITLE) {
            changeTitleBarColor();
        } else if (type == SOLID_COLOR_TITLE) {
            tool_bar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
            Utils.setWindowStatusBarColor(this, colorId);
            Utils.setStatusBarColor(this, colorId);
        }
        Utils.setMargins(tool_bar, 0, Utils.getStatusBarHeight(this), 0, 0);
        Utils.transparencyBar(this);
//        Utils.StatusBarLightMode(this);
    }

    public void setTitleToolbarStyle(Toolbar tool_bar) {

        changeTitleBarColor();
        Utils.setMargins(tool_bar, 0, Utils.getStatusBarHeight(this), 0, 0);
        Utils.transparencyBar(this);
//        if (type == PURE_PICTURE_TITLE) {
//        } else if (type == SOLID_COLOR_TITLE) {
//            tool_bar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
//            Utils.setWindowStatusBarColor(this, colorId);
//            Utils.setStatusBarColor(this, colorId);
//        }
//        Utils.StatusBarLightMode(this);
    }


    private void changeTitleBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility
                    (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /***
     * start activity
     * @param clazz target activity
     */
    public void readyGo(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * start activity with bundle
     *
     * @param clazz  target activity
     * @param bundle bundle
     */
    public void readyGo(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * start activity then finish
     *
     * @param clazz target activity
     */
    public void readyGoThenKill(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * start activity with bundle than finish
     *
     * @param clazz  target activity
     * @param bundle bundle
     */
    public void readyGoThenKill(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * start activity for result
     *
     * @param clazz       target activity
     * @param requestCode request code
     */
    public void readyGoForResult(Class<? extends Activity> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }


    /**
     * start activity for result with bundle
     *
     * @param clazz       target activity
     * @param requestCode request code
     * @param bundle      bundle
     */
    public void readyGoForResult(Class<? extends Activity> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    public void readyGoForNewTask(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void readyGoForNewTask(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 自定义toast
     *
     * @param msg
     */
    public void showToast(String msg) {

        cancelToast(toast);
        toast = new Toast(this);
        //设置Toast显示位置，居中，向 X、Y轴偏移量均为0
        toast.setGravity(Gravity.CENTER, 0, 0);
        //获取自定义视图
        View view = LayoutInflater.from(this).inflate(R.layout.toast_setting_clear, null);

        TextView tvMessage = (TextView) view.findViewById(R.id.tv_message_toast);
        //设置文本
        tvMessage.setText(msg);
        //设置视图
        toast.setView(view);
        //设置显示时长
        toast.setDuration(Toast.LENGTH_SHORT);
        //显示
        toast.show();
    }

    private void cancelToast(Toast toast) {
        if (null != toast) {
            toast.cancel();
            toast = null;
        }
    }
}
