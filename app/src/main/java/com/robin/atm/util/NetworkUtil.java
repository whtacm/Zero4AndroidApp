package com.robin.atm.util;

import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by robin on 4/20/18.
 */

public class NetworkUtil {
    static String mac = "02:00:00:00:00:00";

    public static String getMac() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // < 6.0
            return getLocalMacAddressFromWifiInfo();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            // < 7.0
            return getMacAddress();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (!TextUtils.isEmpty(getMacAddress())) {
                return getMacAddress();
            } else if (!TextUtils.isEmpty(getMachineHardwareAddress())) {
                return getMachineHardwareAddress();
            } else {
                return getLocalMacAddressFromBusybox();
            }
        }


        return mac;
    }

    /**
     * @return
     */
    private static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6,
                    result.length() - 1);
            result = Mac;
        }
        return result;
    }

    /**
     * @param cmd
     * @param filter
     * @return
     */
    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null
                    && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return
     */
    private static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /**
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    /**
     * @return
     */
    private static String getMacAddress() {
        String str = "";
        String macSerial = "";

        Process process = null;
        try {
            process = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader reader = new LineNumberReader(ir);

            for (; null != str; ) {
                str = reader.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return macSerial;
        }

        return mac;
    }

    /**
     * @param fileName
     * @return
     * @throws IOException
     */
    private static String loadFileAsString(String fileName) throws IOException {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    /**
     * @param reader
     * @return
     * @throws IOException
     */
    private static String loadReaderAsString(FileReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    /**
     * @return
     */
    private static String getLocalMacAddressFromWifiInfo() {
        if (isAccessWifiStateAuthorized()) {
            WifiManager wifi = (WifiManager) Utils.getApp().getApplicationContext()
                    .getSystemService(Utils.getApp().WIFI_SERVICE);
            WifiInfo wifiInfo = wifi.getConnectionInfo();
            return wifiInfo.getMacAddress();
        } else {
            return mac;
        }
    }

    /**
     * @return
     */
    private static boolean isAccessWifiStateAuthorized() {
        if (PackageManager.PERMISSION_GRANTED == Utils.getApp().getApplicationContext()
                .checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            return true;
        } else {
            return false;
        }
    }

}
