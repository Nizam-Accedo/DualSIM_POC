package com.airtel.wynk.getimei;

import android.annotation.TargetApi;
import android.content.Context;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


@TargetApi(22)
public class DualSimManagerLolipopNew {

    public static SubscriptionManager sm = null;

    public DualSimManagerLolipopNew(Context applicationContext) {
        // TODO Auto-generated constructor stub
        if (sm == null) {
            sm = SubscriptionManager.from(applicationContext);
        }
    }

    public int getSimSupportedCount() {
        return sm.getActiveSubscriptionInfoCount();
    }

    public JSONArray getActiveSubscriptionInfo(Context applicationContext) {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(applicationContext);
        String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();
        JSONArray arr = new JSONArray();
        List<SubscriptionInfo> list = sm.getActiveSubscriptionInfoList();
        for (int index = 0; index < list.size(); index++) {
            SubscriptionInfo subInfo = list.get(index);
            if (subInfo != null) {
                JSONObject obj = new JSONObject();
                try {
                    CharSequence carrierName = subInfo.getCarrierName();
                    TelephonyManager telephony = (TelephonyManager) applicationContext.getSystemService(Context.TELEPHONY_SERVICE);
                    telephony.getDeviceId();
                    String networkTypeName = Utility.getNetworkTypeName(subInfo.getSubscriptionId());

                    if (index == 0)
                        obj = DualSimManagerNew.getSimInfoJson("SIM_" + (index + 1), networkTypeName, telephony.isNetworkRoaming(), carrierName.toString(), imeiSIM1);
                    else
                        obj = DualSimManagerNew.getSimInfoJson("SIM_" + (index + 1), networkTypeName, telephony.isNetworkRoaming(), carrierName.toString(), imeiSIM2);
                } catch (JSONException e) {

                }
                arr.put(obj);


            }
        }
        return arr;
    }
}
