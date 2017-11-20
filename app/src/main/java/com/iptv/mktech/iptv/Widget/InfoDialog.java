package com.iptv.mktech.iptv.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.iptv.mktech.iptv.R;

/**
 * Created by Administrator on 2017/10/17.
 */

public class InfoDialog extends Dialog {
    private TextView mTvCode;
    private TextView mTVExpireDate;
    private String mCode;
    private String mExpireDate;
    private Context mContext;

    public InfoDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_info);
        mTvCode = findViewById(R.id.tv_code);
        mTVExpireDate = findViewById(R.id.tv_expire_date);
        if (mCode != null) {
            mTvCode.setText(mTvCode.getText()+mCode);
        }
        if (mExpireDate != null) {
            mTVExpireDate.setText(mTVExpireDate.getText()+mExpireDate);
        }
    }


    public void setCode(String code) {
        mCode = code;
    }

    public void setCode(int resId) {
        mCode = mContext.getString(resId);
    }

    public void setExpireDate(String date) {
        mExpireDate = date;
    }

    public void setExpireDate(int resid) {
        mExpireDate = mContext.getString(resid);
    }
}
