package com.hongy.supermarketsystem.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hongy.supermarketsystem.R;

public class EnterDialog extends Dialog{

    private Context mContext;
    private TextView tvCancel,tvEnter;
    private EnterListener listener;

    public EnterDialog(@NonNull Context context,EnterListener listener) {
        super(context);
        mContext = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.enter_dialog_layout,null);
        setContentView(view);
        tvCancel = findViewById(R.id.tv_cancel);
        tvEnter = findViewById(R.id.tv_enter);
        tvCancel.setOnClickListener(new ClickListener());
        tvEnter.setOnClickListener(new ClickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);   //点击外部，不关闭dialog
    }

    private class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_enter:
                    listener.onEnter();
                    break;
                case R.id.tv_cancel:
                    dismiss();
                    break;
            }
        }
    }

    public interface EnterListener{
        void onEnter();
    }
}
