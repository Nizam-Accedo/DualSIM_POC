package com.airtel.wynk.getimei;


import android.annotation.TargetApi;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

@TargetApi(22)
public class DualSimManagerLolipop{
	public static SubscriptionManager sm = null;
		Context mContext;
		public DualSimManagerLolipop(Context context) {
			// TODO Auto-generated constructor stub
			this.mContext = context;
			if(sm==null){
				sm =SubscriptionManager.from(mContext);
			}
		}

		public int getSimSupportedCount(){
			return sm.getActiveSubscriptionInfoCount();
		}
		public List<ActiveNetworkStatus> getActiveSubscriptionInfo(){
			List<SubscriptionInfo> list = sm.getActiveSubscriptionInfoList();
			List<ActiveNetworkStatus> networkList = new ArrayList<ActiveNetworkStatus>();
			for (int index = 0; index < list.size(); index++) {
				SubscriptionInfo subInfo = list.get(index);
				if(subInfo!=null){
					ActiveNetworkStatus network = new ActiveNetworkStatus();
					network.setCarrierName(String.valueOf(subInfo.getDisplayName()));
					network.setImsi(subInfo.getIccId());
					network.setCellid("0");
					network.setLoc("-1");
					network.setMCC(String.valueOf(subInfo.getMcc()));
					network.setMNC(String.valueOf(subInfo.getMnc()));

					network.setNetworkType("");
					network.setRoamingStatus("0");
					network.setSignalStrength(-1);

					network.setSlotno(subInfo.getSimSlotIndex());

					network.setStartTime(String.valueOf(System.currentTimeMillis()));
					network.setEndTime(String.valueOf(System.currentTimeMillis()));
					try{
						if(index==0){
							TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
							telephony.getDeviceId();
							Log.i("Device ID", "" + telephony.getDeviceId(subInfo.getSimSlotIndex()));

							network.setNetworkType(Utility.getNetworkTypeName(telephony.getNetworkType()));
							network.setRoamingStatus(String.valueOf(convertBoolean(telephony.isNetworkRoaming())));
							CellLocation cell =  telephony.getCellLocation();
							String cellLocationString = cell.toString().replace("[", "");
							cellLocationString = cellLocationString.replace("]", "");
							String[] cellLoc = cellLocationString.split(",");
							network.setCellid(cellLoc[0]);
							network.setLoc(cellLoc[1]);
						}
					}catch(Exception e){}
					networkList.add(network);
				}
			}
			return networkList;
		}

	private char[] convertBoolean(boolean networkRoaming) {
			return new char[0];
	}
}
