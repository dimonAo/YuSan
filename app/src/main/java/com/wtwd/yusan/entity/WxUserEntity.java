package com.wtwd.yusan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * time:2018/5/8
 * Created by w77996
 */

public class WxUserEntity implements Parcelable{

    private String openid;
    private String nickname;
    private int sex ;
    private String headimgurl;
    private String city;

    protected WxUserEntity(Parcel in) {
        openid = in.readString();
        nickname = in.readString();
        sex = in.readInt();
        headimgurl = in.readString();
        city = in.readString();
    }

    public static final Creator<WxUserEntity> CREATOR = new Creator<WxUserEntity>() {
        @Override
        public WxUserEntity createFromParcel(Parcel in) {
            return new WxUserEntity(in);
        }

        @Override
        public WxUserEntity[] newArray(int size) {
            return new WxUserEntity[size];
        }
    };

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "WxUserEntity{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", headimgurl='" + headimgurl + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(openid);
        dest.writeString(nickname);
        dest.writeInt(sex);
        dest.writeString(headimgurl);
        dest.writeString(city);
    }
}
