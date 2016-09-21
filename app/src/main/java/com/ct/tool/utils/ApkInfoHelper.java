package com.ct.tool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.ct.tool.R;

/**
 * Created by Cting on 2016/9/1.
 */
public class ApkInfoHelper {

    private static final String TAG = "cting/CtTool";
    Context mContext;
    PackageManager mPkgMgr;

    public ApkInfoHelper(Context context){
        this(context, context.getPackageManager());
    }
    public ApkInfoHelper(Context context, PackageManager pkgMgr) {
        mContext = context;
        mPkgMgr = pkgMgr;
    }

    public Drawable getApkIconWithTheme(String activityClassName, Drawable icon) {
        if (BirdThemeUtils.BIRD_THEME_APK_MANAGER) {
            Drawable themeDrawable = BirdThemeUtils.getDrawableFromTheme(mContext, activityClassName);
            if (themeDrawable != null) {
                return themeDrawable;
            }
        }
        return icon;
    }

    public Drawable getApkIconWithTheme(ActivityInfo activityInfo) {
        String activityClassName = activityInfo.name;
        if (BirdThemeUtils.BIRD_THEME_APK_MANAGER) {
            Drawable themeDrawable = BirdThemeUtils.getDrawableFromTheme(mContext, activityClassName);
            if (themeDrawable != null) {
                return themeDrawable;
            }
        }
        Drawable icon = activityInfo.loadIcon(mPkgMgr);
        return icon;
    }

    public ApplicationInfo getApplicationInfo(@NonNull String pkgName) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mPkgMgr.getApplicationInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return applicationInfo;
        }
    }

    public String getApkPath(ApplicationInfo applicationInfo) {
        if (applicationInfo == null) {
            return "";
        }
        return applicationInfo.sourceDir;
    }

    public String getApkPath(String pkgName) {
        // get apk path by package code path is not effect on framework-res.apk whose package name is "android"
        /*
        try {
            Context pkgContext = context.createPackageContext(pkgName, 0);
            return pkgContext.getPackageCodePath();
        } catch (Exception e) {
            return "";
        }
        */
        return getApkPath(getApplicationInfo(pkgName));
    }

    public String getApkName(ApplicationInfo applicationInfo) {
        return getApkNameFromPath(getApkPath(applicationInfo));
    }

    public String getApkName(String pkgName) {
        return getApkNameFromPath(getApkPath(pkgName));
    }

    private String getApkNameFromPath(String apkPath) {
        return apkPath.substring(apkPath.lastIndexOf("/") + 1, apkPath.length() - 4);
    }

    public String getApkVersion(String pkgName) {
        String version = "";
        PackageInfo pi = null;
        try {
            pi = mPkgMgr.getPackageInfo(pkgName, 0);
            if (pi != null) {
                version = pi.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return version;
        }
    }


    private static Signature[] sSystemSignature;

    public static boolean isSystemPackage(PackageManager pm, PackageInfo pkg) {
        if (sSystemSignature == null) {
            sSystemSignature = new Signature[]{getSystemSignature(pm)};
        }
        return sSystemSignature[0] != null && sSystemSignature[0].equals(getFirstSignature(pkg));
    }

    private static Signature getFirstSignature(PackageInfo pkg) {
        if (pkg != null && pkg.signatures != null && pkg.signatures.length > 0) {
            return pkg.signatures[0];
        }
        return null;
    }

    private static Signature getSystemSignature(PackageManager pm) {
        try {
            final PackageInfo sys = pm.getPackageInfo("android", PackageManager.GET_SIGNATURES);
            return getFirstSignature(sys);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isSystemApp(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public boolean isSystemUpdateApp(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public boolean isThirdApp(PackageInfo pkgInfo) {
        return (!isSystemApp(pkgInfo) && !isSystemUpdateApp(pkgInfo));
    }

    public boolean isThirdApp(String pkgName) {
        boolean isThirdApp = true;
        PackageInfo pi = null;
        try {
            pi = mPkgMgr.getPackageInfo(pkgName, 0);
            if (pi != null) {
                isThirdApp = isThirdApp(pi);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } finally {
            return isThirdApp;
        }
    }

    public static void enterApp(Activity activity, String action) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setAction(action);
        try {
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, action + " not found!", Toast.LENGTH_LONG).show();
        }
    }

    public static void enterDevelopmentApp(Activity activity) {
        enterApp(activity, activity.getString(R.string.fragment_development_action));
    }
}
