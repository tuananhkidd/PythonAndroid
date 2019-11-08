package com.beetech.python.ar.base.network;

import android.content.Context;

import com.beetech.python.ar.R;
import com.beetech.python.ar.util.DeviceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class NetworkCheckerInterceptor implements Interceptor {

    private Context context;

    public NetworkCheckerInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (DeviceUtil.hasNetworkConnection(context)) {
            return chain.proceed(chain.request());
        } else {
            throw new NoConnectivityException();
        }
    }

    public class NoConnectivityException extends IOException {
        @Override
        public String getMessage() {
            return context.getResources().getString(R.string.no_internet_connection);
        }
    }
}
