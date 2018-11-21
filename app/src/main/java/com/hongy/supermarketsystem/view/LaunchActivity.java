package com.hongy.supermarketsystem.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.utils.SharedPreferencesUtils;

import java.util.Random;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.hongy.supermarketsystem.utils.Constant.IS_NEED_LOGIN;

public class LaunchActivity extends Activity{

    private ImageView ivLaunchImg;
    private int[] imgList = {R.mipmap.launch_1,R.mipmap.launch_2,R.mipmap.launch_3,R.mipmap.launch_4,R.mipmap.launch_5};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN); //去掉Activity上面的状态栏
        setContentView(R.layout.activity_launch);
        ivLaunchImg = findViewById(R.id.iv_launch_img);
        ivLaunchImg.setImageResource(imgList[new Random().nextInt(5)]);
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
