package com.wtwd.yusan.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hyphenate.util.EMLog;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.ui.IndexFragment;
import com.wtwd.yusan.ease.ui.LoginActivity;
import com.wtwd.yusan.fragment.MeFragment;
import com.wtwd.yusan.fragment.MessageFragment;
import com.wtwd.yusan.fragment.NearbyMapFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private static final int MAIN_MESSAGE = 0;
    private static final int MAIN_NEAR = 1;
    private static final int MAIN_ME = 2;


    private Button btn_message;
    private Button btn_near;
    private Button btn_me;


    private List<Button> mButtons = new ArrayList<>();
    private List<BaseFragment> mFragments = new ArrayList<>();
    protected static final String TAG = "MainActivity";
    // textview for unread message count
    private TextView unreadLabel;
    // textview for unread event message
//	private TextView unreadAddressLable;

    private IndexFragment indexFragment;

    // user logged into another device
    public boolean isConflict = false;
    // user account was removed
    private boolean isCurrentAccountRemoved = false;

    /**
     * check if current user account was remove
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        }

        //make sure activity will not in background if user is logged into another device or removed
        if (getIntent() != null &&
                (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                        getIntent().getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false))) {
            IMHelper.getInstance().logout(false,null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (getIntent() != null && getIntent().getBooleanExtra("isConflict", false)) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);

        initView();
        showExceptionDialogFromIntent(getIntent());
    }


    private void initView() {
        btn_me = (Button) findViewById(R.id.btn_me);
        btn_message = (Button) findViewById(R.id.btn_message);
        btn_near = (Button) findViewById(R.id.btn_near);

        addListener();
        initPage();
    }

    private void addListener() {
        mButtons.clear();
        mButtons.add(btn_message);
        mButtons.add(btn_near);
        mButtons.add(btn_me);

        btn_me.setOnClickListener(this);
        btn_message.setOnClickListener(this);
        btn_near.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.btn_message == id) {
            changePage(MAIN_MESSAGE);
        } else if (R.id.btn_near == id) {
            changePage(MAIN_NEAR);
        } else if (R.id.btn_me == id) {
            changePage(MAIN_ME);
        }
    }

    private void initPage() {
        prepareFragment();
        changePage(MAIN_NEAR);
    }

    public void changePage(int page) {
        updateFragment(page);
    }

    private void prepareFragment() {
        mFragments.clear();
        // TODO: 2018/a1/26 0026 添加fragment实例到mFragments集合中
        mFragments.add(IndexFragment.getFragment());
        mFragments.add(NearbyMapFragment.newInstance());
        mFragments.add(MeFragment.getMeFragment());
        for (BaseFragment fragment : mFragments) {
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).hide(fragment).commit();
        }
    }

    /**
     * 切换fragment,且设置button selector状态
     */
    private void updateFragment(int position) {
        if (position > mFragments.size() - 1) {
            return;
        }
        for (int i = 0; i < mFragments.size(); i++) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            BaseFragment fragment = mFragments.get(i);
            if (i == position) {
                mButtons.get(i).setSelected(true);
//                mButtons.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorMainAtyBottomTextSelect));

                transaction.show(fragment);
            } else {
                mButtons.get(i).setSelected(false);
//                mButtons.get(i).setTextColor(ContextCompat.getColor(this, R.color.colorMainAtyBottomTextDefault));
                transaction.hide(fragment);
            }
            transaction.commit();
        }
    }

    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow =  false;
    private BroadcastReceiver internalDebugReceiver;

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private int getExceptionMessageId(String exceptionType) {
        if(exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }
    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        IMHelper.getInstance().logout(false,null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        finish();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

}
