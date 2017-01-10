package com.tomorrowhi.thdemo.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by zhaotaotao on 09/01/2017.
 * 权限检测
 */
public class PermissionUtil {

    public static int LOCATION_PERMISSIONS_REQUEST = 1;
    public static int EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 2;

    /**
     * 检查是否已经允许定位权限
     *
     * @param activity this
     */
    public static void locationPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            //申请授权
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                            Manifest.permission.CHANGE_WIFI_STATE,
                            Manifest.permission.READ_PHONE_STATE
                    }, LOCATION_PERMISSIONS_REQUEST);
        }
    }

    /**
     * 检查是否已经允许存储权限
     *
     * @param activity this
     */
    public static void externalStoragePermission(Activity activity) {
        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) && ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            //申请授权
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, EXTERNAL_STORAGE_PERMISSIONS_REQUEST);
        }
    }
}
