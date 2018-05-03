package com.wtwd.yusan.ease.net.callback;

/**
 * Created by XJM on 2018/4/26.
 */

public interface StringCallback {
    void onSuccess(String response);
    void onError(String error);
}
