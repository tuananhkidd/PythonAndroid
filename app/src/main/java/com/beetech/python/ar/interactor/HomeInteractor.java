package com.beetech.python.ar.interactor;

import com.beetech.python.ar.base.network.BaseResponse;

import io.reactivex.Single;

public interface HomeInteractor extends BaseInteractor {
    Single<BaseResponse> getData();
}