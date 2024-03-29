package com.beetech.python.ar.presenter.loader;

import androidx.annotation.NonNull;

import com.beetech.python.ar.presenter.BasePresenter;

/**
 * Factory to implement to create a presenter
 */
public interface PresenterFactory<T extends BasePresenter> {
    @NonNull
    T create();
}
