package com.hongy.supermarketsystem.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.api.ApiManager;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.bean.GoodsInfoFromInternet;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.zxing.activity.CaptureActivity;

import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class EnteringActivity extends Activity{

    private EditText etBarCode,etGoodsName,etGoodsPrice,etGoodsNum;
    private boolean isUseOnlineGoodsSearch = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entering);
        initView();
    }


    private void initView(){
        ImageView ivScan = findViewById(R.id.bt_scan);
        Button btAddGoods = findViewById(R.id.bt_add_goods);
        etBarCode = findViewById(R.id.et_bar_code);
        etGoodsName = findViewById(R.id.et_goods_name);
        etGoodsPrice = findViewById(R.id.et_goods_price);
        etGoodsNum = findViewById(R.id.et_goods_num);
        Switch aSwitch = findViewById(R.id.sw);
        ivScan.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(EnteringActivity.this, "使用在线数据", Toast.LENGTH_SHORT).show();
                    isUseOnlineGoodsSearch = true;
                }else {
                    Toast.makeText(EnteringActivity.this, "关闭在线数据", Toast.LENGTH_SHORT).show();
                    isUseOnlineGoodsSearch  = false;
                }
            }
        });
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    //添加商品到数据库
    private void addGoodsToDB(){
        String goodsName = etGoodsName.getText().toString().trim();
        String goodsPrice = etGoodsPrice.getText().toString().trim();
        String goodsBarcode = etBarCode.getText().toString().trim();
        int goodsNum = Integer.valueOf(etGoodsNum.getText().toString().trim());
        if ("".equals(goodsName)||"".equals(goodsPrice)||"".equals(goodsBarcode)){
            Toast.makeText(this, "商品信息不能为空", Toast.LENGTH_SHORT).show();
        }else {
            Goods goods = new Goods(goodsName,goodsPrice,goodsBarcode,new Random().nextInt(21),1,true);     //这里商品数量暂时写死为1，不可变
            //DataBaseUtil.insertData(goods);

            goods.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){
                        L.i("添加bmob数据成功：objectId："+s);
                        Toast.makeText(EnteringActivity.this, "添加商品成功！", Toast.LENGTH_SHORT).show();
                        etGoodsName.setText("");
                        etGoodsPrice.setText("");
                        etBarCode.setText("");
                    }else {
                        L.i("创建bmob数据失败："+e.getMessage());
                        Toast.makeText(EnteringActivity.this, "添加商品失败！", Toast.LENGTH_SHORT).show();
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
                        .getGoodsDetails(Constant.appcode,scanResult)
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
                    Toast.makeText(this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
