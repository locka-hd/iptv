package com.iptv.mktech.iptv.Widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.iptv.mktech.iptv.R;

/**
 * Created by Administrator on 2017/10/17.
 */

public class ExitDialog extends Dialog {
    private TextView mTvContent;
    private String mText;
    private Context mContext;
    private OnExitDialogInterface mOnExitDialogInterface;

    private String yes = String.format("<<      %s     >>", "Yes");
    private String no = String.format("<<      %s      >>", "No");

    public ExitDialog(@NonNull Context context, OnExitDialogInterface onExitDialogInterface) {
        super(context);
        mContext = context;
        mOnExitDialogInterface = onExitDialogInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_exit);
        mTvContent = findViewById(R.id.tv_select);
        mTvContent.setText(yes);
        mTvContent.requestFocus();
        mTvContent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.e("hehui", keyEvent.toString());
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && view.getId() == R.id.tv_select) {
                    String string = mTvContent.getText().toString();
                    switch (keyEvent.getKeyCode()) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            if (string.contains("Yes")) {
                                mOnExitDialogInterface.onConfirm();
                            } else {
                                mOnExitDialogInterface.onCancel();
                            }
                            break;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            if (string.contains("Yes")) {
                                mTvContent.setText(no);
                            } else {
                                mTvContent.setText(yes);
                            }
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        });


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

    public interface OnExitDialogInterface {
        void onConfirm();

        void onCancel();
    }
}
