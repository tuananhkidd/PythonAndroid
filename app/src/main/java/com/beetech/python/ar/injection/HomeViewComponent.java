package com.beetech.python.ar.injection;

import com.beetech.python.ar.view.impl.HomeActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = HomeViewModule.class)
public interface HomeViewComponent {
    void inject(HomeActivity activity);
}