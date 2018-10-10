package com.hongy.supermarketsystem.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    //数据库文件名
    private static final String DB_NAME = "my_database.db";
    //数据库表明
    public static final String TABLE_NAME = "commodity";
    //数据库版本号
    public static final int DB_VERSION = 1;

    public static final String GOODS_NAME = "goods_name";
    public static final String GOODS_BAR_CODE = "goods_bar_code";
    public static final String PRICE = "price";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //建表
        String sql = "create table " + TABLE_NAME + " (_id integer primary key autoincrement, " +
                GOODS_NAME + " varchar(30), " + PRICE + " float," + GOODS_BAR_CODE +" varchar(30)"+ ")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
