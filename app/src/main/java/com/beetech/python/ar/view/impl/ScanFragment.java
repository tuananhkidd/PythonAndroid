package com.beetech.python.ar.view.impl;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.renderscript.Type;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
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
import com.beetech.python.ar.util.FileUtil;
import com.beetech.python.ar.view.ScanView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

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
    CameraDevice cameraDevice;

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
//            PyObject pyObject = console.callAttr("scan", encoded);
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
        setCameraDisplayOrientation(getActivity(), Camera.CameraInfo.CAMERA_FACING_BACK, camera);
        Camera.Parameters parameters = camera.getParameters();
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
        Bitmap bitmapTest = BitmapFactory.decodeByteArray(data, 0, data.length);


//        bitmap = FileUtil.rotateImage(bitmap,90);

//        try {
//            FileUtil.saveBitmapToFile(bitmap,FileUtil.createImageFile(System.currentTimeMillis()+""));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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

            byte[] decodedString = Base64.decode(value, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            Log.v("ahuhu", "def : " + value);
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

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
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
