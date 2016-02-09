package com.airtel.wynk.getimei;

import android.content.Context;

/**
 * Created by team5 on 9/2/16.
 */
public class ImeiUtil {
    public static String[] getImei(Context mContext) {
        CustomTelephony telephony = new CustomTelephony(mContext);
        if (android.os.Build.VERSION.SDK_INT < 22) {
            return telephony.getIMEIPreLolipop();
        } else if (android.os.Build.VERSION.SDK_INT >= 22) {
            return telephony.getIMEIPostLolipop();
        }
        return null;
    }

    }


