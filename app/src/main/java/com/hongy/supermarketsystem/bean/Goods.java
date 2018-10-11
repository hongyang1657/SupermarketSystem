package com.hongy.supermarketsystem.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Goods {

    @Unique    //该属性值必须在数据库中是唯一值
    private String name;
    private String price;
    @Unique
    private String barCode;
    @Generated(hash = 997633103)
    public Goods(String name, String price, String barCode) {
        this.name = name;
        this.price = price;
        this.barCode = barCode;
    }
    @Generated(hash = 1770709345)
    public Goods() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getBarCode() {
        return this.barCode;
    }
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", barCode='" + barCode + '\'' +
                '}';
    }
}
