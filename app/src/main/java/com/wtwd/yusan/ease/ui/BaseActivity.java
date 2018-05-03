
package com.wtwd.yusan.ease.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hyphenate.easeui.ui.EaseBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@SuppressLint("Registered")
public class BaseActivity extends EaseBaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public <T> T getObject(String response, Class<T> clazz) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				String obj = jsonObject.has("object") ? jsonObject.getString("object") : "";
				if (TextUtils.isEmpty(obj)) {
					String msg = jsonObject.getString("msg");
					Toast.makeText(this, msg + "", Toast.LENGTH_SHORT);
					return null;
				}
				return JSON.parseObject(obj, clazz);
			} else {
				String msg = jsonObject.getString("msg");
				Toast.makeText(this, msg + "", Toast.LENGTH_SHORT);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	;

	public <T> List<T> getListObject(String response, Class<T> clazz) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			int status = jsonObject.getInt("status");
			if (status == 1) {
				String obj = jsonObject.has("object") ? jsonObject.getString("object") : "";
				if (TextUtils.isEmpty(obj)) {
					String msg = jsonObject.getString("msg");
					Toast.makeText(this, msg + "", Toast.LENGTH_SHORT);
					return null;
				}
				return JSON.parseArray(obj, clazz);
			} else {
				String msg = jsonObject.getString("msg");
				Toast.makeText(this, msg + "", Toast.LENGTH_SHORT);
			}
		} catch (JSONException e) {
			Toast.makeText(this, e + "", Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		return null;
	}

}