package com.beetech.python.ar.injection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.beetech.python.ar.base.network.ApiInterface;
import com.beetech.python.ar.base.view.ViewController;
import com.beetech.python.ar.interactor.HomeInteractor;
import com.beetech.python.ar.interactor.impl.HomeInteractorImpl;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.presenter.HomePresenter;
import com.beetech.python.ar.presenter.impl.HomePresenterImpl;
import com.beetech.python.ar.util.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;

@Module
public final class HomeViewModule {
    private AppCompatActivity activity;
    private int layoutId;

    public HomeViewModule(AppCompatActivity activity, int layoutId) {
        this.activity = activity;
        this.layoutId = layoutId;
    }
    @Provides
    public HomeInteractor provideInteractor(ApiInterface apiInterface, RxSchedulers rxSchedulers) {
        return new HomeInteractorImpl(apiInterface, rxSchedulers);
    }

    @Provides
    public PresenterFactory<HomePresenter> providePresenterFactory(@NonNull final HomeInteractor interactor) {
        return new PresenterFactory<HomePresenter>() {
            @NonNull
            @Override
            public HomePresenter create() {
                return new HomePresenterImpl(interactor);
            }
        };
    }

    @Provides
    public ViewController provideViewController() {
        return new ViewController(activity.getSupportFragmentManager(), layoutId);
    }
}
