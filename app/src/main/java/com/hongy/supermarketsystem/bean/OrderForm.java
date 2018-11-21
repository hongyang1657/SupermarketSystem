package com.hongy.supermarketsystem.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class OrderForm extends BmobObject{
    private String totalPrices;
    private List<Goods> goodsList;

    public String getTotalPrices() {
        return totalPrices;
    }

    public void setTotalPrices(String totalPrices) {
        this.totalPrices = totalPrices;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
