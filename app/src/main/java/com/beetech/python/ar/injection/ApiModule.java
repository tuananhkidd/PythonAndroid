package com.beetech.python.ar.injection;

import android.content.Context;

import com.beetech.python.ar.base.network.ApiInterface;
import com.beetech.python.ar.base.network.NetworkCheckerInterceptor;
import com.beetech.python.ar.util.Define;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Provides
    @Singleton
    ApiInterface provideApiInterface(OkHttpClient client, RxJava2CallAdapterFactory rxAdapter) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                //fixme change url
                .baseUrl("http://handycart.dovanbao.com/api/v1/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .build();

        return retrofit.create(ApiInterface.class);
    }

    @Provides
    OkHttpClient provideHttpClient(@ApplicationContext Context context) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        NetworkCheckerInterceptor networkCheckerInterceptor = new NetworkCheckerInterceptor(context);

        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(loggingInterceptor)
                .addInterceptor(networkCheckerInterceptor)
                .connectTimeout(Define.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Define.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
//                .cache(cache)
                .build();
    }

    /*@Provides
    Cache provideCache(File file) {
        return new Cache(file, 10 * 10 * 1000);
    }*/

    /*@Provides
    File provideCacheFile(@ApplicationContext Context context) {
        return context.getFilesDir();
    }*/

    @Provides
    RxJava2CallAdapterFactory provideRxAdapter() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    GsonConverterFactory provideGsonClient() {
        return GsonConverterFactory.create();
    }
}
