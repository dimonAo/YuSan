package com.wtwd.yusan.ease.net.callback;


import com.wtwd.yusan.ease.net.response.BaseResponse;

/**
 * Created by XJM on 2018/4/22.
 */

public interface BaseResponseCallback {
    void onSuccess(BaseResponse response);
    void onError(String error);
}
