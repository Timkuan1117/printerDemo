package com.cybersoft.printerDemo.base;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;

public abstract class BaseActivity <VM extends BaseViewModel, DB extends ViewDataBinding> extends AppCompatActivity {

    protected VM viewModel;
    protected DB binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, initContentView());
        viewModel = initViewModel();

        binding.setLifecycleOwner(this);
        binding.setVariable(BR.viewModel, viewModel);
        initViewObservable();

    }




    protected abstract int initContentView();
    protected abstract VM initViewModel();
    protected abstract void initViewObservable();

}
