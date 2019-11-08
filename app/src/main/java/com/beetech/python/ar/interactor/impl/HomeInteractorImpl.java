package com.beetech.python.ar.interactor.impl;

import javax.inject.Inject;

import com.beetech.python.ar.base.network.ApiInterface;
import com.beetech.python.ar.base.network.BaseResponse;
import com.beetech.python.ar.interactor.HomeInteractor;
import com.beetech.python.ar.util.rx.RxSchedulers;

import io.reactivex.Single;

public final class HomeInteractorImpl implements HomeInteractor {
    private ApiInterface apiInterface;
    private RxSchedulers rxSchedulers;
    @Inject
    public HomeInteractorImpl(ApiInterface apiInterface, RxSchedulers rxSchedulers) {
        this.apiInterface = apiInterface;
        this.rxSchedulers = rxSchedulers;
    }

    @Override
    public Single<BaseResponse> getData() {
        return apiInterface.getData()
                .observeOn(rxSchedulers.androidThread())
                .subscribeOn(rxSchedulers.compute());
    }
}