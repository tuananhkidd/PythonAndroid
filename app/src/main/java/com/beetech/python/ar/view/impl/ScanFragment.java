package com.beetech.python.ar.view.impl;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
//
        Bitmap bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888);
        Allocation bmData = renderScriptNV21ToRGBA8888(
                getActivity(), size.width, size.height, data);
        bmData.copyTo(bitmap);

//        try {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        encoded = encoded.replace("\n", "");

        Python py = Python.getInstance();
        PyObject console = py.getModule("color_barcode_scanner");
        PyObject pyObject = console.callAttr("scan", encoded);
        if (pyObject != null) {
            String value = pyObject.toString();
            Log.v("ahuhu", "def : " + value);
        }

//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    public Allocation renderScriptNV21ToRGBA8888(Context context, int width, int height, byte[] nv21) {
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicYuvToRGB yuvToRgbIntrinsic = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs));

        Type.Builder yuvType = new Type.Builder(rs, Element.U8(rs)).setX(nv21.length);
        Allocation in = Allocation.createTyped(rs, yuvType.create(), Allocation.USAGE_SCRIPT);

        Type.Builder rgbaType = new Type.Builder(rs, Element.RGBA_8888(rs)).setX(width).setY(height);
        Allocation out = Allocation.createTyped(rs, rgbaType.create(), Allocation.USAGE_SCRIPT);

        in.copyFrom(nv21);

        yuvToRgbIntrinsic.setInput(in);
        yuvToRgbIntrinsic.forEach(out);
        return out;
    }

    private void processData(byte[] data) {
        Observable.fromCallable(() -> {
            Python py = Python.getInstance();
            PyObject console = py.getModule("scan");
            PyObject pyObject = console.callAttr("scan", 123);
            String value = pyObject.toString();

            return value;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    Log.v("ahuhu", "value : " + result);
                }, throwable -> {

                });
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
