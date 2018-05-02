package com.wtwd.yusan.entity;

/**
 * time:2018/4/12
 * Created by w77996
 */

public class LastVersionEntity {
    private Long   loc_id;                  //地址ID

    private String locIdStr;

    private Double lat;         //维度

    private Double lng;             //经度

    private String last_time;      //最后一次位置的时间

    private Long user_id;          //用户id

    private long publish_id;                 //用户ID

    private String userIdStr;

    private String head_img;            //头像

    private String birth;               //生日

    private String user_name;          //用户名

    private String password;          //密码

    private int sex;                 //性别

    private String country;

//    private String create_time;

    private int invisible;           //是否隐身


    public Long getLoc_id() {
        return loc_id;
    }

    public void setLoc_id(Long loc_id) {
        this.loc_id = loc_id;
    }

    public String getLocIdStr() {
        return locIdStr;
    }

    public void setLocIdStr(String locIdStr) {
        this.locIdStr = locIdStr;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public long getPublish_id() {
        return publish_id;
    }

    public void setPublish_id(long publish_id) {
        this.publish_id = publish_id;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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
        return "LastVersionEntity{" +
                "loc_id=" + loc_id +
                ", locIdStr='" + locIdStr + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", last_time='" + last_time + '\'' +
                ", user_id=" + user_id +
                ", publish_id=" + publish_id +
                ", userIdStr='" + userIdStr + '\'' +
                ", head_img='" + head_img + '\'' +
                ", birth='" + birth + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", country='" + country + '\'' +
                ", invisible=" + invisible +
                '}';
    }
}
