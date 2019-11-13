package com.beetech.python.ar.base.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.beetech.python.ar.injection.ApplicationContext;
import com.beetech.python.ar.util.Define;

import javax.inject.Inject;

public class RxPreferencesImpl implements RxPreferences {

    private final SharedPreferences mPrefs;

    @Inject
    public RxPreferencesImpl(@ApplicationContext Context context) {
        mPrefs = context.getSharedPreferences(Define.PREF_FILE_NAME, Context.MODE_PRIVATE);
    }


}
