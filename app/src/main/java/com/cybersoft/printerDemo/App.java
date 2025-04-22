package com.cybersoft.printerDemo;

import android.app.Application;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import com.cybersoft.printerDemo.hardware.HardwareManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;

import dalvik.system.DexClassLoader;

public class App extends Application {
    public static Gson gson;
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            gson =new Gson();
            HardwareManager.getInstance();
            HardwareManager.bindService(getApplicationContext());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
