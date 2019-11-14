package com.beetech.python.ar.view.impl;

import android.content.pm.PackageManager;
import android.graphics.Point;

import androidx.annotation.NonNull;

import com.beetech.python.ar.R;
import com.beetech.python.ar.base.view.ViewController;
import com.beetech.python.ar.injection.AppComponent;
import com.beetech.python.ar.injection.DaggerHomeViewComponent;
import com.beetech.python.ar.injection.HomeViewModule;
import com.beetech.python.ar.presenter.HomePresenter;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.view.HomeView;

import javax.inject.Inject;

public final class HomeActivity extends BaseActivity<HomePresenter, HomeView> implements HomeView {
    @Inject
    PresenterFactory<HomePresenter> mPresenterFactory;
    @Inject
    ViewController mViewController;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    public static Point screenParametersPoint = new Point();

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
//        ActivityCompat.requestPermissions(this, new
//                String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        getWindowManager().getDefaultDisplay().getSize(screenParametersPoint);
    }

    @Override
    public void initData() {
        getViewController().addFragment(ScanColorBarCodeFragment.class,null);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_home;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                    getViewController().addFragment(ScanFragment.class,null);
                } else {
//                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


}
