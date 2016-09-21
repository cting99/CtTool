package com.ct.tool.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ct.tool.R;
import com.ct.tool.base.CtSimpleFragment;
import com.ct.tool.utils.ImeHelper;
import com.ct.tool.utils.LineBuilder;

import java.util.Locale;
import java.util.UUID;

/**
 * Created by Cting on 2016/8/27.
 */
public class DeviceConfigFragment extends CtSimpleFragment {
    @Override
    protected void createDataMap() {
        Resources res = getActivity().getResources();
        Configuration configuration = res.getConfiguration();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        Log.d(TAG, "Configuration=" + configuration.toString());
        Log.d(TAG, "DisplayMetrics=" + dm.toString());

        addMap(parseConfiguration(configuration, dm));
        addMap(getImeSystemSettings());


//        final ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        int iconDpi = am.getLauncherLargeIconDensity();
//        int iconSize = am.getLauncherLargeIconSize();
//        Log.d(TAG, "iconDpi=" + iconDpi + ",iconSize=" + iconSize);

    }

    private String parseConfiguration(Configuration configuration, DisplayMetrics displayMetrics) {
        Locale locale = configuration.locale;//Locale.getDefault();
        String mccmnc = LineBuilder.formatMccmnc(configuration.mcc, configuration.mnc);
        int statusBarHeight = 0;
        String screenSize = displayMetrics.heightPixels + " x " + (displayMetrics.widthPixels + statusBarHeight);
        String orientation = (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                ? "(land)"
                : "(port)";

        LineBuilder lineBuilder = new LineBuilder(mContext);
        lineBuilder.append(R.string.fragment_config_label_product, Build.PRODUCT);
        lineBuilder.append("API", Build.VERSION.RELEASE);
        lineBuilder.append("SDK", Build.VERSION.SDK_INT);
        lineBuilder.append(R.string.fragment_config_label_lcm_size, (screenSize + orientation));
        lineBuilder.append(R.string.fragment_config_label_local, locale.getDisplayName());
        lineBuilder.append(R.string.fragment_config_label_local_code, locale.toString());
        lineBuilder.append(R.string.fragment_config_label_local_mcc_mnc, mccmnc);
//        lineBuilder.append("UUID", getUUID());

        return lineBuilder.toString();
    }



    private String getImeSystemSettings() {
        ImeHelper imeUtils = new ImeHelper(mContext, mPkgMgr);
        String currentImeStr = imeUtils.getCurrentImeDisplay();
        String enabledImeStr = imeUtils.getEnabledImeDisplay();

        Log.d(TAG, "currentImeStr=" + currentImeStr
                + "\n" + "enabledImeStr=" + enabledImeStr);

        LineBuilder lineBuilder = new LineBuilder(mContext);
        lineBuilder.setType(LineBuilder.TITLE_SUMMARY_NO_TAB).append("IME");
        lineBuilder.append("Current ime", currentImeStr);
        lineBuilder.append("Enabled ime", enabledImeStr);
        return lineBuilder.toString();
    }

    private String getUUID() {
        final TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" +  Settings.Secure.getString(mResolver, android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }



}
