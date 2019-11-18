package com.beetech.python.ar.injection;

import com.beetech.python.ar.view.impl.ResultScanFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = ResultScanViewModule.class)
public interface ResultScanViewComponent {
    void inject(ResultScanFragment fragment);
}