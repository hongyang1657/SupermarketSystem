package com.hongy.supermarketsystem.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

import cn.bmob.v3.BmobObject;

@Entity
public class Goods extends BmobObject{

    private String name;
    private String price;
    @Unique   //该属性值必须在数据库中是唯一值
    private String barCode;
    //@Transient    //使用该注释的属性不会被存入数据库的字段中
    private int imgResId;
    private int numble;
    private boolean isChecked;
    @Generated(hash = 1172340983)
    public Goods(String name, String price, String barCode, int imgResId,
            int numble, boolean isChecked) {
        this.name = name;
        this.price = price;
        this.barCode = barCode;
        this.imgResId = imgResId;
        this.numble = numble;
        this.isChecked = isChecked;
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
    public int getImgResId() {
        return this.imgResId;
    }
    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }
    public int getNumble() {
        return this.numble;
    }
    public void setNumble(int numble) {
        this.numble = numble;
    }
    public boolean getIsChecked() {
        return this.isChecked;
    }
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", barCode='" + barCode + '\'' +
                ", numble=" + numble +
                ", isChecked=" + isChecked +
                '}';
    }
}
