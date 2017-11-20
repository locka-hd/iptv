package com.iptv.mktech.iptv.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.mktech.iptv.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/10/16.
 */


public class EditDialog extends Dialog {
    private String mTitle;
    private EditText mEditText;
    private Context mContext;
    private String mText;
    private OnEditDialogInterface mOnEditDialogInterface;
    private boolean mDissMissDialog = true;
    private String mHint;

    public EditDialog(Context context, OnEditDialogInterface onEditDialogInterface) {
        super(context);
        mContext = context;
        mOnEditDialogInterface = onEditDialogInterface;
    }

    public EditDialog(Context context, boolean dissMissDialog, OnEditDialogInterface onEditDialogInterface) {
        super(context);
        mContext = context;
        mOnEditDialogInterface = onEditDialogInterface;
        mDissMissDialog = dissMissDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_editext);
        if (mTitle != null) {
            TextView tvTitle = (TextView) findViewById(R.id.tv_dialog_title);
            tvTitle.setText(mTitle);
        }
        mEditText = (EditText) findViewById(R.id.et_input);
        if (mText != null) {
            mEditText.setText(mText);
            mEditText.setSelection(mText.length());
        }
        if (mHint != null) {
            mEditText.setHint(mHint);
        }
        mEditText.requestFocus();
        findViewById(R.id.tv_cancel).setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_confirm).setOnClickListener(mOnClickListener);

        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.y = -100;
        dialogWindow.setAttributes(lp);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_cancel:
                    if (mOnEditDialogInterface != null) {
                        mOnEditDialogInterface.onCancel();
                    }
                    break;
                case R.id.tv_confirm:
                    if (mOnEditDialogInterface != null) {
                        String str = mEditText.getText().toString();
                        if (str != null && str.length() > 0) {
                            mOnEditDialogInterface.onConfirm(mEditText.getText().toString());
                        }
                    }
                    break;
            }
            if (mDissMissDialog) {
                dismiss();
            }
        }
    };

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public void setTitle(int titleId) {
        mTitle = mContext.getString(titleId);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setText(int textId) {
        mText = mContext.getString(textId);
    }

    public void setHint(String hint) {
        mHint = hint;
    }

    public void setHint(int hintId) {
        mHint = mContext.getString(hintId);
    }

    public void clearContentText() {
        if (null != mEditText) {
            mEditText.setText("");
        }
    }

    @Override
    public void show() {
        super.show();
        mEditText.requestFocus();
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mEditText, 0);
            }
        }, 100);

    }

    public interface OnEditDialogInterface {
        void onConfirm(String text);

        void onCancel();
    }
}