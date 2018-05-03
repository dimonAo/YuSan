package com.wtwd.yusan.ease.net.response;

/**
 * Created by ChangLe on 2018/4/28.
 */

public class GroupDetailInfo {

    private Long user_id;                 //用户ID

    private Long Is_admin;

    private String userIdStr;

    private String head_img;            //头像

    private String birth;               //生日

    private String user_name;          //用户名

    private String password;          //密码

    private int sex;                 //性别 1是男，2是女

    private String country;

    private String create_time;

    private int invisible;           //是否隐身
    private long group_id;

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public Long getIs_admin() {
        return Is_admin;
    }

    public void setIs_admin(Long is_admin) {
        Is_admin = is_admin;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
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

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getInvisible() {
        return invisible;
    }

    public void setInvisible(int invisible) {
        this.invisible = invisible;
    }


}