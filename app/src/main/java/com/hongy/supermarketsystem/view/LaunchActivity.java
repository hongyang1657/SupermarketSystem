package com.hongy.supermarketsystem.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.utils.SharedPreferencesUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.hongy.supermarketsystem.utils.Constant.IS_NEED_LOGIN;

public class LaunchActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreferencesUtils.getInstance().getBooleanValueByKey(IS_NEED_LOGIN,true)){       //需要跳转登录页
                    Intent intent = new Intent(LaunchActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(LaunchActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },2000);

    }
}
