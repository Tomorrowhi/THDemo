package com.tomorrowhi.thdemo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tomorrowhi.thdemo.R;

/**
 * Created by zhaotaotao on 10/01/2017.
 * dialog弹窗工具类
 */
public class DialogUtil {

    private static AlertDialog progressDialog;

    public static void hide() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void progressDialog(Context context, String showContent, boolean isCancelable) {
        hide();
        if (progressDialog != null) {
            return;
        }
        progressDialog = new AlertDialog.Builder(context).create();
        progressDialog.show();
        View view = View.inflate(context, R.layout.dialog_progress_view, null);
        TextView desc = (TextView) view.findViewById(R.id.progress_description);
        desc.setText(showContent);
        progressDialog.setContentView(view);
        progressDialog.setCanceledOnTouchOutside(isCancelable);
        progressDialog.setCancelable(isCancelable);
    }


}
