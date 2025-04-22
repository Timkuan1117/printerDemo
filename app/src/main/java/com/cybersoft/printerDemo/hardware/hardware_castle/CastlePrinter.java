package com.cybersoft.printerDemo.hardware.hardware_castle;

import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.LINE_SPACE_XL;
import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.LINE_SPACE_XXL;

import android.graphics.Bitmap;
import android.os.Handler;

import com.cybersoft.printerDemo.hardware.hardwareInterface.cPrinter;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterManager;
import com.cybersoft.printerDemo.utils.LoggerUtil;

import CTOS.CtPrint;

public class CastlePrinter implements cPrinter {
    private final static String TAG = CastlePrinter.class.getSimpleName();
    private CtPrint printer;
    private String printType;
    public CastlePrinter() {
        printer = new CtPrint();
    }


    @Override
    public void init(int w , int h) {
        printer.initPage(h);
    }

    @Override
    public int getStatus() {
        return printer.status();
    }

    @Override
    public void fontSet(int fontSize) {

    }

    @Override
    public void spaceSet(byte wordSpace, byte lineSpace) {

    }

    @Override
    public void printStr(String str, String charset) {


    }

    @Override
    public void step(int b) {
        printer.roll(b);

    }

    @Override
    public void printBitmap(Bitmap bitmap) {
        printer.drawImage(bitmap,0,0);
    }

    @Override
    public boolean start(Handler printHandler , int requestcode ,int forceRetry ) {
        int ret = 0;
        try {
            ret = printer.printPageEx();
            printer.roll(LINE_SPACE_XXL+LINE_SPACE_XL);
            PrinterConstant.PrintResultCode resultCode = devPriResultToPrintResult(ret);
            printHandler.sendMessage(printHandler.obtainMessage(
                    PrinterManager.PRINT_RES, requestcode , forceRetry ,resultCode));
            if (resultCode == PrinterConstant.PrintResultCode.SUCCESS){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            PrinterConstant.PrintResultCode resultCode = PrinterConstant.PrintResultCode.UNKNOWN;
            printHandler.sendMessage(printHandler.obtainMessage(
                    PrinterManager.PRINT_RES,requestcode, forceRetry , resultCode ));
            return false;

        }
    }

    @Override
    public void leftIndents(short indent) {

    }

    @Override
    public int getDotLine() {
        return 0;
    }

    @Override
    public void setGray(int level) {

    }

    @Override
    public void setDoubleWidth(boolean isAscDouble, boolean isLocalDouble) {

    }

    @Override
    public void setDoubleHeight(boolean isAscDouble, boolean isLocalDouble) {

    }

    @Override
    public void setInvert(boolean isInvert) {

    }

    @Override
    public void cutPaper(int mode) {

    }

    @Override
    public String getPrintType() {
        return "";
    }

    @Override
    public void setPrintType(String printType) {

    }

    @Override
    public PrinterConstant.PrintResultCode devPriResultToPrintResult(int status) {
        LoggerUtil.d(TAG, "devPriResultToPrintResult status: " + status);
        switch (status) {
            case 0:
                return PrinterConstant.PrintResultCode.SUCCESS;
            case 0x02:
                return PrinterConstant.PrintResultCode.OVERHEAT;
            case 0x03:
                return PrinterConstant.PrintResultCode.NO_PAPER;
            case 0x04://條碼生成錯誤
            case 0x05://條碼內容錯誤
            case 0x08://參數錯誤
            case 0x09://圖片格式不支援
            case 0x0A://圖片開啟失敗
                return PrinterConstant.PrintResultCode.FORMAT_ERROR;
            case 0x06://條碼內容長度錯誤
            case 0x07://條碼超出紙張範圍
            case 0x0B://圖片大小超過限制
                return PrinterConstant.PrintResultCode.DATA_TOO_LONG;

            case 0x0C://HAL 層硬體錯誤
                return PrinterConstant.PrintResultCode.MALFUNCTION;

            case 0x13://不支援的語言類型
                return PrinterConstant.PrintResultCode.NO_FONT;
            case 1:
                return PrinterConstant.PrintResultCode.BUSY;
            case 0x12://裝置不支援此功能
            default:
                return PrinterConstant.PrintResultCode.UNKNOWN;
        }
    }

}