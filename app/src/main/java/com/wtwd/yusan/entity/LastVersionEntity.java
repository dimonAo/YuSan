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

    private UserEntity user;    //用户

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
