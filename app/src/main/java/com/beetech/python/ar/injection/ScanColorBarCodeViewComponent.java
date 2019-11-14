package com.beetech.python.ar.injection;

import com.beetech.python.ar.view.impl.ScanColorBarCodeFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = ScanColorBarCodeViewModule.class)
public interface ScanColorBarCodeViewComponent {
    void inject(ScanColorBarCodeFragment fragment);
}