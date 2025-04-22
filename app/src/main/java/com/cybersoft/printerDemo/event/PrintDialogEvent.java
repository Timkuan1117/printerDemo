package com.cybersoft.printerDemo.event;

import android.os.Handler;

import com.cybersoft.printerDemo.hardware.hardwarePrinter.PrinterConstant;

public class PrintDialogEvent {
    public PrinterConstant.PrintResultCode resultCode;
    public int requestCode;
    public Handler handler;

    public PrintDialogEvent(PrinterConstant.PrintResultCode resultCode, int requestCode, Handler handler) {
        this.resultCode = resultCode;
        this.requestCode = requestCode;
        this.handler = handler;
    }
}
