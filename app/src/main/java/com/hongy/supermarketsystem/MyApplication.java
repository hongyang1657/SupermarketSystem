package com.hongy.supermarketsystem;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.hongy.supermarketsystem.bean.DaoMaster;
import com.hongy.supermarketsystem.bean.DaoSession;


public class MyApplication extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase(){
        //创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"goods.db");
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        return daoSession;
    }
}
