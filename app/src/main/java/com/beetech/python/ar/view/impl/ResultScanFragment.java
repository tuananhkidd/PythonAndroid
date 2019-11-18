package com.beetech.python.ar.view.impl;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.beetech.python.ar.R;
import com.beetech.python.ar.injection.AppComponent;
import com.beetech.python.ar.injection.DaggerResultScanViewComponent;
import com.beetech.python.ar.injection.ResultScanViewModule;
import com.beetech.python.ar.presenter.ResultScanPresenter;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.view.ResultScanView;

import javax.inject.Inject;

import butterknife.BindView;

public final class ResultScanFragment extends BaseFragment<ResultScanPresenter, ResultScanView> implements ResultScanView {
    @Inject
    PresenterFactory<ResultScanPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @BindView(R.id.tv_result)
    TextView tvResult;

    public ResultScanFragment() {
        // Required empty public constructor
    }


    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerResultScanViewComponent.builder()
                .appComponent(parentComponent)
                .resultScanViewModule(new ResultScanViewModule())
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            tvResult.setText(bundle.getString("scan"));
        }
    }

    @Override
    public boolean backPressed() {
        getViewController().backFromAddFragment(null);
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_result_scan;
    }

    @NonNull
    @Override
    protected PresenterFactory<ResultScanPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
