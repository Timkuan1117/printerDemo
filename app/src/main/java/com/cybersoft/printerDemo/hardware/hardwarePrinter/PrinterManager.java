package com.cybersoft.printerDemo.hardware.hardwarePrinter;

import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.PAGE_WIDTH;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.cybersoft.printerDemo.App;
import com.cybersoft.printerDemo.hardware.HardwareManager;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cPrinter;
import com.pax.gl.page.IPage;
import com.pax.gl.page.PaxGLPage;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.PrintResultCode;
import java.util.List;

public class PrinterManager {
    private String TAG = PrinterManager.class.getSimpleName();
    private static PrinterManager mManager;
    private OnPrinterListener onPrinterListener;
    private IPage readyPrtPages;
    private int readyPrintIndex;
    private List<IPage> cacheModel;
    private PaxGLPage paxGLPage;

    private boolean allPrint;
    public static final int PRINT_RES = 0x10168;
    // 顯示兩種


    public static final int SUERBUTTON= 0x2024;
    public static final int CANCLEBUTTON= 0x2025;

    public static final int PRINT_CALL_PRINTA = 100; // 單印
    public static final int PRINT_CALL_ALLPRINTA = 200; // 多聯列印

    public static final int RETRY_FORCE = 3333;

    public static final int RETRY_NO_FORCE = 2222;


    private PrinterManager(){
    }

    public static PrinterManager getInstance() {
        if (mManager == null) {
            synchronized (PrinterManager.class) {
                if (mManager == null) {
                    mManager = new PrinterManager();
                }
            }
        }
        return mManager;
    }

    /**
     *
     *
     * @param pages 打印內容
     * @param linster 監聽畫面動作
     * @param forceRetry 若有錯誤畫面Dailog，是否可以取消不列印
     *                    若為 True ->RETRY_FORCE 只能不斷跳出重印Dialog
     *                    若為 False->RETRY_NO_FORCE 若發生錯誤，使用者可以結束列印
     */
    public void callPrintData(Context context , final  IPage pages, boolean forceRetry , OnPrinterListener linster){
        final cPrinter printer = HardwareManager.getInstance().getPrinter();
        final int forceRetryFlag = forceRetry ? RETRY_FORCE : RETRY_NO_FORCE;
        this.onPrinterListener = linster;
        this.readyPrtPages = pages;
        // 判別有沒有打印資料
        if(pages == null){
            onPrinterListener.onPrintErr(forceRetryFlag ,printHandler ,PrintResultCode.NO_DATA_PRINT);
            return;
        }else if (printer ==null){ // 判別是否有打印設備
            onPrinterListener.onPrintErr(forceRetryFlag ,printHandler,PrintResultCode.DEVICE_CANT_INIT);
            return;
        }else  if (paxGLPage == null){
            paxGLPage = PaxGLPage.getInstance(context.getApplicationContext());
        }
        new Thread(new Runnable() {
            public void run() {
                Bitmap bitmap = paxGLPage.pageToBitmap(readyPrtPages ,PAGE_WIDTH);
                // 判別設備狀態
                printer.init(384,bitmap.getHeight());
                printer.printBitmap(bitmap);
                printer.start(printHandler,PRINT_CALL_PRINTA,forceRetryFlag);
            }
        }).start();
    }

    /**
     *
     *
     * @param pages 打印內容
     * @param isReprint 重印
     *                  -> 若為True 則從錯誤的地方重印
     *                  -> 若為False 則從頭開始印
     *
     * @param forceRetry 若有錯誤畫面Dailog，是否可以取消不列印
     *                    若為 True ->RETRY_FORCE 只能不斷跳出重印Dialog
     *                    若為 False->RETRY_NO_FORCE 若發生錯誤，使用者可以結束列印
     * @param linster 監聽畫面動作
     */

