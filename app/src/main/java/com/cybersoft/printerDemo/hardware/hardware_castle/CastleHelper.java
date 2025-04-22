package com.cybersoft.printerDemo.hardware.hardware_castle;

import android.content.Context;
import android.graphics.Bitmap;

import com.cybersoft.printerDemo.hardware.hardwareInterface.cHelper;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cPrinter;

import CTOS.CtPrint;

public class CastleHelper implements cHelper {

    private cPrinter ctPrinter;
    @Override
    public void init() {}
    @Override
    public void bindService(Context context) throws Exception {}

    @Override
    public void light(boolean light) {

    }
    @Override
    public cPrinter cPrinter() {
        if (ctPrinter == null) {
            ctPrinter = new CastlePrinter();
        }
        return ctPrinter;
    }
}
