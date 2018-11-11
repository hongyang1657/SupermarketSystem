package com.hongy.supermarketsystem;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.hongy.supermarketsystem.bean.DaoMaster;
import com.hongy.supermarketsystem.bean.DaoSession;

import cn.bmob.v3.Bmob;


public class MyApplication extends Application {

    private static DaoSession daoSession;
    private static MyApplication instance;
    public static MyApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //setupDatabase();
        Bmob.initialize(this,"4b672501c10d79c6b6598238db13bf54");  //初始化bmob
        instance = this;
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
