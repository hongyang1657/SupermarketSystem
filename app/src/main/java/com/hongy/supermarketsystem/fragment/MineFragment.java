package com.hongy.supermarketsystem.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.SharedPreferencesUtils;
import com.hongy.supermarketsystem.view.EnteringActivity;
import com.hongy.supermarketsystem.view.LoginActivity;

public class MineFragment extends Fragment {

    private TextView tvEntering,tvLogout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        tvEntering = view.findViewById(R.id.tv_entering);
        tvLogout = view.findViewById(R.id.tv_logout);
        tvEntering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //录入商品
                Intent intent = new Intent(getActivity(), EnteringActivity.class);
                startActivity(intent);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出登录
                SharedPreferencesUtils.getInstance().setBooleanKeyValue(Constant.IS_NEED_LOGIN,true);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }


}
