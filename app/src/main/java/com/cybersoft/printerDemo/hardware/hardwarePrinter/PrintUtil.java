package com.cybersoft.printerDemo.hardware.hardwarePrinter;

import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.*;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cybersoft.printerDemo.R;
import com.pax.gl.page.IPage;
import com.pax.gl.page.PaxGLPage;

import java.util.ArrayList;
import java.util.List;


// 管理打印樣式
public class PrintUtil {
    private final static String TAG = PrintUtil.class.getSimpleName();
    private static final String SPACE = " ";
    public static void printScript(Context context , int PageNum ) {
        Log.d(TAG , "建立打印單"+PageNum);
        List<IPage> iPages = new ArrayList<>();

        for(int i = 0 ; i<PageNum ; i++){
            Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.mgr_cs_logo);
            iPages.add(getNotSettleYetSheet(context,logo,i));
        }
        PrinterManager.getInstance().setCacheModel(iPages);
    }

    public  static IPage getNotSettleYetSheet(Context context,Bitmap logo,int i) {
        PaxGLPage paxGLPage = PaxGLPage.getInstance(context);
        IPage iPage = paxGLPage.createPage();
        iPage.setTypeFace(PRINTER_FONT);
        iPage.adjustLineSpace(ADJUST_LINE_SPACE);
        iPage.addLine().addUnit(scaleBitmapToWidth(logo,384));
        iPage.addLine().addUnit("疑似網路異常\n導致刷卡機尚未結帳\n請聯絡系統人員，謝謝", TXT_XXXL, CENTER, TXT_BOLD);
        iPage.addLine().addUnit(SPACE, LINE_SPACE, CENTER);
        iPage.addLine().addUnit("頁數"+i, TXT_SS, CENTER, TXT_BOLD);
        return iPage;
    }
    public static Bitmap scaleBitmapToWidth(Bitmap original, int targetWidth) {
        int originalWidth = original.getWidth();
        int originalHeight = original.getHeight();

        if (originalWidth <= targetWidth) {
            return original; // 不需要縮小
        }

        float scaleFactor = (float) targetWidth / originalWidth;
        int targetHeight = (int) (originalHeight * scaleFactor);

        return Bitmap.createScaledBitmap(original, targetWidth, targetHeight, true);
    }
}

