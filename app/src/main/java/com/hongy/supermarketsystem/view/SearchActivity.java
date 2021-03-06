package com.hongy.supermarketsystem.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hongy.supermarketsystem.R;
import com.hongy.supermarketsystem.utils.L;


public class SearchActivity extends Activity{

    private EditText etSerach;
    private TextView tvSerach,tvTips;
    private String content = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);
        initView();
    }

    private void initView(){
        content = getIntent().getStringExtra("content");
        etSerach = findViewById(R.id.et_search);
        tvSerach = findViewById(R.id.tv_bt_search);
        tvTips = findViewById(R.id.tv_tips);
        L.i("11111111111"+getIntent().getStringExtra("tips")+"ssssssssss:"+content);
        tvTips.setText(getIntent().getStringExtra("tips"));
        etSerach.setFocusable(true);
        etSerach.setFocusableInTouchMode(true);
        etSerach.requestFocus();
        etSerach.setText(content);
        etSerach.setSelection(content.length());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        tvSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                Intent intent = new Intent();
                intent.putExtra("serachContent",etSerach.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        etSerach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {  //搜索按键action
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    // 隐藏软键盘
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    Intent intent = new Intent();
                    intent.putExtra("serachContent",etSerach.getText().toString().trim());
                    setResult(RESULT_OK,intent);
                    finish();
                }
                return false;
            }
        });
    }
}
