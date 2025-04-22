package com.cybersoft.printerDemo.ui.viewModel;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.cybersoft.printerDemo.base.BaseViewModel;
import com.cybersoft.printerDemo.base.SingleLiveEvent;
import com.cybersoft.printerDemo.event.PrintDialogEvent;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrintUtil;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterManager;
import com.pax.gl.page.IPage;
import java.util.List;

public class MainViewModel extends BaseViewModel {
    private String TAG = MainViewModel.class.getSimpleName();
    public MutableLiveData<String> printNum = new MutableLiveData<>("1");
    public SingleLiveEvent<PrintDialogEvent> showPrintErrorDialogEvent = new SingleLiveEvent<>();

    public SingleLiveEvent<String> showProgressEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> dismissDialogEvent= new SingleLiveEvent<>();


    private Context appContext;
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.appContext = application.getApplicationContext();
    }


    public void showPrintError(PrinterConstant.PrintResultCode resultCode, int forceRetry, Handler handler) {
        showPrintErrorDialogEvent.postValue(new PrintDialogEvent(resultCode, forceRetry,handler));
    }

    public void showProgress(String title) {
        showProgressEvent.postValue(title);
    }

    public void dismissProgress() {
        dismissDialogEvent.call();
    }



    private void callPrint(final Context context , boolean isReprint){
        List<IPage> printModel =   PrinterManager.getInstance().getCacheModel();
        boolean forceRetry = false;
        if (printModel.size()<=3){
            forceRetry = true;
        }
        PrinterManager.getInstance().callPrintAllData(context, printModel,isReprint ,forceRetry, new PrinterManager.OnPrinterListener() {

            @Override
            public void onPrintSuc() {
                Log.d(TAG ,"打印成功");
                PrinterManager.getInstance().clearCache();
                dismissProgress();
            }
            @Override
            public void onPrintErr(int forceRetry, Handler handler, PrinterConstant.PrintResultCode printResult) {
                Log.d(TAG ,"ERROR:"+printResult + ",forceRetry:"+forceRetry);
                dismissProgress();
                showPrintError(printResult, forceRetry, handler);
            }

            @Override
            public void onPrintCancel(int requestCode) {
                Log.d(TAG,requestCode +":不重印");
                PrinterManager.getInstance().clearCache();
                dismissProgress();

            }
            @Override
            public void onPrintConfrim(int requestCode) {
                Log.d(TAG,requestCode +":重印");
                showProgress("重新列印");
                callPrint(context ,true);
            }
        });
    }
    public void onClickStartPrint() {
        //toastEvent.setValue("StratPrint!");
        showProgress("開始列印");
        PrintUtil.printScript(getApplication(), Integer.valueOf(printNum.getValue()) );
        callPrint(getApplication(),false);
    }
}