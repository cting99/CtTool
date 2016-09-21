package com.ct.tool.utils;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import com.ct.tool.R;

import java.util.List;

public class ImeHelper {

    private static final String TAG = "cting/ImeHelper";

    private Context mContext;
    private List<InputMethodInfo> mImiList;
    private PackageManager mPkgMgr;
    private ContentResolver mResolver;
    InputMethodManager mImeMgr;

    public ImeHelper(Context context){
        this(context, context.getPackageManager());
    }

    public ImeHelper(Context context, PackageManager packageManager) {
        mContext = context;
        mPkgMgr = packageManager;
        mResolver = context.getContentResolver();
        mImeMgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImiList = mImeMgr.getInputMethodList();
    }

    public String getPkgNameFromComponent(@NonNull String componentName) {
        return componentName.contains("/")
                ? componentName.substring(0, componentName.indexOf("/"))
                : componentName;
    }

    public ApplicationInfo getApplicationInfo(@NonNull String pkgName) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mPkgMgr.getApplicationInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }finally {
            return applicationInfo;
        }
    }

    private InputMethodInfo getInputMethodInfo(@NonNull String componentName) {
        if (mImiList == null) {
            Log.w(TAG, "getInputMethodInfo,mImiList null");
            return null;
        }
        for (InputMethodInfo info : mImiList) {
            if (info.getPackageName().equals(getPkgNameFromComponent(componentName))) {
                return info;
            }
        }
        Log.w(TAG, "getInputMethodInfo,no fitable ime:" + componentName);
        return null;
    }

    private InputMethodSubtype getSubtype(InputMethodInfo inputMethodInfo, int subTypeHashCode){
        if (inputMethodInfo == null) {
            Log.w(TAG, "getSubtype:" + inputMethodInfo+","+subTypeHashCode);
            return null;
        }
        for (int i = 0; i < inputMethodInfo.getSubtypeCount(); i++) {
            final InputMethodSubtype subtype = inputMethodInfo.getSubtypeAt(i);
            if (subtype.hashCode() == subTypeHashCode) {
                return subtype;
            }
        }
        return null;
    }

    public InputMethodSubtype getSubtype(@NonNull String component, int subTypeHashCode) {
        return getSubtype(getInputMethodInfo(component), subTypeHashCode);
    }

    private String getSubTypeName(@NonNull String component, InputMethodSubtype subType, int subTypeHashCode) {
        String pkgName = getPkgNameFromComponent(component);
        ApplicationInfo info = getApplicationInfo(pkgName);
        CharSequence subTypeName = "";
        if (info != null && subType!=null) {
            subTypeName = subType.getDisplayName(mContext, pkgName, info);
        }
        return TextUtils.isEmpty(subTypeName) ? "No name" : subTypeName.toString();
    }

    /*
    param component: com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME
    param subTypeHashCode: -921088104
    return:
        -921088104 {en_US, English (US)}
    or empty
    * */
    public String parseSubTypeInfoDisplay(@NonNull String component, int subTypeHashCode) {
        if (subTypeHashCode == -1) {
            return "";
        }
        InputMethodSubtype subType = getSubtype(component, subTypeHashCode);
        String local = "unknow local";
        CharSequence subTypeName = "unknow name";
        if (subType != null) {
            local = subType.getLocale();
            subTypeName = getSubTypeName(component, subType, subTypeHashCode);
        }
        return mContext.getString(R.string.fragment_config_format_ime_subtype, subTypeHashCode, local, subTypeName);
    }

    public String parseSubTypeInfoDisplay(@NonNull String component, String subTypeHashCodeStr) {
        return parseSubTypeInfoDisplay(component, Integer.valueOf(subTypeHashCodeStr));
    }

    public String parseIme(@NonNull String component){
        if (TextUtils.isEmpty(component)) {
            return "";
        }
        Log.d(TAG, "----parseIme:" + component);
        InputMethodInfo imi = getInputMethodInfo(component);
        if (imi != null) {
            LineBuilder lineBuilder = new LineBuilder(mContext);
            lineBuilder.append(imi.loadLabel(mPkgMgr));
            lineBuilder.append(component);
            return lineBuilder.toString();
        }else {
            return component;
        }
    }

    /*
    param info format like:
    com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME;931682827;843948332;816242702;963984255;774684257;-1923548115;-921088104
    or
    com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME;-1
    * */
    public String parseImeWithSubtype(@NonNull String info) {
        if (TextUtils.isEmpty(info)) {
            return "";
        }
        LineBuilder lineBuilder = new LineBuilder(mContext);

        Log.d(TAG, "----parseImeWithSubtype," + info);
        String[] array = info.split(";");

        String imeComponent = array[0];
        lineBuilder.append(parseIme(array[0]));

        int count = array.length;
        for(int i = 1;i<count;i++) {
            String subtypDisplay = parseSubTypeInfoDisplay(imeComponent, array[i]);
            lineBuilder.append(subtypDisplay);
        }
        return lineBuilder.toString();
    }

    public String parseImeWithSubtype(@NonNull String component,@NonNull int subtype) {
        return parseImeWithSubtype(component + ";" + subtype);
    }

    public String getImeDisplay(@NonNull String imeInfo) {
        if (TextUtils.isEmpty(imeInfo)) {
            return "";
        }
        if (imeInfo.contains(";")) {
            return parseImeWithSubtype(imeInfo);
        }
        return parseIme(imeInfo);
    }

    public String getCurrentImeDisplay() {
        String currentIme = Settings.Secure.getString(mResolver, Settings.Secure.DEFAULT_INPUT_METHOD);
        int selectedSubType = Settings.Secure.getInt(mResolver, Settings.Secure.SELECTED_INPUT_METHOD_SUBTYPE,-1);
        return parseImeWithSubtype(currentIme, selectedSubType);
    }

    public String getEnabledImeDisplay() {
        String enabledIme = Settings.Secure.getString(mResolver, Settings.Secure.ENABLED_INPUT_METHODS);
        if (TextUtils.isEmpty(enabledIme)) {
            return "no enabled ime";
        }
        if (!enabledIme.contains(":")) {
            return getImeDisplay(enabledIme);
        }
        String[] enabledImeArray = enabledIme.split(":");
        int count = enabledImeArray.length;
        LineBuilder lineBuilder = new LineBuilder(mContext);
        for (int i=0;i<count;i++) {
            String imeInfo = enabledImeArray[i];

            lineBuilder.append(i+1)
                    .append(getImeDisplay(imeInfo))
                    .enter();
        }
        return lineBuilder.toString();
    }


    /*
    Settings.Secure.DEFAULT_INPUT_METHOD
    com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME

    Settings.Secure.SELECTED_INPUT_METHOD_SUBTYPE
    -921088104

    Settings.Secure.ENABLED_INPUT_METHODS
    com.google.android.inputmethod.korean/.KoreanIme:
    com.google.android.apps.inputmethod.hindi/.HindiInputMethodService:
    com.android.inputmethod.latin/.LatinIME:
    com.google.android.inputmethod.latin/com.android.inputmethod.latin.LatinIME;931682827;843948332;816242702;963984255:
    com.google.android.googlequicksearchbox/com.google.android.voicesearch.ime.VoiceInputMethodService:
    com.google.android.inputmethod.pinyin/.PinyinIME:
    com.inputmethod.mi/com.funny.inputmethod.imecontrol.FunnyIME:
    com.google.android.inputmethod.japanese/.MozcService
    * */
}