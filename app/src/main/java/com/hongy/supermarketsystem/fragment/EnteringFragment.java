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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.DataBaseUtil;
import com.hongy.supermarketsystem.zxing.activity.CaptureActivity;

import static android.app.Activity.RESULT_OK;

public class EnteringFragment extends Fragment {

    private Button btScan,btAddGoods;
    private EditText etBarCode,etGoodsName,etGoodsPrice;

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
        if ("".equals(goodsName)||"".equals(goodsPrice)||"".equals(goodsBarcode)){
            Toast.makeText(getActivity(), "商品信息不能为空", Toast.LENGTH_SHORT).show();
        }else {
            DataBaseUtil.insertData(new Goods(goodsName,goodsPrice,goodsBarcode));
            etGoodsName.setText("");
            etGoodsPrice.setText("");
            etBarCode.setText("");
            Toast.makeText(getActivity(), "添加商品成功", Toast.LENGTH_SHORT).show();
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
