package com.hongy.supermarketsystem.bean;

public class GoodsItemProprety {
    private boolean ischecked;
    private String price;
    private int num;

    public GoodsItemProprety(boolean ischecked, String price, int num) {
        this.ischecked = ischecked;
        this.price = price;
        this.num = num;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
