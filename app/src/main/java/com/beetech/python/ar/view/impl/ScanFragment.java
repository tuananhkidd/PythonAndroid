package com.beetech.python.ar.view.impl;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void initData() {
        super.initData();
        if (!isCameraAvailable()) {
            return;
        }
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mAutoFocusHandler = new Handler();
        mPreview = new CameraPreview(getContext(), this, autoFocusCB);

        mPreview.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        frParent.addView(mPreview);

//        String encoded = FileUtil.read(getContext());
//        Log.v("ahuhu", "object : " + encoded);


//        new Handler().postDelayed(()->{
//
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.demo);
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            encoded = encoded.replace("\n", "");
//
//            Python py = Python.getInstance();
//            PyObject console = py.getModule("color_barcode_scanner");
//            PyObject pyObject = console.callAttr("scan", encoded,602,399);
//            Log.v("ahuhu", "object : " + pyObject);
//
//            Toast.makeText(getContext(), "object : " + pyObject, Toast.LENGTH_SHORT).show();
//            String value = "";
//            if (pyObject != null) {
//                value = pyObject.toString();
//                Toast.makeText(getContext(), "def : " + value, Toast.LENGTH_SHORT).show();
//                Log.v("ahuhu", "def : " + value);
//            }
//        },5000);


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
            camera.setDisplayOrientation(90);
            Camera.Parameters parameters = camera.getParameters();
            parameters.setRotation(90);
            camera.setParameters(parameters);
            Camera.Size size = parameters.getPreviewSize();

//        Bitmap bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888);
//        Allocation bmData = renderScriptNV21ToRGBA8888(
//                getActivity(), size.width, size.height, yuv);
//        bmData.copyTo(bitmap);

            ByteArrayOutputStream outstr = new ByteArrayOutputStream();
            Rect rect = new Rect(0, 0, size.width, size.height);
            YuvImage yuvimage = new YuvImage(data, parameters.getPreviewFormat(), size.width, size.height, null);
            yuvimage.compressToJpeg(rect, 100, outstr);
            Bitmap bitmap = BitmapFactory.decodeByteArray(outstr.toByteArray(), 0, outstr.size());


//        bitmap = FileUtil.rotateImage(bitmap,90);


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();


            String encoded = Base64.encodeToString(outstr.toByteArray(), Base64.DEFAULT);
            encoded = encoded.replace("\n", "");


            Python py = Python.getInstance();
            PyObject console = py.getModule("color_barcode_scanner");
            PyObject pyObject = console.callAttr("scan", encoded);
            Log.v("ahuhu", "object : " + pyObject);
            String value = "";
            if (pyObject != null) {
                value = pyObject.toString();
                if (!TextUtils.isEmpty(value)) {
                    Log.v("ahuhu", "def : " + value);
//                    stopCameraPreview();
//        }
                }
            }
    }


    @SuppressLint("StaticFieldLeak")
    private class ProcessPreviewDataTask extends AsyncTask<byte[], Void, String> {
        Camera camera;

        ProcessPreviewDataTask(Camera camera) {
            this.camera = camera;
        }

        @Override
        protected String doInBackground(byte[]... datas) {
            Log.i("ahihi", "background process started");
            byte[] data = datas[0];
            if (camera == null) {
                return "";
            }
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            Bitmap bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888);
            Allocation bmData = renderScriptNV21ToRGBA8888(
                    getActivity(), size.width, size.height, data);
            bmData.copyTo(bitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            encoded = encoded.replace("\n", "");

            Python py = Python.getInstance();
            PyObject console = py.getModule("color_barcode_scanner");
            PyObject pyObject = console.callAttr("scan", encoded);
            Log.v("ahuhu", "object : " + pyObject);
            String value = "";
            if (pyObject != null) {
                value = pyObject.toString();
                Log.v("ahuhu", "def : " + value);
            }

            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("ahihi", "running onPostExecute " + result);

        }
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

    @Override
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
