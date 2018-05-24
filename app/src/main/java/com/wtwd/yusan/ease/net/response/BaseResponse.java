package com.wtwd.yusan.ease.net.response;

/**
 * Created by XJM on 2018/4/22.
 */

public class BaseResponse {
    private String msg;		//请求信息
    private int status;		//信息编码 1 请求成功 0 请求失败
    private int errCode	;	//错误信息编码，当status为0时有该字段

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
