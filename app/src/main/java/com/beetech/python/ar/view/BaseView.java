package com.beetech.python.ar.view;

import androidx.annotation.StringRes;

public interface BaseView {
    void initView();

    void initData();

    void showErrorDialog(String title, String message);

    void showErrorDialog(String message);

    void showErrorDialog(@StringRes int messageRes);

    void showLoading();

    void hiddenLoading();

    void showLayoutRetry();

    void hideLayoutRetry();
}
