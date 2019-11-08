package com.beetech.python.ar.injection;

import android.content.Context;

import androidx.annotation.NonNull;

import com.beetech.python.ar.App;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    @NonNull
    private final App mApp;

    public AppModule(@NonNull App app) {
        mApp = app;
    }

    @Provides
    @ApplicationContext
    public Context provideAppContext() {
        return mApp;
    }

    @Provides
    public App provideApp() {
        return mApp;
    }
}
