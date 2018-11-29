package com.crowd.curtain.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.crowd.curtain.R;


/**
 * Created by zpwan110 on 2017/3/20.
 */

public class LoadingDialog extends Dialog {

    Context context;
    private static LoadingDialog loadingProgress = null;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public static LoadingDialog createDialog(Context context, String text) {
        loadingProgress = new LoadingDialog(context, R.style.dialog_transparent);
        loadingProgress.setContentView(R.layout.loading_progress);
        loadingProgress.getWindow().getAttributes().gravity = Gravity.CENTER;
        loadingProgress.setCanceledOnTouchOutside(false);
        loadingProgress.setCancelable(true);// 设置当前progressdialog能否被返回键取消掉

        if (text != null) {
            TextView textView = (TextView) loadingProgress.findViewById(R.id.loading_textView);
            textView.setText(text);
        }

        return loadingProgress;
    }
    public void onWindowFousChanged(boolean hasFocus) {
        if (loadingProgress == null) {
            return;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        loadingProgress = null;
    }

    @Override
    public void cancel() {
        super.cancel();
        loadingProgress = null;
    }
}
