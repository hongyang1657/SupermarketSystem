package com.hongy.supermarketsystem.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.fragment.adapter.GoodsAdapter;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.view.SearchActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment implements GoodsAdapter.IitemSelect{

    private List<Goods> goodsList = new ArrayList<>();
    private GoodsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tvSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        conditionQueryData(tvSearch.getText().toString().trim());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler_view_search_list);
        swipeRefreshLayout = view.findViewById(R.id.list_swipe_layout);
        tvSearch = view.findViewById(R.id.tv_search_goods);
        adapter = new GoodsAdapter(goodsList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //L.i("onScrolled:  dx:"+dx+"   dy:"+dy);
                if(!recyclerView.canScrollVertically(1)){
                    L.i("滑动到底部"+goodsList.size());//滑动到底部
                    //TODO 加载新数据
                    loadMoreData(goodsList.size(),tvSearch.getText().toString().trim());
                }
                if(!recyclerView.canScrollVertically(-1)){
                    L.i( "滑动到顶部");//滑动到顶部
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO 下拉刷新
                conditionQueryData(tvSearch.getText().toString().trim());
            }
        });
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转搜索页面
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("content",tvSearch.getText().toString().trim());
                startActivityForResult(intent,1);
            }
        });
    }


    //加载更多数据
    private void loadMoreData(int num,String content){
        BmobQuery<Goods> query = new BmobQuery<>();
        if (!content.equals("")){
            if (judgeQueryType(content)){
                query.addWhereEqualTo("price",content);
            }else {
                query.addWhereContains("name",content);
            }
        }
        query.setSkip(num);
        query.setLimit(20);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e==null){
                    goodsList.addAll(list);
                    for (int i=0;i<goodsList.size();i++){
                        //L.i("查询bmob goodsList:"+goodsList.get(i).toString());
                    }
                    adapter.notifyData(goodsList);
                }else {
                    L.i("查询bmob失败："+e.getMessage());
                }
            }
        });
    }

    //根据条件查询数据
    private void conditionQueryData(String content){
        BmobQuery<Goods> query = new BmobQuery<>();
        // 判断content为中文还是纯数字：1.为数字的情况下查询价格  2.为文字的情况下查询商品名称
        if (!content.equals("")){
            if (judgeQueryType(content)){
                query.addWhereEqualTo("price",content);
            }else {
                query.addWhereContains("name",content);
            }
        }
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

    //判断用户输入的想查询的内容是什么类型
    private boolean judgeQueryType(String content){
        for (int i = content.length();--i>=0;){
            if (!Character.isDigit(content.charAt(i))&&'.'!=content.charAt(i)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onGoodsListChanged(List<Goods> goodsList) {
        for (int i=0;i<goodsList.size();i++){
            //L.i("goodsList:"+goodsList.get(i).toString());
        }
    }

    @Override
    public void onItemLongClickListener(int position) {
        L.i("onItemLongClickListener:"+position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    String serachContent = data.getExtras().getString("serachContent");
                    L.i("serachContent:"+serachContent);
                    tvSearch.setText(serachContent);
                    swipeRefreshLayout.setRefreshing(true);  //开始刷新动画
                    conditionQueryData(serachContent);
                }
                break;
        }
    }
}
