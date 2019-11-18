package com.hongy.supermarketsystem.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import com.hongy.supermarketsystem.BR;
import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.databinding.ActivityLoginBinding;
import com.hongy.supermarketsystem.viewModel.LoginViewModel;
import me.goldze.mvvmhabit.base.BaseActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {


    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public LoginViewModel initViewModel() {
        return ViewModelProviders.of(this).get(LoginViewModel.class);
    }
}
