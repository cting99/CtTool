package com.ct.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Cting on 2016/9/9.
 */
public class CtCommonReceiver extends BroadcastReceiver {

    private static final String TAG = "cting/CtCommonReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "receive:" + action);
        if (Intent.ACTION_LOCALE_CHANGED.equals(action)) {
//            ImeHelper.updateLatinSubtypeByLocal(context);
        }
    }
}
