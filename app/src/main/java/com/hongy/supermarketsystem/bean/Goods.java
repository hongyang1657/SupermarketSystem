package com.hongy.supermarketsystem.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class Goods {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String name;
    private float price;
    @Unique
    private String barCode;

    @Generated(hash = 814012694)
    public Goods(Long id, String name, float price, String barCode) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.barCode = barCode;
    }

    @Generated(hash = 1770709345)
    public Goods() {
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", barCode=" + barCode +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
