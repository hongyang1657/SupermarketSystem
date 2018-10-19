package com.hongy.supermarketsystem.model;

import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.model.impl.IBmobQueryModel;
import com.hongy.supermarketsystem.presenter.BmobQueryPresenter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BmobQueryModel implements IBmobQueryModel{

    private BmobQueryPresenter bmobQueryPresenter;

    public BmobQueryModel(BmobQueryPresenter bmobQueryPresenter) {
        this.bmobQueryPresenter = bmobQueryPresenter;
    }

    @Override
    public void queryObjects(String key,String value,int limit) {
        //bmob条件查询
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo(key,value);
        query.setLimit(limit);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e==null){
                    bmobQueryPresenter.showBmobQueryResult(list,null);
                }else {
                    bmobQueryPresenter.showBmobQueryResult(list,e.getMessage());
                }
            }
        });
    }

    public interface QueryObjectsListener{
        void showBmobQueryResult(List<Goods> list, String e);
    }
}
