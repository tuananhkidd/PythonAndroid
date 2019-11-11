package com.beetech.python.ar.presenter.impl;

import androidx.annotation.NonNull;

import com.beetech.python.ar.presenter.ScanPresenter;
import com.beetech.python.ar.view.ScanView;
import com.beetech.python.ar.interactor.ScanInteractor;

import javax.inject.Inject;

public final class ScanPresenterImpl extends BasePresenterImpl<ScanView> implements ScanPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final ScanInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ScanPresenterImpl(@NonNull ScanInteractor interactor) {
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