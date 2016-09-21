package com.ct.tool.fragment;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.ct.tool.R;
import com.ct.tool.base.CtSimpleAdapter;
import com.ct.tool.base.CtSimpleFragment;
import com.ct.tool.utils.ApkInfoHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cting on 2016/9/1.
 */
public class ApkListFragment {

    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_ACTION = "action";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_VERSION = "version";

    public static final int mLayoutId = R.layout.app_info_list_item;
    public static final int mLayoutIdShortcut = R.layout.app_info_list_item_shortcut;

    public static final String[] mFromColumns = new String[]{
            COLUMN_ICON,
            COLUMN_LABEL,
            COLUMN_ACTION,
            COLUMN_PATH,
            COLUMN_VERSION,
    };

    public static final int[] mToIds = new int[]{
            R.id.app_icon,
            R.id.app_label,
            R.id.app_action,
            R.id.app_path,
            R.id.app_version,
    };


    public static class All extends CtSimpleFragment {
        ApkInfoHelper mApkInfoHelper;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mApkInfoHelper = new ApkInfoHelper(getActivity());
        }

        @Override
        protected void createDataMap() {
            List<ApplicationInfo> infoList = mPkgMgr.getInstalledApplications(0);


            for (ApplicationInfo applicationInfo : infoList) {
                String pkgName = applicationInfo.packageName;

                addMap(applicationInfo.loadIcon(mPkgMgr),
                        applicationInfo.loadLabel(mPkgMgr).toString(),
                        pkgName,
                        mApkInfoHelper.getApkPath(applicationInfo),
                        mApkInfoHelper.getApkVersion(pkgName));
            }
        }

        @Override
        protected CtSimpleAdapter createAdapter() {
            mAdapter = new CtSimpleAdapter(getActivity(), mDatas, getLayoutId(), mFromColumns, mToIds);
            return mAdapter;
        }

        public Map<String, Object> addMap(Drawable icon, String label, String action, String apkPath, String version) {
            Map<String, Object> map;
            map = new HashMap<String, Object>();
            map.put(COLUMN_ICON, icon);
            map.put(COLUMN_LABEL, label);
            map.put(COLUMN_ACTION, action);
            map.put(COLUMN_PATH, apkPath);
            map.put(COLUMN_VERSION, version);
            mDatas.add(map);
            return map;
        }

        protected int getLayoutId() {
            return mLayoutId;
        }

