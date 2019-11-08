package com.beetech.python.ar.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.beetech.python.ar.BuildConfig;
public class DeviceUtil {

    public static boolean hasNetworkConnection(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        }

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }

    }

    public static void showSoftKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void clearFocus(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            activity.getCurrentFocus().clearFocus();
        }
    }

    public static String getAppVersion() {
        return BuildConfig.VERSION_NAME;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Context context, String... listPermission) {
        if (context == null || listPermission == null || listPermission.length == 0)
            return false;
        for (String permission : listPermission) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public static boolean shouldShowRequestPermission(AppCompatActivity context, String... listPermission) {
        if (context == null || listPermission == null || listPermission.length == 0)
            return false;
        for (String permission : listPermission) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission))
                return false;
        }
        return true;
    }

    public static void showDialogSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void openPlayStore(Context context) {
        String appPackageName = context.getPackageName();
        if (BuildConfig.DEBUG) {
            appPackageName = "com.aic.tet9999";
        }
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
