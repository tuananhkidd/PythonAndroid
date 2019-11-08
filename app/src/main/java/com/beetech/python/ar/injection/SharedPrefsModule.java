package com.beetech.python.ar.injection;

import com.beetech.python.ar.base.pref.RxPreferences;
import com.beetech.python.ar.base.pref.RxPreferencesImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPrefsModule {

    @Provides
    @Singleton
    RxPreferences provideRxPreference(RxPreferencesImpl tetVietPreferences) {
        return tetVietPreferences;
    }
}
