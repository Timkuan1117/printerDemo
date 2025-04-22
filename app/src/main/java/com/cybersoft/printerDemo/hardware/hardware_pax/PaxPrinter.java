package com.cybersoft.printerDemo.hardware.hardware_pax;

import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.GRAY;
import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.LINE_SPACE_M;
import static com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant.LINE_SPACE_XXL;
import android.graphics.Bitmap;
import android.os.Handler;

import com.cybersoft.printerDemo.hardware.HardwareManager;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cPrinter;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant;
import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterManager;
import com.cybersoft.printerDemo.utils.LoggerUtil;
import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;

public class PaxPrinter implements cPrinter {
    private final static String TAG = PaxPrinter.class.getSimpleName();
    private IPrinter printer;
    private String printType;
    public PaxPrinter(IPrinter paxPrinter) {
        this.printer = paxPrinter;
    }
    @Override
    public void init(int w , int h ) {
        LoggerUtil.d(TAG, "init()");
        try {
            while (printer.getStatus() == 1) {
                LoggerUtil.d(TAG, "wait");
            }
            try {
                printer.init();
                printer.setGray(GRAY);
                printType = "default";
            } catch (PrinterDevException e) {
                LoggerUtil.d(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (PrinterDevException e) {
            LoggerUtil.d(TAG, e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public int getStatus() {
        try {
            int status = printer.getStatus();
            return  devPriResultToPrintResult(status).getCode();
        } catch (PrinterDevException e) {
            LoggerUtil.d(TAG, e.toString());
            return  PrinterConstant.PrintResultCode.UNKNOWN.getCode();
        }
    }

    @Override
    public void fontSet(int fontSize) {
        try {
            switch (fontSize)
            {
                case 16:
                    printer.fontSet(EFontTypeAscii.FONT_8_16, EFontTypeExtCode.FONT_16_16);
                    break;
                case 24:
                    printer.fontSet(EFontTypeAscii.FONT_12_24, EFontTypeExtCode.FONT_24_24);
                    break;
                case 32:
                    printer.fontSet(EFontTypeAscii.FONT_16_32, EFontTypeExtCode.FONT_32_32);
                    break;
                case 48:
                    printer.fontSet(EFontTypeAscii.FONT_24_48, EFontTypeExtCode.FONT_48_48);
                    break;
                default:
                    printer.fontSet(EFontTypeAscii.FONT_8_16, EFontTypeExtCode.FONT_16_16);
                    break;
            }
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void spaceSet(byte wordSpace, byte lineSpace) {
        try {
            printer.spaceSet(wordSpace, lineSpace);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void printStr(String str, String charset) {
        try {
            printer.printStr(str, charset);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void step(int b) {
        try {
            printer.step(b);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void printBitmap(Bitmap bitmap) {
        try {
            LoggerUtil.d(TAG, "printBitmap()");
            printer.printBitmap(bitmap);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean start(Handler printHandler , int requestcode , int forceRetry) {
        try {
            LoggerUtil.d(TAG, "start()");
            printer.step(LINE_SPACE_XXL+LINE_SPACE_M);
            int res = printer.start();
            PrinterConstant.PrintResultCode resultCode = devPriResultToPrintResult(res);
            printHandler.sendMessage(printHandler.obtainMessage(
                    PrinterManager.PRINT_RES, requestcode , forceRetry ,resultCode ));
            if (resultCode == PrinterConstant.PrintResultCode.SUCCESS){
                return true;
            }else {
                return false;
            }
        } catch (PrinterDevException e) {
            PrinterConstant.PrintResultCode resultCode = PrinterConstant.PrintResultCode.UNKNOWN;
            printHandler.sendMessage(printHandler.obtainMessage(
                    PrinterManager.PRINT_RES,requestcode, forceRetry , resultCode ));
            return false;
        }
    }
    @Override
    public void leftIndents(short indent) {
        try {
            printer.leftIndent(indent);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getDotLine() {
        try {
            int dotLine = printer.getDotLine();
            return dotLine;
        } catch (PrinterDevException e) {
            e.printStackTrace();
            return -2;
        }
    }
    @Override
    public void setGray(int level) {
        try {
            printer.setGray(level);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void setDoubleWidth(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleWidth(isAscDouble, isLocalDouble);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setDoubleHeight(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleHeight(isAscDouble, isLocalDouble);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setInvert(boolean isInvert) {
        try {
            printer.invert(isInvert);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void cutPaper(int mode) {
        try {
            printer.cutPaper(mode);
        } catch (PrinterDevException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getPrintType() {
        return printType;
    }
    @Override
    public void setPrintType(String printType) {
        this.printType = printType;
    }
    @Override
    public PrinterConstant.PrintResultCode devPriResultToPrintResult(int status) {
        LoggerUtil.d(TAG, "devPriResultToPrintResult status: " + status);
        switch (status) {
            case 0:
                return PrinterConstant.PrintResultCode.SUCCESS;
            case 1:
                return PrinterConstant.PrintResultCode.BUSY;
            case 2:
                return PrinterConstant.PrintResultCode.NO_PAPER;
            case 3:
                return PrinterConstant.PrintResultCode.FORMAT_ERROR;
            case 4:
                return PrinterConstant.PrintResultCode.MALFUNCTION;
            case 8:
                return PrinterConstant.PrintResultCode.OVERHEAT;
            case 9:
                return PrinterConstant.PrintResultCode.NO_POWER;
            case 240:
                return PrinterConstant.PrintResultCode.UNFINISHED;
            case 252:
                return PrinterConstant.PrintResultCode.NO_FONT;
            case 254:
                return PrinterConstant.PrintResultCode.DATA_TOO_LONG;
            default:
                return PrinterConstant.PrintResultCode.UNKNOWN;
        }
    }
}
