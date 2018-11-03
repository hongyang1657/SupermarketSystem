package com.hongy.supermarketsystem.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.bean.Goods;
import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.utils.SharedPreferencesUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends Activity{

    private EditText etUsername,etPassword;
    private Button btLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginRequest(etUsername.getText().toString().trim(),etPassword.getText().toString().trim());
            }
        });
    }

    private void loginRequest(String username,String password){
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(username);
        bu2.setPassword(password);
        bu2.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    //Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功
                    SharedPreferencesUtils.getInstance().setBooleanKeyValue(Constant.IS_NEED_LOGIN,false);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
