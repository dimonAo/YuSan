package com.wtwd.yusan.ease.util;

import android.text.TextUtils;


import com.wtwd.yusan.ease.net.response.GroupInfo;
import com.wtwd.yusan.ease.net.response.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChangLe on 2018/4/25.
 */

public class JsonUtil {

    public static JsonUtil JSONUTIL = new JsonUtil();//单例

    public static JsonUtil getJsonUtil() {
        return JSONUTIL;
    }


    public boolean isRequestSeccess(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (TextUtils.isEmpty(status)) return false;
            if (status.equals("1")) {
                return true;
            } else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getStatusStr(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (TextUtils.isEmpty(status)) return null;
            String str = jsonObject.getString("msg");
            return str;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GroupInfo> JsonGroup(String response) {
        List<GroupInfo> grouplist_new = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("object");
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(j);
                String groupName = jsonObject2.getString("groupName");
                String groupId = jsonObject2.getString("groupId");
                String im_groupId = jsonObject2.getString("im_group_id");
                String isAdmin = jsonObject2.getString("isAdmin");
                String user = jsonObject2.getString("user");
                GroupInfo ginfo = new GroupInfo();
                ginfo.setGroup_id(Long.parseLong(groupId));
                ginfo.setIm_groupId(im_groupId);
                ginfo.setGroup_admin(Integer.parseInt(isAdmin));
                ginfo.setGroupIdStr(groupName);
                List<User> userList = JsonUser(user);
                ginfo.setMembers(userList);
                grouplist_new.add(ginfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return grouplist_new;
    }

    public List<User> JsonUser(String response) {
        List<User> Userlist_new = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                String country = jsonObject2.getString("country");
                String password = jsonObject2.getString("password");
                String create_time = jsonObject2.getString("create_time");
                String user_id = jsonObject2.getString("user_id");
                String user_name = jsonObject2.getString("user_name");
                String head_img = jsonObject2.getString("head_img");
                String birth = jsonObject2.getString("birth");
                String sex = jsonObject2.getString("sex");
                String invisible = jsonObject2.getString("invisible");
                User user = new User();
                user.setBirth(birth);
                user.setCountry(country);
                user.setCreateTime(create_time);
                user.setPassword(password);
                user.setUser_name(user_name);
                user.setUserid(Long.parseLong(user_id));
                user.setSex(Integer.parseInt(sex));
                user.setInvisible(Integer.parseInt(invisible));
                user.setHeadImg(head_img);
                Userlist_new.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Userlist_new;
    }
}