    public void callPrintAllData(Context context , final  List<IPage> pages ,boolean isReprint,boolean forceRetry , OnPrinterListener linster  ){
        final cPrinter printer = HardwareManager.getInstance().getPrinter();
        final int forceRetryFlag = forceRetry ? RETRY_FORCE : RETRY_NO_FORCE;
        this.onPrinterListener = linster;
        // 判別有沒有打印資料
        if(pages == null){
            onPrinterListener.onPrintErr(forceRetryFlag ,printHandler ,PrintResultCode.NO_DATA_PRINT);
            return;
        }else if (printer ==null){ // 判別是否有打印設備
            onPrinterListener.onPrintErr(forceRetryFlag ,printHandler,PrintResultCode.DEVICE_CANT_INIT);
            return;
        }else  if (paxGLPage == null){
            paxGLPage = PaxGLPage.getInstance(context.getApplicationContext());
        }

        // 如果是重印 則判斷 是否大於 打印資料，避免重印錯誤
        readyPrintIndex = (!isReprint || readyPrintIndex >= pages.size()) ? 0 : readyPrintIndex;
        new Thread(new Runnable() {
            public void run() {
                allPrint = false;
                boolean printstatus = true;
                for (int i = readyPrintIndex ; i < pages.size() ; i++){
                    if (!printstatus){
                        break;
                    }
                    IPage page = pages.get(i);
                    Bitmap bitmap = paxGLPage.pageToBitmap(page,PAGE_WIDTH);
                    if (bitmap!=null && bitmap.getWidth()>0 && bitmap.getHeight()>0){
                        // 判別設備狀態
                        printer.init(PAGE_WIDTH,bitmap.getHeight());
                        printer.printBitmap(bitmap);
                        printstatus = printer.start(printHandler,PRINT_CALL_ALLPRINTA ,forceRetryFlag);
                        if (printstatus){
                            readyPrintIndex = i;
                        }

                    }

                }
                allPrint = printstatus;
            }
        }).start();
    }

    /**
     *
     *
     * @param msg.what  回應種類
     * @param msg.arg1  觸發來源
     *        msg.arg2  回應Dialog所需類型
     *                  RETRY_PRINT 只能不斷跳出重印Dialog
     *                  PRINT_CLOSE 若發生錯誤，使用者可以結束列印
     *        msg.obj   回應結果
     */
    private Handler printHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PRINT_RES:// 印表機回調
                    PrintResultCode resultCode = (PrintResultCode) msg.obj;
                    switch (resultCode) {
                        case SUCCESS:
                            if (msg.arg1 ==PRINT_CALL_ALLPRINTA && !allPrint ){
                                // callPrintAllData 還未打印完成
                            }else {
                                onPrinterListener.onPrintSuc();
                            }
                            break;
                        case BUSY:
                        case NO_PAPER:// 處理缺紙邏輯
                        case FORMAT_ERROR:// 處理格式錯誤
                        case MALFUNCTION:// 處理機器故障
                        case OVERHEAT:// 處理過熱
                        case NO_POWER:// 處理未連接電源
                        case UNFINISHED:// 處理列印未完成
                        case NO_FONT:// 處理缺字型
                        case DATA_TOO_LONG:// 處理資料太長
                        case UNKNOWN: // 處理未知異常
                            // msg.arg2 告訴 UI 是否強制重印
                            onPrinterListener.onPrintErr(msg.arg2,printHandler,resultCode);
                            break;
                    }
                    break;
                case RETRY_NO_FORCE:{ //Dialog 回調
                    switch (msg.arg1) {
                        case SUERBUTTON:
                            onPrinterListener.onPrintConfrim(RETRY_FORCE);
                            break;
                        case CANCLEBUTTON:
                            onPrinterListener.onPrintCancel(RETRY_FORCE);
                            break;
                    }
                    break;
                }
                case RETRY_FORCE:{
                    onPrinterListener.onPrintConfrim(RETRY_FORCE);
                    break;
                }
            }

        }
    };



    public void setCacheModel (List<IPage> model)
    {
        this.cacheModel = model;
    }

    public List<IPage> getCacheModel ()
    {
        return this.cacheModel;
    }

    public void clearCache ()
    {
        if (cacheModel == null) return;
        for (IPage page : cacheModel) {
            recyclePageBitmaps(page);
        }
        cacheModel.clear(); // 清空整個頁面列表
        cacheModel = null;
    }

    private static void recyclePageBitmaps(IPage page) {
        if (page == null) return;

        List<IPage.ILine> lines = page.getLines();
        if (lines != null) {
            for (IPage.ILine line : lines) {
                if (line == null) continue;

                List<IPage.ILine.IUnit> units = line.getUnits();
                if (units != null) {
                    for (IPage.ILine.IUnit unit : units) {
                        if (unit == null) continue;
                        Bitmap bmp = unit.getBitmap();
                        if (bmp != null && !bmp.isRecycled()) {
                            bmp.recycle();
                            unit.setBitmap(null); // 清除參考，避免記憶體洩漏
                        }
                    }
                    units.clear(); // Optional: 清空 units 清單
                }
            }
            lines.clear(); // Optional: 清空 lines 清單
        }
    }

    public interface OnPrinterListener
    {
        // 列印失敗，通知執行端 修改ＵＩ
        public void onPrintErr (int forceRetry , Handler handler , PrinterConstant.PrintResultCode printResult);
        // 列印成功，通知執行端
        public void onPrintSuc ();
        // 列印失敗，跳出Dialog通知使用者，使用者選擇項目
        public void onPrintCancel (int requestCode);
        // 列印失敗，跳出Dialog通知使用者，使用者選擇項目
        public void onPrintConfrim (int requestCode);

    }

}
