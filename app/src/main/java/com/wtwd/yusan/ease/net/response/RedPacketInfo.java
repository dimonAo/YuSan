package com.wtwd.yusan.ease.net.response;


/**
 * Created by changle on 2018/4/28.
 */

public class RedPacketInfo {
    private String redpacketIdStr;

    private String record_sn;//流水订单号

    private Integer type;               //任务类型

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    private Double money;                 //金额

    private Long publish_id;               //发布者

    private String create_time;   //发布时间

    private Long accept_id;     //任务接收者，可以为空

    private Integer  status;    //任务状态，0是待领取，1已领取，

    private String accept_time;

    private User user;    //用户

    public void setRedpacketIdStr(String redpacketIdStr) {
        this.redpacketIdStr = redpacketIdStr;
    }

    public void setRecord_sn(String record_sn) {
        this.record_sn = record_sn;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setPublish_id(Long publish_id) {
        this.publish_id = publish_id;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setAccept_id(Long accept_id) {
        this.accept_id = accept_id;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public void setUser(User user) {
        this.user = user;
    }
}