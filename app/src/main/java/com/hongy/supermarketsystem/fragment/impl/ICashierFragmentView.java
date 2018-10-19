package com.hongy.supermarketsystem.fragment.impl;

import com.hongy.supermarketsystem.bean.Goods;

import java.util.List;


public interface ICashierFragmentView {
    void showBmobQueryResult(List<Goods> list, String e);
}
