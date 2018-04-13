package com.wtwd.yusan.entity;

/**
<<<<<<< HEAD
 * time:2018/4/13
 * Created by w77996
=======
 * Created by Administrator on 2018/4/13 0013.
>>>>>>> upstream/master
 */

public class ResultEntity {

    private String msg;
    private int status;

    private int errCode;
    private String object;

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

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "msg='" + msg + '\'' +
                ", status=" + status +
                ", errCode=" + errCode +
                ", object='" + object + '\'' +
                '}';
    }
}
