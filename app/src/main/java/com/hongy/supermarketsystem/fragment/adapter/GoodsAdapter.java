package com.hongy.supermarketsystem.fragment.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.L;
import com.squareup.picasso.Picasso;
import java.util.List;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MyViewHolder>{

    private List<Goods> goodsList;
    private Context mContext;
    private IitemSelect iitemSelect;
    private int num;
    private String barcode,goodsName,goodsPrice;
    private boolean isCheckedd;

    public GoodsAdapter(List<Goods> goodsList,IitemSelect iitemSelect) {
        this.goodsList = goodsList;
        this.iitemSelect = iitemSelect;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.goods_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        //L.i("刷新了onBindViewHolder："+i);
        num = goodsList.get(i).getNumble();
        barcode = goodsList.get(i).getBarCode();
        goodsName = goodsList.get(i).getName();
        goodsPrice = goodsList.get(i).getPrice();
        Picasso.get().load(Constant.goodsIconList[goodsList.get(i).getImgResId()]).into(myViewHolder.imageView);
        myViewHolder.tvName.setText(goodsName);
        myViewHolder.tvPrice.setText(String.format(mContext.getResources().getString(R.string.goodPrice),goodsPrice));
        myViewHolder.tvBarcode.setText(barcode);
        myViewHolder.tvGoodsNum.setText(String.valueOf(num));        //item上数量只显示为1
        myViewHolder.cbSelectGoods.setChecked(goodsList.get(i).getIsChecked());
        myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {        //item长按事件
            @Override
            public boolean onLongClick(View view) {
                iitemSelect.onItemLongClickListener(i);
                return false;
            }
        });
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {          //item点击事件
            @Override
            public void onClick(View v) {
                iitemSelect.onItemClickListener(i);
            }
        });
        myViewHolder.ibReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = setNumPlus(myViewHolder.tvGoodsNum,0);  //减一
                goodsList.get(i).setNumble(num);
                iitemSelect.onGoodsListChanged(goodsList);
            }
        });
        myViewHolder.ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = setNumPlus(myViewHolder.tvGoodsNum,1);  //加一
                goodsList.get(i).setNumble(num);
                iitemSelect.onGoodsListChanged(goodsList);
            }
        });
        myViewHolder.cbSelectGoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {    //选择item按钮
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedd = isChecked;
            }
        });
        myViewHolder.cbSelectGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsList.get(i).setIsChecked(isCheckedd);
                iitemSelect.onGoodsListChanged(goodsList);
            }
        });
        iitemSelect.onGoodsListChanged(goodsList);
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }


    //商品数量加一或减一
    private int setNumPlus(TextView tvGoodsNum,int flag){
        int num = Integer.valueOf(tvGoodsNum.getText().toString());
        if (flag==0){
            if (num>1){
                num = num - 1;
            }else {
                num = 1;
            }
        }else if (flag==1){
            if (num<99){
                num = num + 1;
            }
        }
        tvGoodsNum.setText(String.valueOf(num));
        return num;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private CheckBox cbSelectGoods;
        private ImageView imageView;
        private TextView tvName;
        private TextView tvPrice;
        private TextView tvBarcode;
        private LinearLayout llGoodsNum;
        private ImageButton ibReduce,ibPlus;
        private TextView tvGoodsNum;

        MyViewHolder(View itemView) {
            super(itemView);
            cbSelectGoods = itemView.findViewById(R.id.cb_select_goods);
            imageView = itemView.findViewById(R.id.iv_goods);
            tvName = itemView.findViewById(R.id.tv_goods_name);
            tvPrice = itemView.findViewById(R.id.tv_goods_price);
            tvBarcode = itemView.findViewById(R.id.tv_goods_barcode);
            llGoodsNum = itemView.findViewById(R.id.ll_seting_goods_num);
            ibReduce = itemView.findViewById(R.id.ib_reduce);
            ibPlus = itemView.findViewById(R.id.ib_plus);
            tvGoodsNum = itemView.findViewById(R.id.tv_goods_num);
        }
    }

    public void notifyData(List<Goods> goodsList){
        this.goodsList = goodsList;
        notifyDataSetChanged();
        iitemSelect.onGoodsListChanged(goodsList);
    }

    public interface IitemSelect{
        void onGoodsListChanged(List<Goods> goodsList);   //商品数据改变的回调
        void onItemLongClickListener(int position);
        void onItemClickListener(int position);
    }
}
