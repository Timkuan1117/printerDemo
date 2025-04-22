package com.cybersoft.printerDemo.base;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class BaseViewModel extends AndroidViewModel {
    public SingleLiveEvent<String> toastEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> finishEvent = new SingleLiveEvent<>();
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }


}