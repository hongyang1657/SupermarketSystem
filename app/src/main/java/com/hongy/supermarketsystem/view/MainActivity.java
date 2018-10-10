package com.hongy.supermarketsystem.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.fragment.CashierFragment;
import com.hongy.supermarketsystem.fragment.EnteringFragment;
import com.hongy.supermarketsystem.fragment.ListFragment;
import com.hongy.supermarketsystem.fragment.MineFragment;


public class MainActivity extends Activity {

    private FragmentManager fm;
    private FragmentTransaction transaction;
    private CashierFragment cashierFragment;
    private EnteringFragment enteringFragment;
    private ListFragment listFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initView();
    }

    private void initView(){
        fm = getFragmentManager();
        transaction = fm.beginTransaction();
        cashierFragment = new CashierFragment();
        enteringFragment = new EnteringFragment();
        listFragment = new ListFragment();
        mineFragment = new MineFragment();
        transaction.replace(R.id.fl_blank, cashierFragment);
        transaction.add(R.id.fl_blank,enteringFragment);
        transaction.add(R.id.fl_blank,listFragment);
        transaction.add(R.id.fl_blank,mineFragment);
        transaction.show(cashierFragment);
        transaction.hide(enteringFragment);
        transaction.hide(listFragment);
        transaction.hide(mineFragment);
        transaction.commit();
    }

    public void click(View view){
        transaction = fm.beginTransaction();
        switch (view.getId()){
            case R.id.rb_scan:

                transaction.show(cashierFragment);
                transaction.hide(enteringFragment);
                transaction.hide(listFragment);
                transaction.hide(mineFragment);
                transaction.commit();
                break;
            case R.id.rb_entering:
                transaction.show(enteringFragment);
                transaction.hide(cashierFragment);
                transaction.hide(listFragment);
                transaction.hide(mineFragment);
                transaction.commit();
                break;
            case R.id.rb_list:
                transaction.show(listFragment);
                transaction.hide(enteringFragment);
                transaction.hide(cashierFragment);
                transaction.hide(mineFragment);
                transaction.commit();
                break;
            case R.id.rb_mine:
                transaction.show(mineFragment);
                transaction.hide(enteringFragment);
                transaction.hide(listFragment);
                transaction.hide(cashierFragment);
                transaction.commit();
                break;
            default:

                break;
        }
    }
}
