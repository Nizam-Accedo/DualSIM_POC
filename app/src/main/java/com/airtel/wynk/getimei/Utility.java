package com.airtel.wynk.getimei;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class Utility {

	public static Context context = null;

	public static String getNetworkTypeName(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "CDMA - EvDo rev. 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "CDMA - EvDo rev. A";
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return "CDMA - EvDo rev. B";
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "CDMA - 1xRTT";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "LTE";
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return "CDMA - eHRPD";
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "iDEN";
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "HSPA+";
            default:
                return "UNKNOWN";
        }
    }

	public static boolean isConnectedToInternet(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}


	public static String getAndroidId(Context mContext) {
		String androidId = "NA";
		try {
			androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return androidId;
	}


    /**
     * This method is used to set shared preferences
     *
     * @param context Application context
     * @param key     shared object key
     * @param value   shared object value
     */
    public static void setPreferences(Context context, String key, String value) {
        if (context != null) {
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(context);
            if (appSharedPrefs != null) {
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                if (prefsEditor != null && key != null) {
                    prefsEditor.putString(key, value);
                    prefsEditor.commit();
                }
            }
        }

    }

    /**
     * This method is used to get shared object
     *
     * @param key shared object key
     * @return return value, for default "" asign.
     */
    public static String getPreferences(Context mcontext, String key) {

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(mcontext);

        String json = appSharedPrefs.getString(key, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return json;
    }
}