        protected String getApkOwner(String pkgName) {
            boolean isThirdApp = mApkInfoHelper.isThirdApp(pkgName);
            return isThirdApp
                    ? getString(R.string.fragment_app_list_third_app)
                    : getString(R.string.fragment_app_list_system_app);
        }

    }

    public static class Shortcut extends All {

        @Override
        protected void createDataMap() {

            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> shortcuts = mPkgMgr.queryIntentActivities(i, 0);
            updateBy(shortcuts);
        }

        @Override
        protected int getLayoutId() {
            return mLayoutIdShortcut;
        }

        protected void updateBy(List<ResolveInfo> lists) {
            for (ResolveInfo resolveInfo : lists) {
                ActivityInfo activityInfo = resolveInfo.activityInfo;
                String pkgName = activityInfo.packageName;

                addMap(mApkInfoHelper.getApkIconWithTheme(activityInfo),
                        activityInfo.loadLabel(mPkgMgr).toString(),
                        pkgName + "/" + activityInfo.name,
                        mApkInfoHelper.getApkPath(pkgName),
                        mApkInfoHelper.getApkVersion(pkgName));
            }
        }
    }

    public static class Home extends Shortcut {
        @Override
        protected void createDataMap() {

            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            List<ResolveInfo> homes = mPkgMgr.queryIntentActivities(i, 0);
            updateBy(homes);
        }
    }

    public static class Ime extends Shortcut {
        @Override
        protected void createDataMap() {

            List<ResolveInfo> services = mPkgMgr.queryIntentServices(
                    new Intent(android.view.inputmethod.InputMethod.SERVICE_INTERFACE),
                    PackageManager.GET_META_DATA);

            for (ResolveInfo resolveInfo : services) {
                ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                String pkgName = serviceInfo.packageName;

                addMap(getImeIcon(resolveInfo, serviceInfo),
                        serviceInfo.loadLabel(mPkgMgr).toString(),
                        pkgName + "/" + serviceInfo.name,
                        mApkInfoHelper.getApkPath(pkgName),
                        mApkInfoHelper.getApkVersion(pkgName));
            }
        }

        private Drawable getImeIcon(ResolveInfo resolveInfo, ServiceInfo serviceInfo) {
            Drawable serviceDrawable = serviceInfo.loadIcon(mPkgMgr);
            Drawable resolveDrawable = resolveInfo.loadIcon(mPkgMgr);

            return serviceDrawable == null ? resolveDrawable : serviceDrawable;
        }
    }

    public static class Widget extends Shortcut {

        public static final String COLUMN_WIDGET_SIZE = "widget_size";
        public static final String[] mWidgetFromColumns = new String[]{
                COLUMN_ICON,
                COLUMN_LABEL,
                COLUMN_ACTION,
                COLUMN_PATH,
                COLUMN_VERSION,
                COLUMN_WIDGET_SIZE,
        };

        public static final int[] mWidgetToIds = new int[]{
                R.id.app_icon,
                R.id.app_label,
                R.id.app_action,
                R.id.app_path,
                R.id.app_version,
                R.id.widget_size,
        };

        @Override
        protected int getLayoutId() {
            return R.layout.app_info_list_item_widget;
        }

        @Override
        protected CtSimpleAdapter createAdapter() {
            return new CtSimpleAdapter(mContext,
                    mDatas,
                    getLayoutId(),
                    mWidgetFromColumns,
                    mWidgetToIds);
        }

        public Map<String, Object> addMap(Drawable icon, String label, String action, String apkPath, String version, String widgetSize) {
            Map<String, Object> map = super.addMap(icon, label, action, apkPath, version);
            map.put(COLUMN_WIDGET_SIZE, widgetSize);
            return map;
        }

        @Override
        protected void createDataMap() {
            AppWidgetManager widgets = AppWidgetManager.getInstance(mContext);
            List<AppWidgetProviderInfo> widgetsInfo = widgets.getInstalledProviders();

            final ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            int iconDpi = am.getLauncherLargeIconDensity();

            for (AppWidgetProviderInfo info : widgetsInfo) {
                String pkgName = info.provider.getPackageName();
                addMap(getWidgetIcon(info, iconDpi),
                        info.loadLabel(mPkgMgr),
                        info.provider.flattenToShortString(),
                        mApkInfoHelper.getApkPath(pkgName),
                        mApkInfoHelper.getApkVersion(pkgName));
            }
        }

        private String getWidgetSize(AppWidgetProviderInfo info) {
            return getGride(info.minWidth) + " x " + getGride(info.minHeight);
        }

        private static int getGride(int size) {
            return (size + 30) / 70;
//            return (size + 2) / 74;
        }

        private Drawable getWidgetIcon(AppWidgetProviderInfo info, int density) {
            Drawable apkIcon = info.loadIcon(mContext, density);
            Drawable previewIcon = info.loadPreviewImage(mContext, density);
            return previewIcon == null ? apkIcon : previewIcon;
        }

    }

    public static final String APK_NAME = "apk_name";
    public static final String APK_LABEL = "apk_label";
    public static final String APK_OWNER = "apk_owner";

    public static class Project extends All {
        //app name,app label,version,owner

        @Override
        protected void createDataMap() {
            List<ApplicationInfo> infoList = mPkgMgr.getInstalledApplications(0);


            for (ApplicationInfo applicationInfo : infoList) {
                String pkgName = applicationInfo.packageName;

                addMap(mApkInfoHelper.getApkName(pkgName),
                        applicationInfo.loadLabel(mPkgMgr).toString(),
                        mApkInfoHelper.getApkVersion(pkgName),
                        getApkOwner(pkgName),
                        mApkInfoHelper.getApkPath(applicationInfo));
            }
        }

        @Override
        protected CtSimpleAdapter createAdapter() {
            mAdapter = new CtSimpleAdapter(getActivity(), mDatas,
                    R.layout.app_info_list_item_project,
                    new String[]{
                            APK_NAME,
                            APK_LABEL,
                            COLUMN_VERSION,
                            APK_OWNER,
                            COLUMN_PATH,
                    },
                    new int[]{
                            R.id.app_name,
                            R.id.app_label,
                            R.id.app_version,
                            R.id.app_owner,
                            R.id.app_path,
                    });
            return mAdapter;
        }

        public Map<String, Object> addMap(String apkName, String apkLabel, String apkVersion, String apkOwner, String apkPath) {
            Map<String, Object> map;
            map = new HashMap<String, Object>();
            map.put(APK_NAME, apkName);
            map.put(APK_LABEL, apkLabel);
            map.put(COLUMN_VERSION, apkVersion);
            map.put(APK_OWNER, apkOwner);
            map.put(COLUMN_PATH, apkPath);
            mDatas.add(map);
            return map;
        }
    }
}
