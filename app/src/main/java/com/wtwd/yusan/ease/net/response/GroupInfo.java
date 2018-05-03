package com.wtwd.yusan.ease.net.response;

import java.util.List;

/**
 * Created by XJM on 2018/4/25.
 */

public class GroupInfo {

    private Long group_id;                    //群组ID
    private String im_groupId;                    //环信群组ID

    private String groupIdStr;

    private int group_admin;           //群主

    private List<User> members;      //群成员

    private String create_time;     //创建时间

    public String getIm_groupId() {
        return im_groupId;
    }

    public void setIm_groupId(String im_groupId) {
        this.im_groupId = im_groupId;
    }

    public Long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Long group_id) {
        this.group_id = group_id;
    }

    public String getGroupIdStr() {
        return groupIdStr;
    }

    public void setGroupIdStr(String groupIdStr) {
        this.groupIdStr = groupIdStr;
    }

    public int getGroup_admin() {
        return group_admin;
    }

    public void setGroup_admin(int group_admin) {
        this.group_admin = group_admin;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}