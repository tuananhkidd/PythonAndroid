package com.beetech.python.ar.injection;

import android.content.Context;

import com.beetech.python.ar.App;
import com.beetech.python.ar.base.network.ApiInterface;
import com.beetech.python.ar.base.pref.RxPreferences;
import com.beetech.python.ar.util.rx.RxModule;
import com.beetech.python.ar.util.rx.RxSchedulers;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RxModule.class,SharedPrefsModule.class,ApiModule.class})
public interface AppComponent {
    App getApp();

    @ApplicationContext
    Context getAppContext();

    RxSchedulers rxSchedulers();

    RxPreferences rxPreferences();

    ApiInterface apiInterface();

}