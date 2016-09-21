package com.ct.tool.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Cting on 2016/8/27.
 */
public class PermissionUtils {


    private static final String[] ALL_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
    };
    private static final String TAG = "cting/Permission";

    Activity mActivity;

    public PermissionUtils(Activity activity) {
        mActivity = activity;
    }

    public void checkPermissions() {
        if (!hasAllPermission()) {
            grantAllPermissoin();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        boolean ret = mActivity.getApplicationContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        if (ret) {
//            Log.i(TAG, "permission " + permission + " already granted");
        }else{
            Log.w(TAG, "permission " + permission + " not granted");
        }
        return ret;
    }


    public boolean hasAllPermission() {
        for (String permission : ALL_PERMISSIONS) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    public void grantAllPermissoin() {
        String[] missingPermission = getMissingPermissions(ALL_PERMISSIONS);
        if (missingPermission != null && missingPermission.length>0) {
            grantPermission(missingPermission);
        }
//        for (String permission : missingPermission) {
//        }
    }

    public String[] getMissingPermissions(String[] requiredPermissions) {
        final ArrayList<String> missingList = new ArrayList<String>();

        for (int i = 0; i < requiredPermissions.length; i++) {
            if (!hasPermission(requiredPermissions[i])) {
                missingList.add(requiredPermissions[i]);
            }
        }
        int size = missingList.size();
        Log.d(TAG, "miss permission:" + missingList.toString());
        String[] list = new String[size];
        for (int i = 0; i < size; i++) {
            list[i] = missingList.get(i);
        }
        return list;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void grantPermission(String[] permissions) {
        Log.d(TAG, "grant permission:" + permissions);
        mActivity.requestPermissions(permissions, 0);
    }
}
