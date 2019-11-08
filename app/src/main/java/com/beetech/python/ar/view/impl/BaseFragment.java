package com.beetech.python.ar.view.impl;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;


import com.beetech.python.ar.App;
import com.beetech.python.ar.base.view.ViewController;
import com.beetech.python.ar.injection.AppComponent;
import com.beetech.python.ar.presenter.BasePresenter;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.presenter.loader.PresenterLoader;
import com.beetech.python.ar.view.BaseView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<P extends BasePresenter<V>, V extends BaseView> extends Fragment implements LoaderManager.LoaderCallbacks<P>, BaseView {
    public static final long CLICK_TIME_INTERVAL = 300L;

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
     * Is this the first start of the fragment (after onCreate)
     */
    private boolean mFirstStart;

    public static long lastClickTime = System.currentTimeMillis();
    /**
     * The ViewController for control fragments in an activity
     */
    @Nullable
    private ViewController mViewController;
    private Unbinder mBinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirstStart = true;

        injectDependencies();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResId(), container, false);
        mBinder = ButterKnife.bind(this, rootView);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this).startLoading();
    }

    private void injectDependencies() {
        setupComponent(((App) getActivity().getApplication()).getAppComponent());
    }

    @Override
    public void onStart() {
        super.onStart();
        lastClickTime = System.currentTimeMillis();
        if (mPresenter == null) {
            mNeedToCallStart.set(true);
        } else {
            doStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

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
    public void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();

            mPresenter.onViewDetached();
        }

        super.onStop();
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(getActivity(), getPresenterFactory());
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

    public void setViewController(ViewController viewController) {
        this.mViewController = viewController;
    }

    //region => TetViet base area

    public void setData(HashMap<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof String) {
                bundle.putString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                bundle.putDouble(entry.getKey(), ((Double) entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                bundle.putInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Float) {
                bundle.putFloat(entry.getKey(), ((Float) entry.getValue()));
            } else if (entry.getValue() instanceof Boolean) {
                bundle.putBoolean(entry.getKey(), ((Boolean) entry.getValue()));
            } else if (entry.getValue() instanceof Parcelable) {
                bundle.putParcelable(entry.getKey(), ((Parcelable) entry.getValue()));
            } else if (entry.getValue() instanceof String[]) {
                bundle.putStringArray(entry.getKey(), (String[]) entry.getValue());
            } else if (entry.getValue() instanceof ArrayList) {
                if (((ArrayList) entry.getValue()).size() > 0 && ((ArrayList) entry.getValue()).get(0) instanceof String) {
                    bundle.putStringArrayList(entry.getKey(), (ArrayList<String>) entry.getValue());
                } else if (((ArrayList) entry.getValue()).size() > 0 && ((ArrayList) entry.getValue()).get(0) instanceof Parcelable) {
                    bundle.putParcelableArrayList(entry.getKey(), (ArrayList<? extends Parcelable>) entry.getValue());
                }
            }
        }
        setArguments(bundle);
    }

    public ViewController getViewController() {
        if (mViewController == null) {
            return ((BaseActivity) getActivity()).getViewController();
        } else {
            return mViewController;
        }
    }

    /**
     * This func has call when pressed back button on device.
     *
     * @return True if need destroy Activity
     */
    public abstract boolean backPressed();

    /**
     * Avoid duplicate click listener at the same Time
     *
     * @return True if occurred duplicate click, else other wise
     */
    protected boolean avoidDuplicateClick() {
        long now = System.currentTimeMillis();
        if (now - lastClickTime < CLICK_TIME_INTERVAL) {
            return true;
        }
        lastClickTime = now;
        return false;
    }

    public void backFromAddFragment() {

    }


    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void showErrorDialog(String message) {

    }

//    public void showDialog(String message, boolean isCancel, BaseDialog.OnDialogListener listener) {
//
//    }
//
//    public void showDialogConfirm(String title, String message, boolean isCancel, BaseDialog.OnDialogListener listener, BaseDialog.OnDialogListener cancelListener) {
//
//    }

    @Override
    public void showErrorDialog(String title, String message) {

    }

    @Override
    public void showErrorDialog(int messageRes) {

    }

    @Override
    public void showLoading() {
//        LoadingDialog.getInstance(getContext()).show();
    }

    @Override
    public void hiddenLoading() {
//        LoadingDialog.getInstance(getContext()).hidden();
    }

    //endregion
    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    public void onDestroy() {
//        LoadingDialog.getInstance(getContext()).destroyLoadingDialog();
        super.onDestroy();
        if (mBinder != null)
            mBinder.unbind();
    }

    protected String getTrackingScreenName() {
        return null;
    }

//    public void trackEvent(String event) {
//        if (!TextUtils.isEmpty(event))
//            mAnalyticsUtils.trackEvent(event);
//    }


    @Override
    public void showLayoutRetry() {
//        if (getActivity() != null && getActivity() instanceof MainActivity) {
//            ((MainActivity) getActivity()).showLayoutRetry();
//        }
    }

    @Override
    public void hideLayoutRetry() {
//        if (getActivity() != null && getActivity() instanceof MainActivity) {
//            ((MainActivity) getActivity()).hideLayoutRetry();
//        }
    }

    abstract void onRetry();

    abstract void onRefreshData();
}