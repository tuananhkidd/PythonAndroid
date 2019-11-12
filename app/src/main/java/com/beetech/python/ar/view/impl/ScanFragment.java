package com.beetech.python.ar.view.impl;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.beetech.python.ar.R;
import com.beetech.python.ar.custom.CameraPreview;
import com.beetech.python.ar.injection.AppComponent;
import com.beetech.python.ar.injection.DaggerScanViewComponent;
import com.beetech.python.ar.injection.ScanViewModule;
import com.beetech.python.ar.presenter.ScanPresenter;
import com.beetech.python.ar.presenter.loader.PresenterFactory;
import com.beetech.python.ar.view.ScanView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import javax.inject.Inject;

import butterknife.BindView;

public final class ScanFragment extends BaseFragment<ScanPresenter, ScanView> implements ScanView, Camera.PreviewCallback {
    private CameraPreview mPreview;
    private Camera mCamera;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;

    @BindView(R.id.fr_parent)
    RelativeLayout frParent;
    @BindView(R.id.view)
    View view;

    @Inject
    PresenterFactory<ScanPresenter> mPresenterFactory;

    public ScanFragment() {
    }


    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerScanViewComponent.builder()
                .appComponent(parentComponent)
                .scanViewModule(new ScanViewModule())
                .build()
                .inject(this);
    }

    @Override
    public void initData() {
        super.initData();
        if (!isCameraAvailable()) {
            return;
        }
        mAutoFocusHandler = new Handler();
        mPreview = new CameraPreview(getContext(), this, autoFocusCB);

        mPreview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        frParent.addView(mPreview);



    }

    public boolean isCameraAvailable() {
        PackageManager pm = getActivity().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void cancelRequest() {
//        Intent dataIntent = new Intent();
//        dataIntent.putExtra(ERROR_INFO, "Camera unavailable");
//        setResult(Activity.RESULT_CANCELED, dataIntent);
//        finish();
    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_scan;
    }

    @NonNull
    @Override
    protected PresenterFactory<ScanPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();

        Log.v("kiki","data "+data);

        Python py = Python.getInstance();
        PyObject console = py.getModule("color_barcode_scanner");
        PyObject pyObject = console.callAttr("scan","");
        String value = pyObject.toString();
        Log.v("ahuhu","def : "+value);
    }

    private void stopCameraPreview() {
        if (mCamera != null) {
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
        }
        mPreviewing = false;
    }

    private void initCamera() {
        try {
            // Open the default i.e. the first rear facing camera.
            mCamera = Camera.open();
            if (mCamera == null) {
                cancelRequest();
                return;
            }
            mPreview.setCamera(mCamera);
            mPreview.showSurfaceView();
            mPreviewing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void destroyCamera() {
        try {
            if (mCamera != null) {
                mPreview.setCamera(null);
                mCamera.cancelAutoFocus();
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mPreview.hideSurfaceView();
                mPreviewing = false;
                mCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null && mPreviewing) {
                mCamera.autoFocus(autoFocusCB);
            }
        }
    };

    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


    public void onPause() {
        super.onPause();
        destroyCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        initCamera();
    }

    private void createBlinkView() {
        ObjectAnimator anim = ObjectAnimator.ofInt(view, "backgroundColor", Color.WHITE, Color.RED,
                Color.TRANSPARENT);
        anim.setDuration(500);
        anim.setEvaluator(new ArgbEvaluator());
//        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        anim.start();
    }
}
