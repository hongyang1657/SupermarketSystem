package com.hongy.supermarketsystem.utils;


import com.hongy.supermarketsystem.MyApplication;
import com.hongy.supermarketsystem.bean.Goods;

import java.util.List;


public class DataBaseUtil {

    public static void insertData(Goods goods){
        MyApplication.getDaoSession().getGoodsDao().insertOrReplace(goods);
        L.i("插入成功");
    }

    public static void deleteData(Goods goods) {
        MyApplication.getDaoSession().getGoodsDao().delete(goods);
        L.i("删除成功");
    }

    public static void updateData(Goods goods) {
        MyApplication.getDaoSession().getGoodsDao().update(goods);
        L.i("修改成功");
    }


    /**
     * 查询全部数据
     */
    public static List<Goods> queryAll() {
        return MyApplication.getDaoSession().getGoodsDao().loadAll();
    }
}
