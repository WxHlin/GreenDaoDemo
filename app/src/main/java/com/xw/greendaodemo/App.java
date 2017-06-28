package com.xw.greendaodemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/6/28 0028.
 */

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        GreenDaoManager.getInstance();
    }

    public static Context getContext() {
        return mContext;
    }

}
