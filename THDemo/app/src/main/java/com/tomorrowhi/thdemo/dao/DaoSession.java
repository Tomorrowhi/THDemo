package com.tomorrowhi.thdemo.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.tomorrowhi.thdemo.bean.LastMsgInfo;

import com.tomorrowhi.thdemo.dao.LastMsgInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig lastMsgInfoDaoConfig;

    private final LastMsgInfoDao lastMsgInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        lastMsgInfoDaoConfig = daoConfigMap.get(LastMsgInfoDao.class).clone();
        lastMsgInfoDaoConfig.initIdentityScope(type);

        lastMsgInfoDao = new LastMsgInfoDao(lastMsgInfoDaoConfig, this);

        registerDao(LastMsgInfo.class, lastMsgInfoDao);
    }
    
    public void clear() {
        lastMsgInfoDaoConfig.clearIdentityScope();
    }

    public LastMsgInfoDao getLastMsgInfoDao() {
        return lastMsgInfoDao;
    }

}
