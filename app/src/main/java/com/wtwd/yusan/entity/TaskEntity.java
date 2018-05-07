package com.wtwd.yusan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskEntity implements Parcelable {

    private long mission_id;                  //任务ID

    private String content;         //任务描述

    private int type;               //任务类型

    private int sex;          //限制接受者性别1是男，2是女，3是不限

    private String address;          //地址

    private double money;                 //任务金额

//    private UserEntity publisher;               //任务发布者

    private String create_time;   //任务发布时间

    private long start_time; //任务开始时间

//    private UserEntity accepter;     //任务接收者，可以为空

    private int status;    //任务状态，0是待领取，1是进行中，2是确认完成，3是等待对方确认，4是已完成，5是已失效

    private String accept_time;//领取时间

    private String finish_time;//完成时间

    private int anonymous;//匿名装填，1是匿名，0是不匿名

    ////////////////////////////////////////////////////////////////
    private long publish_id;                 //用户ID

    private long user_id;

    private String userIdStr;

    private String head_img;            //头像

    private String birth;               //生日

    private String user_name;          //用户名

    private String password;          //密码

    private int user_sex;                 //性别

    private String nick_name;

    private String country;

//    private String create_time;

    private int invisible;           //是否隐身

////////////////////////////////////////////////////////////////////////

//    private UserEntity user;

    public TaskEntity() {

    }

    protected TaskEntity(Parcel in) {
        mission_id = in.readLong();
        content = in.readString();
        type = in.readInt();
        sex = in.readInt();
        address = in.readString();
        money = in.readDouble();
        create_time = in.readString();
        start_time = in.readLong();
        status = in.readInt();
        accept_time = in.readString();
        finish_time = in.readString();
        anonymous = in.readInt();
        publish_id = in.readLong();
        user_id = in.readLong();
        userIdStr = in.readString();
        head_img = in.readString();
        birth = in.readString();
        user_name = in.readString();
        password = in.readString();
        user_sex = in.readInt();
        nick_name = in.readString();
        country = in.readString();
        invisible = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mission_id);
        dest.writeString(content);
        dest.writeInt(type);
        dest.writeInt(sex);
        dest.writeString(address);
        dest.writeDouble(money);
        dest.writeString(create_time);
        dest.writeLong(start_time);
        dest.writeInt(status);
        dest.writeString(accept_time);
        dest.writeString(finish_time);
        dest.writeInt(anonymous);
        dest.writeLong(publish_id);
        dest.writeLong(user_id);
        dest.writeString(userIdStr);
        dest.writeString(head_img);
        dest.writeString(birth);
        dest.writeString(user_name);
        dest.writeString(password);
        dest.writeInt(user_sex);
        dest.writeString(nick_name);
        dest.writeString(country);
        dest.writeInt(invisible);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public long getMission_id() {
        return mission_id;
    }

    public void setMission_id(long mission_id) {
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
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

    public long getPublish_id() {
        return publish_id;
    }

    public void setPublish_id(long publish_id) {
        this.publish_id = publish_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getInvisible() {
        return invisible;
    }

    public void setInvisible(int invisible) {
        this.invisible = invisible;
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
                ", create_time='" + create_time + '\'' +
                ", start_time=" + start_time +
                ", status=" + status +
                ", accept_time='" + accept_time + '\'' +
                ", finish_time='" + finish_time + '\'' +
                ", anonymous=" + anonymous +
                ", publish_id=" + publish_id +
                ", user_id=" + user_id +
                ", userIdStr='" + userIdStr + '\'' +
                ", head_img='" + head_img + '\'' +
                ", birth='" + birth + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", user_sex=" + user_sex +
                ", nick_name='" + nick_name + '\'' +
                ", country='" + country + '\'' +
                ", invisible=" + invisible +
                '}';
    }
}
