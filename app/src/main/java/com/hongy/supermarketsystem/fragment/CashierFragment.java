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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.fragment.adapter.GoodsAdapter;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.zxing.activity.CaptureActivity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;

public class CashierFragment extends Fragment implements GoodsAdapter.IitemSelect{

    private Button btScan;
    private RecyclerView recyclerView;
    private CheckBox cbTotal;
    private Button btTotal;
    private TextView tvTotalPrice;
    private EditText etCodebar;
    private Button btAddBarcode;
    private GoodsAdapter adapter;
    private List<Goods> goodsList = new ArrayList<>();
    private boolean ischeck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashier, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        btScan = view.findViewById(R.id.bt_scan);
        recyclerView = view.findViewById(R.id.recycler_view_cashier_list);
        cbTotal = view.findViewById(R.id.cb_total);
        btTotal = view.findViewById(R.id.bt_settle_accounts);
        tvTotalPrice = view.findViewById(R.id.tv_total);
        etCodebar = view.findViewById(R.id.et_input_barcode);
        btAddBarcode = view.findViewById(R.id.bt_input_barcode);
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
                if(recyclerView.canScrollVertically(1)){
                }else {
                    L.i("滑动到底部");//滑动到底部
                }
                if(recyclerView.canScrollVertically(-1)){
                }else {
                    L.i( "滑动到顶部");//滑动到顶部
                }
            }
        });
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();    //开始扫描
            }
        });
        cbTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ischeck = isChecked;
            }
        });
        cbTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全选 更新goodsList
                for (int i=0;i<goodsList.size();i++){
                    goodsList.get(i).setIsChecked(ischeck);
                }
                adapter.notifyData(goodsList);
            }
        });
        btAddBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //手动输入条形码并添加
                searchGoods(etCodebar.getText().toString().trim());
                etCodebar.setText("");
            }
        });
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
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //查询数据库中有没有此商品
            L.i("条码："+scanResult);
            searchGoods(scanResult);
            etCodebar.setText(scanResult);
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
    private boolean isRepetition;
    private void searchGoods(final String scanResult){
        isRepetition = false;

        //判断如果清单中已经有该商品，则不添加新的item，而是在该商品数量上加一
        //scanResultList = DataBaseUtil.queryByBarcode(scanResult);      //本地数据库查询

        //bmob条件查询
        BmobQuery<Goods> query = new BmobQuery<>();
        query.addWhereEqualTo("barCode",scanResult);
        query.setLimit(1);
        query.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if (e==null){
                    L.i("bmob数据库查询：barCode："+scanResult+"的数据size："+list.size());
                    scanResultList = list;
                }else {
                    L.i("查询失败："+e.getMessage());
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
                            L.i("121211111111111212"+scanResultList.get(0).toString());
                            isRepetition = true;
                        }
                    }
                    if (!isRepetition){
                        goodsList.add(scanResultList.get(0));
                    }
                    adapter.notifyData(goodsList);
                }
            }
        });


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
}
