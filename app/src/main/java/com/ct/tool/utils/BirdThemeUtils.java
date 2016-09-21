package com.ct.tool.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Cting on 2016/8/15.
 */
public class BirdThemeUtils {
    public static final String TAG = "BirdThemeUtils";
    public static final boolean DEBUG = true;

    public static final boolean BIRD_THEME_APK_MANAGER = true;//SystemProperties.getBoolean("ro.bdfun.themeapk_manager", false);
    private static final String CUR_THEME = "curTheme";

    public static String getCurrentTheme(Context context) {
        String curTheme = Settings.System.getString(context.getContentResolver(), CUR_THEME);
        return curTheme;
    }

    public static Resources getApkResources(Context context, String themePkgName) {
        if (TextUtils.isEmpty(themePkgName)) {
            return null;
        }
        Resources res = null;
        try {
            res = context.getPackageManager().getResourcesForApplication(themePkgName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean isCurrentThemeValid(Context context) {
        String curTheme = getCurrentTheme(context);
        Resources res = getApkResources(context, curTheme);
        if (res != null) {
            return true;
        }
        return false;
    }

    public static Drawable getDrawableFromTheme(Context context, String themeIconName, int iconDpi) {
        String curTheme = getCurrentTheme(context);
        if (TextUtils.isEmpty(curTheme)) {
            if(DEBUG)   Log.i(TAG, "bird,getDrawableFromTheme,current theme null,themeIconName="+themeIconName);
            return null;
        }
        Resources res = getApkResources(context, curTheme);
        if (res == null) {
            if(DEBUG)   Log.i(TAG, "bird,getDrawableFromTheme,current theme " + curTheme + " resources null");
            return null;
        }
        String drawableName = themeIconName.replace(".", "_").toLowerCase();// like com_android_deskclock_deskclock
        int identifier = res.getIdentifier(drawableName, "drawable", curTheme);
        if (identifier == 0) {
            return null;
        }
        if(DEBUG)   Log.d(TAG, "bird,getDrawableFromTheme,get icon [" + drawableName + "] from [" + curTheme + "] : " + identifier);
        return iconDpi==-1 ? res.getDrawable(identifier) : res.getDrawableForDensity(identifier, iconDpi);
    }

    public static Drawable getDrawableFromTheme(Context context, String themeIconName){
        return getDrawableFromTheme(context, themeIconName, -1);
    }

    public static Drawable getDrawableFromTheme(Context context, ActivityInfo activityInfo, int iconDpi) {
        if (activityInfo == null) {
            if(DEBUG)   Log.i(TAG, "bird,getDrawableFromTheme,activityInfo null");
            return null;
        }
        return getDrawableFromTheme(context, activityInfo.name, iconDpi);
    }

    public static Drawable getDrawableFromTheme(Context context, ActivityInfo activityInfo){
        return getDrawableFromTheme(context, activityInfo, -1);
    }

    public static Drawable getDrawableFromTheme(Context context, ResolveInfo resolveInfo, int iconDpi) {
        if (resolveInfo == null) {
            if(DEBUG)   Log.d(TAG, "bird,getDrawableFromTheme,resolveInfo null");
            return null;
        }
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        return getDrawableFromTheme(context, activityInfo, iconDpi);
    }

    public static Drawable getDrawableFromTheme(Context context, ResolveInfo resolveInfo){
        return getDrawableFromTheme(context, resolveInfo, -1);
    }



    public static void setDrawable(Context context, ImageView imageView, String iconName, int iconDpi, Drawable defaultDrawable) {
        if (imageView == null || TextUtils.isEmpty(iconName) || defaultDrawable == null) {
            if(DEBUG)   Log.i(TAG, "setDrawable,imageView=" + imageView + ",iconName=" + iconName + ",defaultDrawable=" + defaultDrawable);
            return;
        }
        Drawable themeDrawable = getDrawableFromTheme(context, iconName, iconDpi);
        if (themeDrawable == null) {
            themeDrawable = defaultDrawable;
        }
        imageView.setImageDrawable(themeDrawable);
    }

    public static void setDrawable(Context context, ImageView imageView, String iconName, Drawable defaultDrawable) {
        setDrawable(context, imageView, iconName, -1, defaultDrawable);
    }


    public static void setDrawable(Context context, ImageView imageView, ResolveInfo resolveInfo, Drawable defaultDrawable) {
        if (resolveInfo == null) {
            if(DEBUG)   Log.i(TAG, "bird,setDrawable,resolveInfo null");
            return;
        }
        setDrawable(context, imageView, resolveInfo.activityInfo.name, -1, defaultDrawable);
    }
}
