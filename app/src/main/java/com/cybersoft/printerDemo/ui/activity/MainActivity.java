package com.cybersoft.printerDemo.ui.activity;



import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterManager.RETRY_NO_FORCE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.cybersoft.printerDemo.R;
import com.cybersoft.printerDemo.base.BaseActivity;
import com.cybersoft.printerDemo.databinding.ActivityMainBinding;
import com.cybersoft.printerDemo.event.PrintDialogEvent;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterManager;
import com.cybersoft.printerDemo.ui.viewModel.MainViewModel;
import com.cybersoft.printerDemo.utils.DialogUtils;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding> {

    @Override
    protected int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected MainViewModel initViewModel() {
        return new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    protected void initViewObservable() {
        viewModel.toastEvent.observe(this, msg -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        viewModel.showPrintErrorDialogEvent.observe(this, new Observer<PrintDialogEvent>() {
            @Override
            public void onChanged(PrintDialogEvent event) {
                showPrintErrorDialog(event.resultCode, event.requestCode, event.handler);
            }
        });
        viewModel.showProgressEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String title) {
                DialogUtils.show(MainActivity.this,title,true);
            }
        });
        viewModel.dismissDialogEvent.observe(this, unused -> {
            DialogUtils.dismiss();
        });
    }

    private void showPrintErrorDialog(PrinterConstant.PrintResultCode result , final int requestCode , final Handler handler) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("列印失敗")
                .setMessage(result.getDescription() + "\n是否要重新列印？")
                .setCancelable(false)
                .setPositiveButton("重新列印", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 按下「重新列印」時發送重印訊息給 Handler
                        Message msg = handler.obtainMessage(requestCode, PrinterManager.SUERBUTTON, PrinterManager.SUERBUTTON);
                        handler.sendMessage(msg);
                    }
                });
        if (requestCode == RETRY_NO_FORCE){
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Message msg = handler.obtainMessage(requestCode,PrinterManager.CANCLEBUTTON,PrinterManager.CANCLEBUTTON);
                    handler.sendMessage(msg);
                }
            });
        }
        builder.show();
    }


}
