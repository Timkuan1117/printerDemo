package com.cybersoft.printerDemo.hardware.hardwareInterface;

import android.graphics.Bitmap;
import android.os.Handler;

import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant;

public interface cPrinter {

    void init(int w , int h);

    int getStatus();

    void fontSet(int fontSize);

    void spaceSet(byte wordSpace, byte lineSpace);

    void printStr(String str, String charset);
    //走紙
    void step(int b);

    void printBitmap(Bitmap bitmap);

    boolean start(Handler handler , int requestcode , int forceRetry );

    void leftIndents(short indent);

    int getDotLine();

    void setGray(int level);

    void setDoubleWidth(boolean isAscDouble, boolean isLocalDouble);

    void setDoubleHeight(boolean isAscDouble, boolean isLocalDouble);

    void setInvert(boolean isInvert);

    void cutPaper(int mode);

    String getPrintType();

    void setPrintType(String printType);

    PrinterConstant.PrintResultCode devPriResultToPrintResult(int status);
}
