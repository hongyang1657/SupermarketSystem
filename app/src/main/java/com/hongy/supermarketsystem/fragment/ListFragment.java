package com.hongy.supermarketsystem.fragment;


import android.app.Fragment;
import android.content.DialogInterface;
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
import com.hongy.supermarketsystem.utils.DataBaseUtil;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.view.SearchActivity;
import com.hongy.supermarketsystem.view.dialog.GoodsEditorDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

public class ListFragment extends Fragment implements GoodsAdapter.IitemSelect{

    private List<Goods> goodsList = new ArrayList<>();
    private GoodsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tvSearch,tvClean,tvHint;
    private GoodsEditorDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        initView(view);
        conditionQueryData(tvSearch.getText().toString().trim());
        dialog = new GoodsEditorDialog(getActivity());
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //详情页dialog退出，此时刷新list
                conditionQueryData(tvSearch.getText().toString().trim());
            }
        });

        L.i("数据库:"+ DataBaseUtil.queryAll());
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
        tvClean = view.findViewById(R.id.tv_clean);
        tvHint = view.findViewById(R.id.tv_hint);
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
                    if (goodsList.size()==0){
                        tvHint.setVisibility(View.VISIBLE);
                    }else {
                        tvHint.setVisibility(View.GONE);
                    }
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
                intent.putExtra("tips","目前只能通过价格查找商品");
                startActivityForResult(intent,1);
            }
        });
        tvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSearch.setText("");
                swipeRefreshLayout.setRefreshing(true);  //开始刷新动画
                conditionQueryData(tvSearch.getText().toString().trim());
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

    //Item长按事件
    @Override
    public void onItemLongClickListener(int position) {
        L.i("onItemLongClickListener:"+position);
    }

    //item点击事件
    @Override
    public void onItemClickListener(int position) {
        //弹出商品详情dialog
        L.i("position:"+position);
        dialog.setGoods(goodsList.get(position).getBarCode(),goodsList.get(position).getName()
                ,goodsList.get(position).getPrice(),goodsList.get(position).getNumble(),goodsList.get(position).getObjectId());
        dialog.show();
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
