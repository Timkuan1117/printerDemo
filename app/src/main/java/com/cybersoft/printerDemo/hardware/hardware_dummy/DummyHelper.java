package com.cybersoft.printerDemo.hardware.hardware_dummy;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cHelper;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cPrinter;


public class DummyHelper implements cHelper {
    private static final String TAG = DummyHelper.class.getSimpleName();
    @Override
    public void init() {
        Log.d(TAG , "不認識的裝置:"+ Build.MODEL);

    }

    @Override
    public void bindService(Context context) throws Exception {
        Log.d(TAG , "不認識的裝置跳過綁定");
    }

    @Override
    public void light(boolean light) {

    }

    @Override
    public cPrinter cPrinter() {
        return null;
    }


}
