package com.hongy.supermarketsystem.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.utils.L;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class GoodsEditorDialog extends Dialog{

    private Context mContext;
    private ImageButton ibDelete,ibClose;
    private TextView tvCancel,tvEnter;
    private EditText etBarCode,etGoodsName,etPrice,etNum;
    private String barcode,goodsName,goodsPrice,objectId;
    private int goodsNum;

    public GoodsEditorDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public void setGoods(String barcode,String goodsName,String goodsPrice,int goodsNum,String objectId){
        this.barcode = barcode;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.goodsNum = goodsNum;
        this.objectId = objectId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //每次刷新data
        etBarCode.setText(barcode);
        etGoodsName.setText(goodsName);
        etPrice.setText(goodsPrice);
        etNum.setText(String.valueOf(goodsNum));
    }

    private void initView(){
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.goods_edit_dialog_layout,null);
        setContentView(view);
        ibDelete = view.findViewById(R.id.ib_delete);
        ibClose = view.findViewById(R.id.ib_close);
        tvCancel = view.findViewById(R.id.tv_cancel);
        tvEnter = view.findViewById(R.id.tv_enter);
        etBarCode = view.findViewById(R.id.et_bar_code);
        etGoodsName = view.findViewById(R.id.et_goods_name);
        etPrice = view.findViewById(R.id.et_goods_price);
        etNum = view.findViewById(R.id.et_goods_num);
        ibDelete.setOnClickListener(new ClickListener());
        ibClose.setOnClickListener(new ClickListener());
        tvCancel.setOnClickListener(new ClickListener());
        tvEnter.setOnClickListener(new ClickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);   //点击外部，不关闭dialog
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    public class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ib_delete:
                    deleteGoods(objectId);
                    break;
                case R.id.ib_close:
                    dismiss();
                    break;
                case R.id.tv_cancel:
                    dismiss();
                    break;
                case R.id.tv_enter:
                    updateGoods(objectId);
                    break;
            }
        }
    }

    //删除商品
    private void deleteGoods(String objectId){
        Goods goods = new Goods();
        goods.setObjectId(objectId);
        goods.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    L.i("bmob删除"+"成功");
                    Toast.makeText(mContext, "删除成功！", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    L.i("bmob删除"+"失败："+e.getMessage()+","+e.getErrorCode());
                    Toast.makeText(mContext, "操作失败，请检查网络后重试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //修改商品
    private void updateGoods(String objectId){
        Goods goods = new Goods();
        goods.setNumble(1);       //暂时写死为1，不可改变商品库存
        goods.setBarCode(etBarCode.getText().toString().trim());
        goods.setName(etGoodsName.getText().toString().trim());
        goods.setPrice(etPrice.getText().toString().trim());
        goods.setIsChecked(true);
        goods.update(objectId,new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    L.i("bmob修改"+"成功");
                    Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    L.i("bmob修改"+"失败："+e.getMessage()+","+e.getErrorCode());
                    Toast.makeText(mContext, "操作失败，请检查网络后重试！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
