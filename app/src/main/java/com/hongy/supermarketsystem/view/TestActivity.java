package com.hongy.supermarketsystem.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.api.ApiManager;
import com.hongy.supermarketsystem.bean.GoodsInfoFromInternet;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.zxing.activity.CaptureActivity;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class TestActivity extends Activity{

    Button btnQrCode; // 扫码
    TextView tvResult; // 结果
    private String appcode = "APPCODE 5353076bdf71427c8c8aa5ab97eec66c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnQrCode = (Button) findViewById(R.id.btn_qrcode);
        btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();
            }
        });
        tvResult = (TextView) findViewById(R.id.txt_result);
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(TestActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(TestActivity.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            tvResult.setText(scanResult);
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
                        }
                    });
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
                    Toast.makeText(TestActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
