package com.beetech.python.ar.injection;

import com.beetech.python.ar.view.impl.ScanFragment;

import dagger.Component;

@FragmentScope
@Component(dependencies = AppComponent.class, modules = ScanViewModule.class)
public interface ScanViewComponent {
    void inject(ScanFragment fragment);
}