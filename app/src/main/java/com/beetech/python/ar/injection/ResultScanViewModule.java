package com.beetech.python.ar.injection;

import androidx.annotation.NonNull;

import com.beetech.python.ar.interactor.ResultScanInteractor;
import com.beetech.python.ar.interactor.impl.ResultScanInteractorImpl;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.presenter.ResultScanPresenter;
import com.beetech.python.ar.presenter.impl.ResultScanPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ResultScanViewModule {
    @Provides
    public ResultScanInteractor provideInteractor() {
        return new ResultScanInteractorImpl();
    }

    @Provides
    public PresenterFactory<ResultScanPresenter> providePresenterFactory(@NonNull final ResultScanInteractor interactor) {
        return new PresenterFactory<ResultScanPresenter>() {
            @NonNull
            @Override
            public ResultScanPresenter create() {
                return new ResultScanPresenterImpl(interactor);
            }
        };
    }
}
