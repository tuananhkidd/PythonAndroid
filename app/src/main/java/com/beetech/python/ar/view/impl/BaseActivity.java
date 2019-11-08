package com.beetech.python.ar.view.impl;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.beetech.python.ar.App;
import com.beetech.python.ar.base.view.ViewController;
import com.beetech.python.ar.injection.AppComponent;
import com.beetech.python.ar.presenter.BasePresenter;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.presenter.loader.PresenterLoader;
import com.beetech.python.ar.view.BaseView;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

import static com.beetech.python.ar.view.impl.BaseFragment.CLICK_TIME_INTERVAL;

public abstract class BaseActivity<P extends BasePresenter<V>, V extends BaseView> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<P>, BaseView {
    /**
     * Do we need to call {@link #doStart()} from the {@link #onLoadFinished(Loader, BasePresenter)} method.
     * Will be true if presenter wasn't loaded when {@link #onStart()} is reached
     */
    private final AtomicBoolean mNeedToCallStart = new AtomicBoolean(false);
    /**
     * The presenter for this view
     */
    @Nullable
    protected P mPresenter;
    /**
     * Is this the first start of the activity (after onCreate)
     */
    private boolean mFirstStart;

    private CompositeDisposable mCompositeSubscription;

    private Unbinder mBinder;

    public static String c = "54874434";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mBinder = ButterKnife.bind(this);
        mFirstStart = true;

        injectDependencies();

        getSupportLoaderManager().initLoader(0, null, this).startLoading();
    }

    private void injectDependencies() {
        setupComponent(((App) getApplication()).getAppComponent());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mPresenter == null) {
            mNeedToCallStart.set(true);
        } else {
            doStart();
        }
    }

    /**
     * Call the presenter callbacks for onStart
     */
    @SuppressWarnings("unchecked")
    private void doStart() {
        assert mPresenter != null;

        mPresenter.onViewAttached((V) this);

        mPresenter.onStart(mFirstStart);

        mFirstStart = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompositeSubscription = new CompositeDisposable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCompositeSubscription.dispose();
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();

            mPresenter.onViewDetached();
        }

        super.onStop();
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, getPresenterFactory());
    }

    @Override
    public final void onLoadFinished(Loader<P> loader, P presenter) {
        mPresenter = presenter;

        if (mNeedToCallStart.compareAndSet(true, false)) {
            doStart();
        }
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }

    /**
     * Get the presenter factory implementation for this view
     *
     * @return the presenter factory
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Setup the injection component for this view
     *
     * @param appComponent the app component
     */
    protected abstract void setupComponent(@NonNull AppComponent appComponent);


    protected abstract int getRootViewId();

    public abstract ViewController getViewController();

    @Override
    public void onBackPressed() {
        if (getViewController() != null && getViewController().getCurrentFragment() != null) {
            if (getViewController().getCurrentFragment().backPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void showErrorDialog(String message) {
//        new BaseDialog(this)
//                .setMessage(message)
//                .setPositiveButton(R.string.ok, null)
//                .show();
    }

    @Override
    public void showLoading() {
//        LoadingDialog.getInstance(this).show();
    }

    @Override
    public void hiddenLoading() {
//        LoadingDialog.getInstance(this).hidden();
    }

    @Override
    public void showErrorDialog(String title, String message) {
//        new BaseDialog(this)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(R.string.ok, null)
//                .show();
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    protected void onDestroy() {
//        LoadingDialog.getInstance(this).destroyLoadingDialog();
        super.onDestroy();
        if (mBinder != null)
            mBinder.unbind();
    }

    @Override
    public void showErrorDialog(int messageRes) {
//        new BaseDialog(this)
//                .setMessage(messageRes)
//                .setPositiveButton(R.string.ok, null)
//                .show();
    }


    /**
     * Avoid duplicate click listener at the same Time
     *
     * @return True if occurred duplicate click, else other wise
     */
    protected boolean avoidDuplicateClick() {
        long now = System.currentTimeMillis();
        if (now - BaseFragment.lastClickTime < CLICK_TIME_INTERVAL) {
            return true;
        }
        BaseFragment.lastClickTime = now;
        return false;
    }

    @Override
    public void showLayoutRetry() {

    }

    @Override
    public void hideLayoutRetry() {

    }
}