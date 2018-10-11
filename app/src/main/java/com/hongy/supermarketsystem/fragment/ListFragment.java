package com.hongy.supermarketsystem.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.utils.DataBaseUtil;
import com.hongy.supermarketsystem.utils.L;

import java.util.List;

public class ListFragment extends Fragment {

    private List<Goods> goodsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i("onResume");
        refreshData();
    }

    private void initView(View view){

    }

    private void refreshData(){
        goodsList = DataBaseUtil.queryAll();
        for (int i=0;i<goodsList.size();i++){
            L.i("goodsList:"+goodsList.get(i).toString());
        }
    }
}
