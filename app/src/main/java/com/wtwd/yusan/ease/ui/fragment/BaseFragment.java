package com.wtwd.yusan.ease.ui.fragment;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by XJM on 2018/4/23.
 */

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(),container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent(view);
        initData();

    }

    @LayoutRes
    public abstract int getLayoutResId();

    public abstract void initView(View view);

    public abstract void initEvent(View view);

    public abstract void initData();

    public <T> T getObject(String response,Class<T> clazz){
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if(status == 1) {
                String obj = jsonObject.has("object") ? jsonObject.getString("object") : "";
                if (TextUtils.isEmpty(obj)) {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getActivity(),msg+"",Toast.LENGTH_SHORT);
                    return null;
                }
                return JSON.parseObject(obj,clazz);
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getActivity(),msg+"",Toast.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    };

    public <T> List<T> getListObject(String response,Class<T> clazz){
        try {
            JSONObject jsonObject = new JSONObject(response);
            int status = jsonObject.getInt("status");
            if(status == 1) {
                String obj = jsonObject.has("object") ? jsonObject.getString("object") : "";
                if (TextUtils.isEmpty(obj)) {
                    String msg = jsonObject.getString("msg");
                    Toast.makeText(getActivity(),msg+"",Toast.LENGTH_SHORT);
                    return null;
                }
                return JSON.parseArray(obj,clazz);
            }else {
                String msg = jsonObject.getString("msg");
                Toast.makeText(getActivity(),msg+"",Toast.LENGTH_SHORT);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    };

}
