package com.hongy.supermarketsystem.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.fragment.adapter.GoodsAdapter;
import com.hongy.supermarketsystem.utils.DataBaseUtil;
import com.hongy.supermarketsystem.utils.L;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private List<Goods> goodsList = new ArrayList<>();
    private GoodsAdapter adapter;
    private RecyclerView recyclerView;

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
        recyclerView = view.findViewById(R.id.recycler_view_search_list);
        adapter = new GoodsAdapter(goodsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void refreshData(){
        goodsList = DataBaseUtil.queryAll();
        for (int i=0;i<goodsList.size();i++){
            L.i("goodsList:"+goodsList.get(i).toString());
        }
        adapter.notifyData(goodsList);
    }
}
