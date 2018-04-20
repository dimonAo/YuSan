package com.wtwd.yusan.entity;


import java.util.List;

public class ResultEntity<T> {

    private String msg;
    private int status;

    private int errCode;
    private List<T> object;

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

    public List<T> getObject() {
        return object;
    }

    public void setObject(List<T> object) {
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
