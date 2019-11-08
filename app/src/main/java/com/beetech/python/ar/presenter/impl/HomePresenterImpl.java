package com.beetech.python.ar.presenter.impl;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.beetech.python.ar.presenter.HomePresenter;
import com.beetech.python.ar.view.HomeView;
import com.beetech.python.ar.interactor.HomeInteractor;

import javax.inject.Inject;

public final class HomePresenterImpl extends BasePresenterImpl<HomeView> implements HomePresenter {
    /**
     * The interactor
     */
    @NonNull
    private final HomeInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public HomePresenterImpl(@NonNull HomeInteractor interactor) {
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

    @Override
    public void getData() {
        mInteractor.getData().doOnSubscribe(disposable -> {

        })
                .doFinally(()->{

                })
                .subscribe(
                        baseResponse ->{
                            Log.v("ahuhu","hihi");
                        },
                        throwable -> {
                            throwable.printStackTrace();
                        }
                );
    }
}