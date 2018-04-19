package com.wtwd.yusan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskEntity implements Parcelable {

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

    private String accept_time;//领取时间

    private String finish_time;//完成时间

    private int anonymous;//匿名装填，1是匿名，0是不匿名


    public TaskEntity() {

    }

    protected TaskEntity(Parcel in) {
        if (in.readByte() == 0) {
            mission_id = null;
        } else {
            mission_id = in.readLong();
        }
        content = in.readString();
        type = in.readInt();
        sex = in.readInt();
        address = in.readString();
        money = in.readDouble();
        create_time = in.readString();
        start_time = in.readString();
        status = in.readInt();
        accept_time = in.readString();
        finish_time = in.readString();
        anonymous = in.readInt();
    }

    public static final Creator<TaskEntity> CREATOR = new Creator<TaskEntity>() {
        @Override
        public TaskEntity createFromParcel(Parcel in) {
            return new TaskEntity(in);
        }

        @Override
        public TaskEntity[] newArray(int size) {
            return new TaskEntity[size];
        }
    };

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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
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

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    public String getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(String finish_time) {
        this.finish_time = finish_time;
    }

    public int getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(int anonymous) {
        this.anonymous = anonymous;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mission_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mission_id);
        }
        dest.writeString(content);
        dest.writeInt(type);
        dest.writeInt(sex);
        dest.writeString(address);
        dest.writeDouble(money);
        dest.writeString(create_time);
        dest.writeString(start_time);
        dest.writeInt(status);
        dest.writeString(accept_time);
        dest.writeString(finish_time);
        dest.writeInt(anonymous);
    }
}
