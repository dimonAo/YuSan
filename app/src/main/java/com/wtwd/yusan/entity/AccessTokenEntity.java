package com.wtwd.yusan.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class AccessTokenEntity implements Parcelable {

    /**
     * 接口调用凭证
     */
    private String access_token;

    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    private String expires_in;

    /**
     * 用户刷新access_token
     */
    private String refresh_token;
    /**
     * 授权用户唯一标识
     */
    private String openid;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;
    /**
     * 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
     */
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public static Creator<AccessTokenEntity> getCREATOR() {
        return CREATOR;
    }

    protected AccessTokenEntity(Parcel in) {
        access_token = in.readString();
        expires_in = in.readString();
        refresh_token = in.readString();
        openid = in.readString();
        scope = in.readString();
        unionid = in.readString();
    }

    public static final Creator<AccessTokenEntity> CREATOR = new Creator<AccessTokenEntity>() {
        @Override
        public AccessTokenEntity createFromParcel(Parcel in) {
            return new AccessTokenEntity(in);
        }

        @Override
        public AccessTokenEntity[] newArray(int size) {
            return new AccessTokenEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(access_token);
        dest.writeString(expires_in);
        dest.writeString(refresh_token);
        dest.writeString(openid);
        dest.writeString(scope);
        dest.writeString(unionid);
    }

    @Override
    public String toString() {
        return "AccessTokenEntity{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
