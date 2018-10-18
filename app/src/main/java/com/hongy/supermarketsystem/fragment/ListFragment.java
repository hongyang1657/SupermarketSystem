package com.hongy.supermarketsystem.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ListFragment extends Fragment implements GoodsAdapter.IitemSelect{

    private List<Goods> goodsList = new ArrayList<>();
    private GoodsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
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
        refreshData();
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_search_list);
        swipeRefreshLayout = view.findViewById(R.id.list_swipe_layout);
        adapter = new GoodsAdapter(goodsList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 下拉刷新
                refreshData();
            }
        });

    }

    private void refreshData(){
        //goodsList = DataBaseUtil.queryAll();      //本地数据库查询所有商品数据
//        for (int i=0;i<goodsList.size();i++){
//            L.i("goodsList:"+goodsList.get(i).toString());
//        }

        //查询bomb数据库
        BmobQuery<Goods> query = new BmobQuery<>();
        query.setLimit(20);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e==null){
                    goodsList = list;
                    for (int i=0;i<goodsList.size();i++){
                        L.i("查询bmob goodsList:"+goodsList.get(i).toString());
                    }
                    adapter.notifyData(goodsList);
                }else {
                    L.i("查询bmob失败："+e.getMessage());
                }
                swipeRefreshLayout.setRefreshing(false);   //结束下拉刷新的动画
            }
        });



    }

    @Override
    public void onGoodsListChanged(List<Goods> goodsList) {
        for (int i=0;i<goodsList.size();i++){

            L.i("goodsList:"+goodsList.get(i).toString());
        }
    }
}
