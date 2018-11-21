package com.hongy.supermarketsystem.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.utils.L;

/**
 * 结算页
 */
public class SettlementActivity extends Activity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        String totalPrice = getIntent().getStringExtra("totalPrice");
        String goodsLists = getIntent().getStringExtra("goodsList");
        L.i("总价："+totalPrice);
        L.i("goodsLists："+goodsLists);
    }
}
