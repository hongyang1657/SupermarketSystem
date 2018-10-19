package com.hongy.supermarketsystem.presenter;

import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.fragment.impl.ICashierFragmentView;
import com.hongy.supermarketsystem.model.BmobQueryModel;
import com.hongy.supermarketsystem.presenter.impl.IBmobQueryPresenter;
import java.util.List;


public class BmobQueryPresenter implements IBmobQueryPresenter,BmobQueryModel.QueryObjectsListener {

    private BmobQueryModel bmobQueryModel;
    private ICashierFragmentView iCashierFragmentView;

    public BmobQueryPresenter(ICashierFragmentView iCashierFragmentView) {
        this.iCashierFragmentView = iCashierFragmentView;
        bmobQueryModel = new BmobQueryModel(this);
    }

    @Override
    public void queryObjects(String key,String value,int limit) {
        bmobQueryModel.queryObjects(key, value, limit);
    }

    @Override
    public void showBmobQueryResult(List<Goods> list, String e) {
        iCashierFragmentView.showBmobQueryResult(list,e);
    }
}
