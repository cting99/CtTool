package com.ct.tool.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import com.ct.tool.R;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Cting on 2016/8/31.
 */
public class LineBuilder {

    private static final String TAG = "cting/LineBuilder";
    public static final String LINE_BREAK = "\n";

    /* LABEL_VALUE:
           [LABEL] value
    
       TITLE_SUMMARY:
           [LABEL]
                summary
        */
    @IntDef({LABEL_VALUE,TITLE_SUMMARY,TITLE_SUMMARY_NO_TAB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FormatType{}

    public static final int LABEL_VALUE = 0;
    public static final int TITLE_SUMMARY = 1;
    public static final int TITLE_SUMMARY_NO_TAB = 2;

    private StringBuilder sBuilder = new StringBuilder();
    private int mFormatType = LABEL_VALUE;
    private Context mContext;

    public LineBuilder(Context context, @FormatType int type) {
        mContext = context;
        setType(type);
    }

    public LineBuilder(Context context) {
        mContext = context;
    }

    public LineBuilder setType(@FormatType int type) {
        mFormatType = type;
        return this;
    }

    private String getObjValue(Object obj) {
        String value = "";
        if (obj == null) {
            value = "null";
        }else if (obj instanceof Integer || obj instanceof CharSequence) {
            value = String.valueOf(obj);
        } else if (obj instanceof Boolean) {
            value = (Boolean) obj ? "TRUE" : "FALSE";
        }else {
            value = obj.toString();
        }
        return value;
    }

    public LineBuilder append(Object obj) {
        append(getObjValue(obj));
        return this;
    }

    public LineBuilder append(String line) {
        sBuilder.append(line).append(LINE_BREAK);
        switch (mFormatType) {
            case TITLE_SUMMARY:
            case TITLE_SUMMARY_NO_TAB:
                sBuilder.append(LINE_BREAK);
                break;

        }
        return this;
    }

    public LineBuilder enter() {
        sBuilder.append(LINE_BREAK);
        return this;
    }

    private static String getFormatString(Context context, @FormatType final int type, String value1, String value2) {
        int formatResId = R.string.text_line_format_label_value;
        switch (type) {
            case LABEL_VALUE:
                formatResId = R.string.text_line_format_label_value;
                break;
            case TITLE_SUMMARY:
                formatResId = R.string.text_line_format_title_summary;
                break;
            case TITLE_SUMMARY_NO_TAB:
                formatResId = R.string.text_line_format_title_summary_no_tab;
                break;
        }
        value2 = TextUtils.isEmpty(value2) ? "--" : value2;
        return context.getString(formatResId, value1, value2);
    }
    private String getFormatString(Context context, String value1, String value2) {
        return getFormatString(mContext, mFormatType, value1, value2);
    }

    public LineBuilder append(String label, String value) {
        return append(getFormatString(mContext, label, value));
    }

    public LineBuilder append(String label, Object obj) {
        return append(label, getObjValue(obj));
    }

    public LineBuilder append(@StringRes int labelId, Object obj) {
        return append(mContext.getString(labelId), obj);
    }


    public static final String printArray(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String str : array) {
            sb.append(str).append(",");
        }
        return sb.toString();
    }

    public static String fillUpZero(int number) {
        return (number <= 9) ? ("0" + number) : String.valueOf(number);
    }

    public static String formatMccmnc(int mcc, int mnc) {
        Log.d(TAG, "mcc=" + mcc + ",mnc=" + mnc);
        if (mcc == 0 && mnc == 0) {
            return "";
        }
        return mcc + " " + fillUpZero(mnc);
    }

    @Override
    public String toString() {
        return sBuilder.toString();
    }
}
