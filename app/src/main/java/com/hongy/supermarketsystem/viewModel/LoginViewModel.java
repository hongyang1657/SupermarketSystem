package com.hongy.supermarketsystem.viewModel;
import android.app.Application;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.hongy.supermarketsystem.utils.Constant;
import com.hongy.supermarketsystem.utils.L;
import com.hongy.supermarketsystem.utils.SharedPreferencesUtils;
import com.hongy.supermarketsystem.view.MainActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class LoginViewModel extends BaseViewModel {

    public ObservableField<String> username = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");


    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            L.i(""+username.get().trim()+" "+password.get());
            loginRequest(username.get().trim(),password.get().trim());
        }
    });

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    private void loginRequest(String username,String password){
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(username);
        bu2.setPassword(password);
        bu2.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                L.i("e:"+e.toString());
                //if(e==null){
                    //Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功
                    SharedPreferencesUtils.getInstance().setBooleanKeyValue(Constant.IS_NEED_LOGIN,false);
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    getApplication().startActivity(intent);
                    finish();
//                }else{
//                    Toast.makeText(getApplication(), "登录失败", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}
