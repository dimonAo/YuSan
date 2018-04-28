package com.wtwd.yusan.entity.manager;

import android.content.Context;

import com.wtwd.yusan.entity.UserEntity;
import com.wtwd.yusan.entity.greendao.UserEntityDao;
import com.wtwd.yusan.entity.operation.BaseDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public class UserManager extends BaseDao<UserEntity> {
    public UserManager(Context context) {
        super(context);
    }


    public UserEntity queryUserForUserId(Long userId) {
        QueryBuilder userQuery = daoSession.getUserEntityDao().queryBuilder();
        userQuery.where(UserEntityDao.Properties.User_id.eq(userId));
        List<UserEntity> mList = userQuery.list();
        if (mList.size() > 0) {
            return mList.get(0);
        }
        return null;
    }


}
