package com.wtwd.yusan.ease.net.response;

import java.io.Serializable;

/**
 * Created by XJM on 2018/4/23.
 */

public class ContactListInfo implements Serializable{
    private long user_id;
    private String user_name;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    private String head_img;
    private int sex;
    private String sort_letters;


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSortLetters() {
        return sort_letters;
    }

    public void setSortLetters(String sort_letters) {
        this.sort_letters = sort_letters;
    }
}
