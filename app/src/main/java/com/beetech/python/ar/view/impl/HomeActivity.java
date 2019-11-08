package com.beetech.python.ar.view.impl;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.beetech.python.ar.R;
import com.beetech.python.ar.base.view.ViewController;
import com.beetech.python.ar.view.HomeView;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.presenter.HomePresenter;
import com.beetech.python.ar.injection.AppComponent;
import com.beetech.python.ar.injection.HomeViewModule;
import com.beetech.python.ar.injection.DaggerHomeViewComponent;

import javax.inject.Inject;

public final class HomeActivity extends BaseActivity<HomePresenter, HomeView> implements HomeView {
    @Inject
    PresenterFactory<HomePresenter> mPresenterFactory;
    ViewController mViewController;

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerHomeViewComponent.builder()
                .appComponent(parentComponent)
                .homeViewModule(new HomeViewModule(this,getRootViewId()))
                .build()
                .inject(this);
    }

    @Override
    protected int getRootViewId() {
        return R.id.rlMain;
    }

    @Override
    public ViewController getViewController() {
        return mViewController;
    }

    @NonNull
    @Override
    protected PresenterFactory<HomePresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void initView() {
        mPresenter.getData();
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

}
