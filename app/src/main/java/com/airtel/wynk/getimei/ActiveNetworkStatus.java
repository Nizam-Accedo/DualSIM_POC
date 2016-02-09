package com.airtel.wynk.getimei;

/**
 * Created by arun.chand on 27-01-2016.
 */
public class ActiveNetworkStatus {
    private String carrierName;
    private String imsi;
    private String cellid;
    private String loc;
    private String MCC;
    private String MNC;
    private String networkType;
    private String roamingStatus;
    private int signalStrength;
    private int slotno;
    private String startTime;
    private String endTime;

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public void setCellid(String cellid) {
        this.cellid = cellid;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setMCC(String MCC) {
        this.MCC = MCC;
    }

    public void setMNC(String MNC) {
        this.MNC = MNC;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public void setRoamingStatus(String roamingStatus) {
        this.roamingStatus = roamingStatus;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    public void setSlotno(int slotno) {
        this.slotno = slotno;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
