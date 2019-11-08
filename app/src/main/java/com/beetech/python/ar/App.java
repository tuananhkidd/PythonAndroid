package com.beetech.python.ar;

import android.app.Application;

import androidx.annotation.NonNull;

import com.beetech.python.ar.injection.AppComponent;
import com.beetech.python.ar.injection.AppModule;
import com.beetech.python.ar.injection.DaggerAppComponent;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public final class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        Python.start(new AndroidPlatform(this));

    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}