package com.xw.greendaodemo;

import com.xw.greendaodemo.dao.DaoMaster;
import com.xw.greendaodemo.dao.DaoSession;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class GreenDaoManager {

    private static GreenDaoManager mInstance;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private DaoMaster.DevOpenHelper devOpenHelper;


    public GreenDaoManager() {
        //创建一个数据库   数据库名称
        devOpenHelper = new DaoMaster.DevOpenHelper(App.getContext(), "user-db", null);
        DaoMaster mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            mInstance = new GreenDaoManager();
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    /**
     * 关闭数据连接
     */
    public void close() {
        if (devOpenHelper != null) {
            devOpenHelper.close();
        }
    }

}
