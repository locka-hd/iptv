package com.iptv.mktech.iptv.utils;

/**
 * Created by Administrator on 2017/10/9.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

public class NetworkManager {
    public static final String TAG = NetworkManager.class.getSimpleName();

    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {
        Log.e("hehui", "1111111111111111111");
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                Log.e("hehui", "isNetworkAvailable: Unavailable");
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            Log.i("NetWorkState", "Available");
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("hehui", "isNetworkAvailable Exception:" + e.getMessage());
        }

        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedNetType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
            return ConnectivityManager.TYPE_ETHERNET;
        }
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return activeInfo.getType();
        }
        return -1;
    }

 /*   public String getMacAddress(){
        EthernetManager ethManager = (EthernetManager) this.getSystemService("ethernet");
        return ethManager.getMacAddr()==null?"":ethManager.getMacAddr();
    }*/


    public static String getEthMacAddress() {
        try {
            return loadFileAsString("/sys/class/net/eth0/address").toUpperCase(Locale.ENGLISH).substring(0, 17);
        } catch (IOException e) {
            return null;
        }
    }

    public static String loadFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }


//    public static String getMacAddr(String ifName) {
//        String macAddr = "00:00:00:00:00:00";
//        Enumeration<NetworkInterface> en = null;
//        try {
//            en = NetworkInterface.getNetworkInterfaces();
//            while (en.hasMoreElements()) {
//                NetworkInterface ni = en.nextElement();
//                if (ni.getName().startsWith(ifName)) {
//                    System.out.println("eth0  status  " + ni.isUp());
//                    byte[] mac = ni.getHardwareAddress();
//                    StringBuffer sb = new StringBuffer();
//                    for (int i = 0; i < mac.length; i++) {
//                        if (i != 0) {
//                            sb.append(":");
//                        }
//                        String s = Integer.toHexString(mac[i] & 0xFF);
//                        sb.append(s.length() == 1 ? 0 + s : s);
//                    }
//                    macAddr = sb.toString().toUpperCase();
//                }
//            }
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//
//        return macAddr;
//    }


    public static final String getWiFiMacAddress(Context context) {
        String wifiAdress = "";
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (info == null || info.getMacAddress() == null || info.getMacAddress().isEmpty()) {
                wifiAdress = "";
            } else {
                wifiAdress = info.getMacAddress();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
        return wifiAdress;
    }

    public static String getMac(Context context) {
        String ethAddress = getEthMacAddress();
        if (ethAddress != null && !ethAddress.equals("") && ethAddress.length() > 0) {
            return ethAddress;
        } else {
            String wifiAddress = getWiFiMacAddress(context);
            if (wifiAddress != null && !wifiAddress.equals("") && wifiAddress.length() > 0) {
                return wifiAddress;
            }
        }
        return null;
    }

}
