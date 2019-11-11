package com.beetech.python.ar.injection;

import androidx.annotation.NonNull;

import com.beetech.python.ar.interactor.ScanInteractor;
import com.beetech.python.ar.interactor.impl.ScanInteractorImpl;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.presenter.ScanPresenter;
import com.beetech.python.ar.presenter.impl.ScanPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ScanViewModule {
    @Provides
    public ScanInteractor provideInteractor() {
        return new ScanInteractorImpl();
    }

    @Provides
    public PresenterFactory<ScanPresenter> providePresenterFactory(@NonNull final ScanInteractor interactor) {
        return new PresenterFactory<ScanPresenter>() {
            @NonNull
            @Override
            public ScanPresenter create() {
                return new ScanPresenterImpl(interactor);
            }
        };
    }
}
