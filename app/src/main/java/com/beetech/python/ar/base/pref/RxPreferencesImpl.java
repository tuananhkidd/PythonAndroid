package com.beetech.python.ar.base.pref;

import android.content.Context;
import android.content.SharedPreferences;

import com.beetech.python.ar.injection.ApplicationContext;
import com.beetech.python.ar.util.Define;

import javax.inject.Inject;

public class RxPreferencesImpl implements RxPreferences {

    private static final String PREF_KEY_FCM_TOKEN = "PREF_KEY_FCM_TOKEN";
    private static final String PREF_KEY_FIRST_TIME_USE_APP = "PREF_KEY_FIRST_TIME_USE_APP";
    private static final String PREF_KEY_IS_USER_FCM_TOKEN_REGISTERED = "PREF_KEY_IS_USER_FCM_TOKEN_REGISTERED";
    private static final String PREF_KEY_LAST_LOCATION_LATITUDE = "PREF_KEY_LAST_LOCATION_LATITUDE";
    private static final String PREF_KEY_LAST_LOCATION_LONGITUDE = "PREF_KEY_LAST_LOCATION_LONGITUDE";
    private static final String PREF_KEY_CLIENT_ID_CHAT_BOT = "PREF_KEY_CLIENT_ID_CHAT_BOT";
    private static final String PREF_KEY_IS_UPDATE_INFO_CHAT_BOT = "PREF_KEY_IS_UPDATE_INFO_CHAT_BOT";
    private static final String PREF_KEY_IS_SUBSCRIBE_TOPIC_SUCCEED = "PREF_KEY_IS_SUBSCRIBE_TOPIC_SUCCEED";
    private static final String PREF_KEY_LAST_TIME_PLAY_DAILY_MESSAGE= "PREF_KEY_LAST_TIME_PLAY_DAILY_MESSAGE";
    private static final String PREF_KEY_FIRST_TIME_UPDATE_LOCATION = "PREF_KEY_FIRST_TIME_UPDATE_LOCATION";
    private static final String PREF_KEY_AUDIO_SPLASH = "PREF_KEY_AUDIO_SPLASH";

    private final SharedPreferences mPrefs;

    @Inject
    public RxPreferencesImpl(@ApplicationContext Context context) {
        mPrefs = context.getSharedPreferences(Define.PREF_FILE_NAME, Context.MODE_PRIVATE);
    }


}
