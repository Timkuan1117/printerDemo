package com.cybersoft.printerDemo.hardware;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.cybersoft.printerDemo.hardware.hardwareInterface.cHelper;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cPrinter;
import com.cybersoft.printerDemo.hardware.hardware_castle.CastleHelper;
import com.cybersoft.printerDemo.hardware.hardware_dummy.DummyHelper;
import com.cybersoft.printerDemo.hardware.hardware_pax.PaxHelper;

public class HardwareManager {

    private static final String TAG = HardwareManager.class.getSimpleName();
    private static HardwareManager instance;

    private static cHelper helper;

    private static String device;

    private HardwareManager() {
        device = Build.MODEL;
    }

    public static HardwareManager getInstance() {
        if (instance == null) {
            synchronized (HardwareManager.class) {
                if (instance == null) {
                    instance = new HardwareManager();
                    InitDevice();
                }
            }
        }
        return instance;
    }
    private static void InitDevice() {
        Log.d(TAG, "Devices實例化:" + device);
        switch (device) {
            case "A910S":
                helper = new PaxHelper();
                break;
            case "S1F3":
                helper = new CastleHelper();
                break;
            default:
                helper = new DummyHelper();
        }
    }

    public static void bindService(Context context) throws Exception {
        Log.d(TAG, "Devices 服務綁定");
        if (helper == null) {
            InitDevice();
        }
        helper.bindService(context);
    }



    public cHelper getHelp() {
        return helper;
    }

    public cPrinter getPrinter() {
        if (helper == null) {
            return null;
        }
        return helper.cPrinter();
    }
}