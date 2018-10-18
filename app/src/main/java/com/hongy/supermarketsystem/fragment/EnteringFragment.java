package com.hongy.supermarketsystem.fragment;


import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.api.ApiManager;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.bean.GoodsInfoFromInternet;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.DataBaseUtil;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.zxing.activity.CaptureActivity;

import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class EnteringFragment extends Fragment {

    private String appcode = "APPCODE 5353076bdf71427c8c8aa5ab97eec66c";
    private Button btScan,btAddGoods;
    private EditText etBarCode,etGoodsName,etGoodsPrice,etGoodsNum;
    private Switch aSwitch;
    private boolean isUseOnlineGoodsSearch = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entering, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        btScan = view.findViewById(R.id.bt_scan);
        btAddGoods = view.findViewById(R.id.bt_add_goods);
        etBarCode = view.findViewById(R.id.et_bar_code);
        etGoodsName = view.findViewById(R.id.et_goods_name);
        etGoodsPrice = view.findViewById(R.id.et_goods_price);
        etGoodsNum = view.findViewById(R.id.et_goods_num);
        aSwitch = view.findViewById(R.id.sw);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });
        btAddGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGoodsToDB();
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(getActivity(), "使用在线数据", Toast.LENGTH_SHORT).show();
                    isUseOnlineGoodsSearch = true;
                }else {
                    Toast.makeText(getActivity(), "关闭在线数据", Toast.LENGTH_SHORT).show();
                    isUseOnlineGoodsSearch  = false;
                }
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
    
    //添加商品到数据库
    private void addGoodsToDB(){
        String goodsName = etGoodsName.getText().toString().trim();
        String goodsPrice = etGoodsPrice.getText().toString().trim();
        String goodsBarcode = etBarCode.getText().toString().trim();
        int goodsNum = Integer.valueOf(etGoodsNum.getText().toString().trim());
        if ("".equals(goodsName)||"".equals(goodsPrice)||"".equals(goodsBarcode)){
            Toast.makeText(getActivity(), "商品信息不能为空", Toast.LENGTH_SHORT).show();
        }else {
            Goods goods = new Goods(goodsName,goodsPrice,goodsBarcode,Constant.goodsIconList[new Random().nextInt(22)],1,true);
            DataBaseUtil.insertData(goods);
            etGoodsName.setText("");
            etGoodsPrice.setText("");
            etBarCode.setText("");
            Toast.makeText(getActivity(), "添加商品成功", Toast.LENGTH_SHORT).show();

            goods.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){
                        L.i("添加bmob数据成功：objectId："+s);
                    }else {
                        L.i("创建bmob数据失败："+e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            etBarCode.setText(scanResult);
            //如果开启在线商品信息查询
            if (isUseOnlineGoodsSearch){
                ApiManager.serviceBarcode
                        .getGoodsDetails(appcode,scanResult)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Subscriber<GoodsInfoFromInternet>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                L.i("e:"+e.toString());
                            }

                            @Override
                            public void onNext(GoodsInfoFromInternet goodsInfoFromInternet) {
                                L.i("goodsInfoFromInternet:"+new Gson().toJson(goodsInfoFromInternet));
                                etGoodsName.setText(goodsInfoFromInternet.getShowapi_res_body().getGoodsName());
                                etGoodsPrice.setText(goodsInfoFromInternet.getShowapi_res_body().getPrice());
                            }
                        });
            }
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
}
