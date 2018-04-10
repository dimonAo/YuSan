package com.wtwd.yusan.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtwd.yusan.R;
import com.wtwd.yusan.util.Utils;


/**
 * Created by Administrator on 2018/1/26 0026.
 */

public abstract class BaseFragment extends Fragment {
    public Toolbar tool_bar;
    //    public ImageView img_tool_bar_left;
    public ImageView img_tool_bar_right;
    public TextView text_tool_bar_title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutResourceId() != 0) {
//            changeTitleBarColor();
            View mView = inflater.inflate(getLayoutResourceId(), container, false);
            tool_bar = (Toolbar) mView.findViewById(R.id.tool_bar);
            img_tool_bar_right = (ImageView) mView.findViewById(R.id.img_tool_bar_right);
            text_tool_bar_title = (TextView) mView.findViewById(R.id.text_tool_bar_title);
            img_tool_bar_right.setVisibility(View.GONE);
            Utils.setMargins(tool_bar, 0, Utils.getStatusBarHeight(getActivity()), 0, 0);
            setTitleToolbarStyle(tool_bar);

            initFragmentView(savedInstanceState, mView);

            return mView;
        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id for Fragment");
        }
    }

    public abstract int getLayoutResourceId();

    public abstract void initFragmentView(Bundle savedInstanceState, View mView);


    private void changeTitleBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility
                    (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
//        Utils.StatusBarLightMode(getActivity());
    }

    public void setTitleToolbarStyle(Toolbar tool_bar) {

        changeTitleBarColor();
        Utils.setMargins(tool_bar, 0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        Utils.transparencyBar(getActivity());
//        if (type == PURE_PICTURE_TITLE) {
//        } else if (type == SOLID_COLOR_TITLE) {
//            tool_bar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
//            Utils.setWindowStatusBarColor(this, colorId);
//            Utils.setStatusBarColor(this, colorId);
//        }
//        Utils.StatusBarLightMode(this);
    }


    /***
     * start activity
     * @param clazz target activity
     */
    public void readyGo(Class<? extends Activity> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * start activity with bundle
     *
     * @param clazz  target activity
     * @param bundle bundle
     */
    public void readyGo(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
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
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * start activity with bundle than finish
     *
     * @param clazz  target activity
     * @param bundle bundle
     */
    public void readyGoThenKill(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * start activity for result
     *
     * @param clazz       target activity
     * @param requestCode request code
     */
    public void readyGoForResult(Class<? extends Activity> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
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
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

}
