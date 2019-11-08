package com.beetech.python.ar.util.rx;


import dagger.Module;
import dagger.Provides;

@Module
public class RxModule {
    @Provides
    RxSchedulers provideRxSchedulers() {
        return new AppRxSchedulers();
    }
}
