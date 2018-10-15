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
import com.hongy.supermarketsystem.bean.GoodsItemProprety;
import com.hongy.supermarketsystem.fragment.adapter.GoodsAdapter;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.DataBaseUtil;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQrCode();    //开始扫描
            }
        });
        cbTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //全选 更新goodsList
                for (int i=0;i<goodsList.size();i++){
                    goodsList.get(i).setIsChecked(isChecked);
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

    private void searchGoods(String scanResult){
        boolean isRepetition = false;
        //判断如果清单中已经有该商品，则不添加新的item，而是在该商品数量上加一
        List<Goods> scanResultList = DataBaseUtil.queryByBarcode(scanResult);

        if (scanResultList.size()==0){          //没有该商品
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

    private Map<Integer,GoodsItemProprety> itemPropretyMap = new HashMap<>();
    /**
     * 购物车里商品信息改变
     * @param position  item位置
     * @param isChecked   是否选中
     * @param price     价格
     * @param num   数量
     */
    @Override
    public void onItemChecked(int position,boolean isChecked,String price,int num) {
        L.i("position:"+position+" isChecked:"+isChecked+" price:"+price+"  num:"+num);
        itemPropretyMap.put(position,new GoodsItemProprety(isChecked,price,num));

        for (int i=0;i<itemPropretyMap.size();i++){

            if (itemPropretyMap.get(i).isIschecked()){      //该item被选中（表示价格需要计算）

            }
        }
    }
}
