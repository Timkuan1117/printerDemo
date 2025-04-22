package com.cybersoft.printerDemo;

import android.app.Application;
import com.cybersoft.printerDemo.hardware.HardwareManager;
import com.google.gson.Gson;

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
