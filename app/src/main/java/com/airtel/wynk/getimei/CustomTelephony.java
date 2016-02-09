package com.airtel.wynk.getimei;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by team5 on 9/2/16.
 */
public class CustomTelephony {
        Context mContext;
        TelephonyManager telephony;
    private String SIM_VARINT = "";
    private String telephonyClassName = "";
    private SharedPreferences pref;
    private int slotNumber_1 = 0;
    private int slotNumber_2 = 1;
    private String slotName_1 = "null";
    private String slotName_2 = "null";
    private String[] listofClass;

    final static String m_IMEI = "getDeviceId";

        public CustomTelephony(Context mContext) {
            try {
                this.mContext = mContext;
                telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                    fetchClassInfo();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * This method returns the class name in which we fetch dual sim details
         */
        public void fetchClassInfo() {
            try {
                telephonyClassName = "android.telephony.TelephonyManager";
                listofClass = new String[]{
                        "com.mediatek.telephony.TelephonyManagerEx",
                        "android.telephony.TelephonyManager",
                        "android.telephony.MSimTelephonyManager",
                        "android.telephony.TelephonyManager"};
                for (int index = 0; index < listofClass.length; index++) {
                    if (isTelephonyClassExists(listofClass[index])) {
                        if (isMethodExists(listofClass[index], "getDeviceId")) {
                            System.out.println("getDeviceId method found");
                            if (!SIM_VARINT.equalsIgnoreCase("")) {
                                break;
                            }
                        }
                        if (isMethodExists(listofClass[index],
                                "getNetworkOperatorName")) {
                            System.out
                                    .println("getNetworkOperatorName method found");
                            break;
                        } else if (isMethodExists(listofClass[index],
                                "getSimOperatorName")) {
                            System.out.println("getSimOperatorName method found");
                            break;
                        }
                    }
                }
                for (int index = 0; index < listofClass.length; index++) {
                    if (slotName_1 == null || slotName_1.equalsIgnoreCase("")) {
                        getValidSlotFields(listofClass[index]);
                        // if(slotName_1!=null || !slotName_1.equalsIgnoreCase("")){
                        getSlotNumber(listofClass[index]);
                    } else {
                        break;
                    }
                }

                JSONObject edit = new JSONObject();
                edit.put("dualsim_telephonycls", telephonyClassName);
                edit.put("SIM_VARINT", SIM_VARINT);
                edit.put("SIM_SLOT_NAME_1", slotName_1);
                edit.put("SIM_SLOT_NAME_2", slotName_2);
                edit.put("SIM_SLOT_NUMBER_1", slotNumber_1);
                edit.put("SIM_SLOT_NUMBER_2", slotNumber_2);
                Log.d("INFO :", edit.toString());
                System.out.println("Done");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        /**
         * Check method with sim variant
         */
        public boolean isMethodExists(String className, String compairMethod) {
            boolean isExists = false;
            try {
                Class<?> telephonyClass = Class.forName(className);
                Class<?>[] parameter = new Class[1];
                parameter[0] = int.class;
                StringBuffer sbf = new StringBuffer();
                Method[] methodList = telephonyClass.getDeclaredMethods();
                for (int index = methodList.length - 1; index >= 0; index--) {
                    sbf.append("\n\n" + methodList[index].getName());
                    if (methodList[index].getReturnType().equals(String.class)) {
                        String methodName = methodList[index].getName();
                        if (methodName.contains(compairMethod)) {
                            Class<?>[] param = methodList[index]
                                    .getParameterTypes();
                            if (param.length > 0) {
                                if (param[0].equals(int.class)) {
                                    try {
                                        SIM_VARINT = methodName.substring(
                                                compairMethod.length(),
                                                methodName.length());
                                        telephonyClassName = className;
                                        isExists = true;
                                        break;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    telephonyClassName = className;
                                    isExists = true;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isExists;
        }



        @TargetApi(Build.VERSION_CODES.M)
        public String[] getIMEIPreLolipop() {
            String[] imeis = new String[2];
            telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                imeis[0] = invokeMethod(telephonyClassName, slotNumber_1, m_IMEI, SIM_VARINT);
                if (TextUtils.isEmpty(imeis[0])) {
                    imeis[0] = telephony.getDeviceId(slotNumber_1);
                }
                imeis[1] = invokeMethod(telephonyClassName, slotNumber_2, m_IMEI, SIM_VARINT);
                if (imeis[1] == null || imeis[1].equalsIgnoreCase("")) {
                    imeis[1]= telephony.getDeviceId(slotNumber_2);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
            return imeis;
        }

       public boolean isTelephonyClassExists(String className) {

            boolean isClassExists = false;
            try {
                Class<?> telephonyClass = Class.forName(className);
                isClassExists = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isClassExists;
        }

        /**
         * Here we are identify sim slot number
         */
        public void getValidSlotFields(String className) {

            String value = null;
            try {
                Class<?> telephonyClass = Class.forName(className);
                Class<?>[] parameter = new Class[1];
                parameter[0] = int.class;
                StringBuffer sbf = new StringBuffer();
                Field[] fieldList = telephonyClass.getDeclaredFields();
                for (int index = 0; index < fieldList.length; index++) {
                    sbf.append("\n\n" + fieldList[index].getName());
                    Class<?> type = fieldList[index].getType();
                    Class<?> type1 = int.class;
                    if (type.equals(type1)) {
                        String variableName = fieldList[index].getName();
                        if (variableName.contains("SLOT")
                                || variableName.contains("slot")) {
                            if (variableName.contains("1")) {
                                slotName_1 = variableName;
                            } else if (variableName.contains("2")) {
                                slotName_2 = variableName;
                            } else if (variableName.contains("" + slotNumber_1)) {
                                slotName_1 = variableName;
                            } else if (variableName.contains("" + slotNumber_2)) {
                                slotName_2 = variableName;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Some device assign different slot number so here code execute
         * to get slot number
         */
        public void getSlotNumber(String className) {
            try {
                Class<?> c = Class.forName(className);
                Field fields1 = c.getField(slotName_1);
                fields1.setAccessible(true);
                slotNumber_1 = (Integer) fields1.get(null);
                Field fields2 = c.getField(slotName_2);
                fields2.setAccessible(true);
                slotNumber_2 = (Integer) fields2.get(null);
            } catch (Exception e) {
                slotNumber_1 = 0;
                slotNumber_2 = 1;
                // e.printStackTrace();
            }
        }

        private String invokeMethod(String className, int slotNumber,
                                    String methodName, String SIM_variant) {
            String value = "";

            try {
                Class<?> telephonyClass = Class.forName(className);
                Constructor[] cons = telephonyClass.getDeclaredConstructors();
                cons[0].getName();
                cons[0].setAccessible(true);
                Object obj = cons[0].newInstance();
                Class<?>[] parameter = new Class[1];
                parameter[0] = int.class;
                Object ob_phone = null;
                try {
                    Method getSimID = telephonyClass.getMethod(methodName
                            + SIM_variant, parameter);
                    Object[] obParameter = new Object[1];
                    obParameter[0] = slotNumber;
                    ob_phone = getSimID.invoke(obj, obParameter);
                } catch (Exception e) {
                    if (slotNumber == 0) {
                        Method getSimID = telephonyClass.getMethod(methodName
                                + SIM_variant, parameter);
                        Object[] obParameter = new Object[1];
                        obParameter[0] = slotNumber;
                        ob_phone = getSimID.invoke(obj);
                    }
                }

                if (ob_phone != null) {
                    value = ob_phone.toString();
                }
            } catch (Exception e) {
                invokeOldMethod(className, slotNumber, methodName, SIM_variant);
            }

            return value;
        }

        public String invokeOldMethod(String className, int slotNumber,
                                      String methodName, String SIM_variant) {
            String val = "";
            try {
                Class<?> telephonyClass = Class
                        .forName("android.telephony.TelephonyManager");
                Constructor[] cons = telephonyClass.getDeclaredConstructors();
                cons[0].getName();
                cons[0].setAccessible(true);
                Object obj = cons[0].newInstance();
                Class<?>[] parameter = new Class[1];
                parameter[0] = int.class;
                Object ob_phone = null;
                try {
                    Method getSimID = telephonyClass.getMethod(methodName
                            + SIM_variant, parameter);
                    Object[] obParameter = new Object[1];
                    obParameter[0] = slotNumber;
                    ob_phone = getSimID.invoke(obj, obParameter);
                } catch (Exception e) {
                    if (slotNumber == 0) {
                        Method getSimID = telephonyClass.getMethod(methodName
                                + SIM_variant, parameter);
                        Object[] obParameter = new Object[1];
                        obParameter[0] = slotNumber;
                        ob_phone = getSimID.invoke(obj);
                    }
                }

                if (ob_phone != null) {
                    val = ob_phone.toString();
                }
            } catch (Exception e) {

            }
            return val;
        }

    @TargetApi(Build.VERSION_CODES.M)
    public String[] getIMEIPostLolipop() {
        String[] imeis = new String[2];
        imeis[0] = telephony.getDeviceId(slotNumber_1);
        if (imeis[0] == null || imeis[0].equalsIgnoreCase("")) {
            imeis[0] = invokeMethod(telephonyClassName, slotNumber_1, m_IMEI, SIM_VARINT);
        }
        imeis[1] = telephony.getDeviceId(slotNumber_2);
        if (imeis[1] == null || imeis[1].equalsIgnoreCase("")) {
            imeis[1] = invokeMethod(telephonyClassName, slotNumber_1, m_IMEI, SIM_VARINT);
        }
        return imeis;
    }
}
