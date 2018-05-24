package com.wtwd.yusan.ease.net.response;

/**
 * Created by XJM on 2018/4/17.
 */

public class User {
    private Long   user_id;                 //用户ID
    private String userIdStr;
    private String head_img;            //头像
    private String birth;               //生日
    private String user_name;          //用户名
    private String password;          //密码
    private int sex;                 //性别 1是男，2是女
    private String country;
    private String create_time;
    private int invisible;           //是否隐身

    public Long getUserid() {
        return user_id;
    }

    public void setUserid(Long user_id) {
        this.user_id = user_id;
    }

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    public String getHeadImg() {
        return head_img;
    }

    public void setHeadImg(String head_img) {
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

    public String getCreateTime() {
        return create_time;
    }

    public void setCreateTime(String create_time) {
        this.create_time = create_time;
    }

    public int getInvisible() {
        return invisible;
    }

    public void setInvisible(int invisible) {
        this.invisible = invisible;
    }
}
