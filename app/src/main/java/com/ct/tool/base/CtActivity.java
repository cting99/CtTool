package com.ct.tool.base;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import com.ct.tool.R;
import com.ct.tool.utils.PermissionUtils;

import java.util.List;

public class CtActivity extends PreferenceActivity {

    private final String TAG = "cting/CtActivity";
    public static final boolean DEBUG_LIFE_CYCLE = false;

    public static final String META_DATA_HEADER = "com.ct.tool.HEADER";
    private static final int DEFAULT_META_DATA_HEADER_RES = R.xml.pref_ct_tool_base;

    protected int mHeaderId = DEFAULT_META_DATA_HEADER_RES;
    public PermissionUtils mPermissionUtils;

    public CtActivity(){
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "CtActivity");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onCreate");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mPermissionUtils = new PermissionUtils(this);
            mPermissionUtils.checkPermissions();
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onBuildHeaders");
        getMetaData();
        loadHeadersFromResource(mHeaderId, target);

    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (!mPermissionUtils.hasAllPermission()) {
            Toast.makeText(this, R.string.app_permission_toast, Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private void getMetaData() {
        if(DEBUG_LIFE_CYCLE)   Log.d(TAG, "getMetaData");
        try {
            ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(),
                    PackageManager.GET_META_DATA);
            if (ai != null && ai.metaData != null) {
                mHeaderId = ai.metaData.getInt(META_DATA_HEADER, DEFAULT_META_DATA_HEADER_RES);

            }
        } catch (PackageManager.NameNotFoundException nnfe) {
            // No recovery
            Log.w(TAG, "Cannot get Metadata for: " + getComponentName().toString());
        }
        if (mHeaderId <= 0) {
            mHeaderId = DEFAULT_META_DATA_HEADER_RES;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(DEBUG_LIFE_CYCLE)   Log.i(TAG, "onDestroy");
    }
}
