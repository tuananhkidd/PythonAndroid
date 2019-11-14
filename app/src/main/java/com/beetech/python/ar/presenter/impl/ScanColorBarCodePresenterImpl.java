package com.beetech.python.ar.presenter.impl;

import androidx.annotation.NonNull;

import com.beetech.python.ar.presenter.ScanColorBarCodePresenter;
import com.beetech.python.ar.view.ScanColorBarCodeView;
import com.beetech.python.ar.interactor.ScanColorBarCodeInteractor;

import javax.inject.Inject;

public final class ScanColorBarCodePresenterImpl extends BasePresenterImpl<ScanColorBarCodeView> implements ScanColorBarCodePresenter {
    /**
     * The interactor
     */
    @NonNull
    private final ScanColorBarCodeInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ScanColorBarCodePresenterImpl(@NonNull ScanColorBarCodeInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }
}