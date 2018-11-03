package com.hongy.supermarketsystem.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hongy.supermarketsystem.R;

public class SearchActivity extends Activity{

    private EditText etSerach;
    private TextView tvSerach;
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
        etSerach.setFocusable(true);
        etSerach.setFocusableInTouchMode(true);
        etSerach.requestFocus();
        etSerach.setText(content);
        etSerach.setSelection(content.length());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        tvSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("serachContent",etSerach.getText().toString().trim());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
