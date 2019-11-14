package com.beetech.python.ar.injection;

import androidx.annotation.NonNull;

import com.beetech.python.ar.interactor.ScanColorBarCodeInteractor;
import com.beetech.python.ar.interactor.impl.ScanColorBarCodeInteractorImpl;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.presenter.ScanColorBarCodePresenter;
import com.beetech.python.ar.presenter.impl.ScanColorBarCodePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ScanColorBarCodeViewModule {
    @Provides
    public ScanColorBarCodeInteractor provideInteractor() {
        return new ScanColorBarCodeInteractorImpl();
    }

    @Provides
    public PresenterFactory<ScanColorBarCodePresenter> providePresenterFactory(@NonNull final ScanColorBarCodeInteractor interactor) {
        return new PresenterFactory<ScanColorBarCodePresenter>() {
            @NonNull
            @Override
            public ScanColorBarCodePresenter create() {
                return new ScanColorBarCodePresenterImpl(interactor);
            }
        };
    }
}
