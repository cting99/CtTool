package com.ct.tool.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ct.tool.base.CtSimpleFragment;
import com.ct.tool.utils.LineBuilder;

import java.util.List;

/**
 * Created by Cting on 2016/8/30.
 */
public class SimFragment extends CtSimpleFragment {

    private static final String TAG = "cting/sim";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void createDataMap() {
        SubscriptionManager subscriptionMgr = SubscriptionManager.from(getActivity());
        List<SubscriptionInfo> infoList = subscriptionMgr.getActiveSubscriptionInfoList();

        String line;
        if (infoList == null || infoList.size() == 0) {
            addMap("No sim insert!");
        }else {
            for (SubscriptionInfo info : infoList) {
                Log.d(TAG, "infoList=" + infoList.toString());
                line = parseSubscriptionInfo(info);
                addMap(line);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private String parseSubscriptionInfo(SubscriptionInfo info) {
        int slot = info.getSimSlotIndex() + 1;
        String mccmnc = LineBuilder.formatMccmnc(info.getMcc(), info.getMnc());
        boolean isRoaming = (info.getDataRoaming() == 1);

        LineBuilder lineBuilder = new LineBuilder(mContext);
        lineBuilder.append("SIM", String.valueOf(slot));
        lineBuilder.append("CarrierName", info.getCarrierName());
        lineBuilder.append("DisplayName", info.getDisplayName());
        lineBuilder.append("MccMnc", mccmnc);
        lineBuilder.append("Roaming", isRoaming);

        return lineBuilder.toString();
    }

}
