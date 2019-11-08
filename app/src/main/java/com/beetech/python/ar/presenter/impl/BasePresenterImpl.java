package com.beetech.python.ar.presenter.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.beetech.python.ar.presenter.BasePresenter;
import com.beetech.python.ar.view.BaseView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Abstract presenter implementation that contains base implementation for other presenters.
 * Subclasses must call super for all {@link BasePresenter} method overriding.
 */
public abstract class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {
    /**
     * The view
     */
    @Nullable
    protected V mView;
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onViewAttached(@NonNull V view) {
        mView = view;
    }


    @Override
    public void onStart(boolean viewCreated) {
        if (viewCreated && mView != null) {
            mView.initView();
            mView.initData();
        }
    }

    @Override
    public void onStop() {

    }


    @Override
    public void onViewDetached() {
        mView = null;
    }

    @Override
    public void onPresenterDestroyed() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
