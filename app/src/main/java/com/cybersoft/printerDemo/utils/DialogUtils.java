package com.cybersoft.printerDemo.utils;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


/**
 * Created by goldze on 2017/5/10.
 */

public class DialogUtils {
    private static boolean shouldDismissDialog = false;
    private static AlertDialog dialog = null;

    // 顯示進度對話框
    public static void show(Context context, String content, boolean horizontal) {

        if (dialog != null && dialog.isShowing()) {
            return; // 如果已經有對話框顯示，就不再顯示
        }
        shouldDismissDialog = false; // 重設
        dialog = showIndeterminateProgressDialog(context, content, horizontal);
        dialog.setOnShowListener(d -> {
            Log.d("DialogDebug", "dialog is showing" + dialog.isShowing() + shouldDismissDialog);
            if (shouldDismissDialog && dialog.isShowing()) {
                Log.d("DialogDebug", "dialog is need dissmiss");
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    // 隱藏進度對話框
    public static void dismiss() {

        if (dialog != null && dialog.isShowing()) {
            Log.d("DialogDebug", "dialog is showing, going to dismiss");
            dialog.dismiss();
            dialog = null;  // 確保 dialog 被設置為 null
        }else {
            Log.d("DialogDebug", "dialog is null or not showing");
            shouldDismissDialog = true;
        }

    }

    // 顯示進度對話框的實現方法
    private static AlertDialog showIndeterminateProgressDialog(Context context, String content, boolean horizontal) {
        // 創建一個容器來放進度條
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 100, 40, 100);  // 設置內邊距，讓對話框內有空間

        // 創建並配置進度條
        ProgressBar progressBar = new ProgressBar(context);
        progressBar.setIndeterminate(true);  // 設置為不確定模式

        if (horizontal) {
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        } else {
            progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        layout.addView(progressBar);  // 將進度條加到布局中

        // 創建並配置 AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(content)
                .setCancelable(false)  // 不可取消
                .setView(layout)  // 設置自定義視圖
                .setOnKeyListener((dialogInterface, keyCode, event) -> {
                    if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            // 阻止返回鍵關閉對話框
                            return true;
                        }
                    }
                    return false;
                })
                .create();

        return dialog;
    }

}
