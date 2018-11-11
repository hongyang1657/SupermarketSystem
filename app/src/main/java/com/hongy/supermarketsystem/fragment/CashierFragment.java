package com.hongy.supermarketsystem.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.fragment.adapter.GoodsAdapter;
import com.hongy.supermarketsystem.fragment.impl.ICashierFragmentView;
import com.hongy.supermarketsystem.presenter.BmobQueryPresenter;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.view.SearchActivity;
import com.hongy.supermarketsystem.view.dialog.EnterDialog;
import com.hongy.supermarketsystem.zxing.activity.CaptureActivity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

public class CashierFragment extends Fragment implements GoodsAdapter.IitemSelect,ICashierFragmentView{

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private CheckBox cbTotal;
    private Button btTotal;
    private TextView tvTotalPrice,tvSearch,tvClean;
    private GoodsAdapter adapter;
    private List<Goods> goodsList = new ArrayList<>();
    private boolean ischeck;
    private BmobQueryPresenter bmobQueryPresenter;
    private ClickListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = inflater.inflate(R.layout.fragment_cashier, container, false);
        initView(view);
        bmobQueryPresenter = new BmobQueryPresenter(this);
        return view;
    }

    private void initView(View view){
        listener = new ClickListener();
        recyclerView = view.findViewById(R.id.recycler_view_cashier_list);
        tvSearch = view.findViewById(R.id.tv_search_goods);
        tvClean = view.findViewById(R.id.tv_clean);
        floatingActionButton = view.findViewById(R.id.float_action_bt_scan);
        cbTotal = view.findViewById(R.id.cb_total);
        btTotal = view.findViewById(R.id.bt_settle_accounts);
        tvTotalPrice = view.findViewById(R.id.tv_total);
        adapter = new GoodsAdapter(goodsList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        tvSearch.setOnClickListener(listener);
        tvClean.setOnClickListener(listener);
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
                    L.i("滑动到底部");//滑动到底部
                }
                if(!recyclerView.canScrollVertically(-1)){
                    L.i( "滑动到顶部");//滑动到顶部
                }
            }
        });
        cbTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ischeck = isChecked;
            }
        });
        cbTotal.setOnClickListener(listener);
        floatingActionButton.setOnClickListener(listener);
    }

    public class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_search_goods:
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    intent.putExtra("content",tvSearch.getText().toString().trim());
                    startActivityForResult(intent,1);
                    break;
                case R.id.tv_clean:
                    tvSearch.setText("");
                    break;
                case R.id.cb_total:
                    //全选 更新goodsList
                    for (int i=0;i<goodsList.size();i++){
                        goodsList.get(i).setIsChecked(ischeck);
                    }
                    adapter.notifyData(goodsList);
                    break;
                case R.id.float_action_bt_scan:
                    startQrCode();    //开始扫描
                    break;
                case R.id.bt_settle_accounts:

                    break;
            }
        }
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {  //扫描结果回调
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //查询数据库中有没有此商品
            L.i("条码："+scanResult);
            searchGoods(scanResult);
            tvSearch.setText(scanResult);
        }else if (requestCode==1 && resultCode==RESULT_OK){  //搜索结果回调
            String serachContent = data.getExtras().getString("serachContent");
            L.i("serachContent:"+serachContent);
            searchGoods(serachContent);
            tvSearch.setText(serachContent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private List<Goods> scanResultList;
    private boolean isRepetition;       //判断列表中是否已经有该商品
    private void searchGoods(final String scanResult){
        isRepetition = false;
        //判断如果清单中已经有该商品，则不添加新的item，而是在该商品数量上加一
        //scanResultList = DataBaseUtil.queryByBarcode(scanResult);      //本地数据库查询
        //bmob条件查询
        bmobQueryPresenter.queryObjects("barCode",scanResult,1);

    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tvTotalPrice.setText("¥"+msg.obj);
                    cbTotal.setText("  全选("+msg.arg1+")");
                    cbTotal.setChecked(isAllChecked);
                    break;
            }
        }
    };

    private boolean isAllChecked;
    @Override
    public void onGoodsListChanged(List<Goods> goodsList) {
        this.goodsList = goodsList;
        BigDecimal sum = new BigDecimal("0");
        int goodsTotalNum = 0;
        isAllChecked = true;
        for (int i=0;i<goodsList.size();i++){
            L.i("goodsList:"+goodsList.get(i).toString());
            Goods goods = goodsList.get(i);
            if (goods.getIsChecked()){
                BigDecimal price = new BigDecimal(goods.getPrice());
                BigDecimal num = new BigDecimal(goods.getNumble());
                sum = sum.add(price.multiply(num));
                goodsTotalNum = goodsTotalNum+goods.getNumble();
            }else {
                isAllChecked = false;
            }
        }
        Message message = new Message();
        message.what = 1;
        message.obj = sum.toString();
        message.arg1 = goodsTotalNum;
        handler.sendMessage(message);

    }

    private EnterDialog dialog = null;
    @Override
    public void onItemLongClickListener(final int position) {
        L.i("onItemLongClickListener:"+position);
        dialog = new EnterDialog(getActivity(), new EnterDialog.EnterListener() {
            @Override
            public void onEnter() {
                //删除该条item
                goodsList.remove(position);
                adapter.notifyData(goodsList);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onItemClickListener(int position) {

    }

    //bmob查询结果
    @Override
    public void showBmobQueryResult(List<Goods> list, String e) {
        if (e==null){
            scanResultList = list;
        }else {
            L.i("查询失败："+e);
            scanResultList = null;
        }

        if (scanResultList==null){          //没有该商品
            //收银扫描发现没有该商品
            L.i("收银扫描发现没有该商品");
            Toast.makeText(getActivity(), "没有该商品", Toast.LENGTH_SHORT).show();
        }else {
            if (goodsList.size()==0){
                isRepetition = false;
            }
            for (int i=0;i<goodsList.size();i++){
                if (goodsList.get(i).getBarCode().equals(scanResultList.get(0).getBarCode())){          //列表中已经有该商品
                    goodsList.get(i).setNumble(goodsList.get(i).getNumble()+1);
                    L.i("scanResultList:"+scanResultList.get(0).toString());
                    isRepetition = true;
                }
            }
            if (!isRepetition){
                goodsList.add(scanResultList.get(0));
            }
            adapter.notifyData(goodsList);
        }
    }
}
