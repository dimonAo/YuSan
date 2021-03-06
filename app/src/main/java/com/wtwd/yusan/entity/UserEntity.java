package com.wtwd.yusan.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.CircularProgressDrawable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * time:2018/4/12
 * Created by w77996
 */

@Entity(nameInDb = "USER")
public class UserEntity implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "USER_ID")
    private Long user_id;                 //用户ID

    @Property(nameInDb = "USER_ID_STR")
    private String userIdStr;

    @Property(nameInDb = "HEAD_IMG")
    private String head_img;            //头像

    @Property(nameInDb = "BIRTH")
    private String birth;               //生日

    @Property(nameInDb = "USER_NAME")
    private String user_name;          //用户名

    @Property(nameInDb = "PASSWORD")
    private String password;          //密码

    @Property(nameInDb = "SEX")
    private int sex;                 //性别
    @Property(nameInDb = "HEIGHT")
    private String height;

    @Property(nameInDb = "NICK_NAME")
    private String nick_name;

    @Property(nameInDb = "COUNTRY")
    private String country;

    @Property(nameInDb = "CREATE_TIME")
    private String create_time;

    @Property(nameInDb = "INVISIBLE")
    private int invisible;           //是否隐身

    @Property(nameInDb = "OPEN_ID")
    private String open_id;           //是否隐身

    protected UserEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            user_id = null;
        } else {
            user_id = in.readLong();
        }
        userIdStr = in.readString();
        head_img = in.readString();
        birth = in.readString();
        user_name = in.readString();
        password = in.readString();
        sex = in.readInt();
        height = in.readString();
        nick_name = in.readString();
        country = in.readString();
        create_time = in.readString();
        invisible = in.readInt();
        open_id = in.readString();
    }

    @Generated(hash = 402094971)
    public UserEntity(Long id, Long user_id, String userIdStr, String head_img,
            String birth, String user_name, String password, int sex, String height,
            String nick_name, String country, String create_time, int invisible,
            String open_id) {
        this.id = id;
        this.user_id = user_id;
        this.userIdStr = userIdStr;
        this.head_img = head_img;
        this.birth = birth;
        this.user_name = user_name;
        this.password = password;
        this.sex = sex;
        this.height = height;
        this.nick_name = nick_name;
        this.country = country;
        this.create_time = create_time;
        this.invisible = invisible;
        this.open_id = open_id;
    }

    @Generated(hash = 1433178141)
    public UserEntity() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (user_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(user_id);
        }
        dest.writeString(userIdStr);
        dest.writeString(head_img);
        dest.writeString(birth);
        dest.writeString(user_name);
        dest.writeString(password);
        dest.writeInt(sex);
        dest.writeString(height);
        dest.writeString(nick_name);
        dest.writeString(country);
        dest.writeString(create_time);
        dest.writeInt(invisible);
        dest.writeString(open_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel in) {
            return new UserEntity(in);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", userIdStr='" + userIdStr + '\'' +
                ", head_img='" + head_img + '\'' +
                ", birth='" + birth + '\'' +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", height='" + height + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", country='" + country + '\'' +
                ", create_time='" + create_time + '\'' +
                ", invisible=" + invisible +
                ", open_id='" + open_id + '\'' +
                '}';
    }
}
