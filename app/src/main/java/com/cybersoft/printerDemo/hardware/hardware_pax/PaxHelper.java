package com.cybersoft.printerDemo.hardware.hardware_pax;

import android.content.Context;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cHelper;
import com.cybersoft.printerDemo.hardware.hardwareInterface.cPrinter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
public class PaxHelper implements cHelper {

    private Context context;

    private cPrinter cPrinter;
    private int retry ;
    public static IDAL mDal;

    @Override
    public void init() {
        this.context = context;
        this.retry = 0;
    }

    @Override
    public void bindService(Context context) throws Exception {
        mDal = NeptuneLiteUser.getInstance().getDal(context);
    }


    @Override
    public void light(boolean light) {

    }

    @Override
    public cPrinter cPrinter() {
        if (cPrinter == null) {
            cPrinter = new PaxPrinter(mDal.getPrinter());
        }
        return cPrinter;
    }



}


