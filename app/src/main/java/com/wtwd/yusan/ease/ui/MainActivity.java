/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wtwd.yusan.ease.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.util.EMLog;
import com.wtwd.yusan.R;
import com.wtwd.yusan.ease.Constant;
import com.wtwd.yusan.ease.IMHelper;
import com.wtwd.yusan.ease.permission.PermissionsManager;
import com.wtwd.yusan.ease.permission.PermissionsResultAction;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {

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
		setContentView(R.layout.em_activity_main);
		// runtime permission for android 6.0, just require all permissions here for simple
		requestPermissions();

		initView();

		showExceptionDialogFromIntent(getIntent());


//		UserDao userDao = new UserDao(this);

		indexFragment = IndexFragment.newInstance();

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, indexFragment)
				.show(indexFragment)
				.commit();
		//debug purpose only
//        registerInternalDebugReceiver();
	}

	@TargetApi(23)
	private void requestPermissions() {
		PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
			@Override
			public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDenied(String permission) {
				//Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * init views
	 */
	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
	}

	@Override
	public void back(View view) {
		super.back(view);
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("isConflict", isConflict);
		outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(false);
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
		if (!MainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (exceptionBuilder == null)
				    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
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
	
	/**
	 * debug purpose only, you can ignore this
	 */
	private void registerInternalDebugReceiver() {
	    internalDebugReceiver = new BroadcastReceiver() {
            
            @Override
            public void onReceive(Context context, Intent intent) {
                IMHelper.getInstance().logout(false,new EMCallBack() {
                    
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                finish();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        });
                    }
                    
                    @Override
                    public void onProgress(int progress, String status) {}
                    
                    @Override
                    public void onError(int code, String message) {}
                });
            }
        };
        IntentFilter filter = new IntentFilter(getPackageName() + ".em_internal_debug");
        registerReceiver(internalDebugReceiver, filter);
    }

	@Override 
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
	}
}
