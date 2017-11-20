package com.iptv.mktech.iptv.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.iptv.mktech.iptv.R;

/**
 * Created by Administrator on 2017/10/17.
 */

public class WarningDialog extends Dialog {
    private TextView mTvContent;
    private String mText;
    private Context mContext;

    public WarningDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_warning);
        mTvContent = findViewById(R.id.tv_content);
        if (mText != null) {
            mTvContent.setText(mText);
        }
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setText(int textId) {
        mText = mContext.getString(textId);
    }
}
