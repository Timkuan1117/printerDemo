package com.cybersoft.printerDemo.hardware.hardwareInterface;

import android.content.Context;
import android.graphics.Bitmap;

public interface cHelper {
    void init();
    void bindService(Context context) throws Exception;

    void light(boolean light);
    cPrinter cPrinter();

}
