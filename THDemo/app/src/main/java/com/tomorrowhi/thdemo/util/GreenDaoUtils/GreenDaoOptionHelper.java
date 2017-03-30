package com.tomorrowhi.thdemo.util.GreenDaoUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.blankj.utilcode.util.LogUtils;
import com.tomorrowhi.thdemo.dao.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by zhaotaotao on 2016/11/12.
 * 自定义数据库对象，主要服务于数据库升级
 */

public class GreenDaoOptionHelper extends DaoMaster.DevOpenHelper {


    public GreenDaoOptionHelper(Context context, String name) {
        super(context, name);
    }

    public GreenDaoOptionHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
        if (oldVersion == newVersion) {
            LogUtils.d("数据库已是最新版本:" + oldVersion);
            return;
        }
        LogUtils.d("数据库需要从" + oldVersion + "升级至最新版本:" + newVersion);
        switch (oldVersion) {
            case 1:
                //最旧版本
                String sql = "";
                db.execSQL(sql);
            case 2:
                sql = "";
                db.execSQL(sql);
            case 3:
                //最新版本
                sql = "";
                db.execSQL(sql);
                break;
        }
    }
}
