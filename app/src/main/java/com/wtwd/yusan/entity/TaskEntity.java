package com.wtwd.yusan.entity;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskEntity {

    private Long mission_id;                  //任务ID

    private String content;         //任务描述

    private int type;               //任务类型

    private int sex;          //限制接受者性别1是男，2是女，3是不限

    private String address;          //地址

    private double money;                 //任务金额

    private UserEntity publisher;               //任务发布者

    private String create_time;   //任务发布时间

    private String start_time; //任务开始时间

    private UserEntity accepter;     //任务接收者，可以为空

    private int status;    //任务状态，0是待领取，1是进行中，2是确认完成，3是等待对方确认，4是已完成，5是已失效


    public Long getMission_id() {
        return mission_id;
    }

    public void setMission_id(Long mission_id) {
        this.mission_id = mission_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public UserEntity getPublisher() {
        return publisher;
    }

    public void setPublisher(UserEntity publisher) {
        this.publisher = publisher;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public UserEntity getAccepter() {
        return accepter;
    }

    public void setAccepter(UserEntity accepter) {
        this.accepter = accepter;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "mission_id=" + mission_id +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", sex=" + sex +
                ", address='" + address + '\'' +
                ", money=" + money +
                ", publisher=" + publisher +
                ", create_time='" + create_time + '\'' +
                ", start_time='" + start_time + '\'' +
                ", accepter=" + accepter +
                ", status=" + status +
                '}';
    }
}
