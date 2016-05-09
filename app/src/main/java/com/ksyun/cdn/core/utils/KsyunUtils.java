package com.ksyun.cdn.core.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;


public class KsyunUtils {

    public static int getUid(Context context) {
        int uid = -1;
        try {
            PackageManager pm = context.getPackageManager();
//            ApplicationInfo ai = pm.getApplicationInfo("com.xiaomi.uaq", PackageManager.GET_ACTIVITIES);
            ApplicationInfo ai = pm.getApplicationInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            uid = ai.uid;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return uid;
    }

    // 或许信号强度
    public static int getRSSI() {
        return Global.RSSI;
    }

    // 获取网络名称： WIFI, GSM
    public static String getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getTypeName();
        }
        return "";
    }

    // 获取IP
    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() ) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    // 获取DNS
    public static String getLocalDns(Context context) {
        // This originally looked for all lines containing .dns; but
        // http://code.google.com/p/android/issues/detail?id=2207#c73
        // indicates that net.dns* should always be the active nameservers, so
        // we use those.
        String re1 = "^\\d+(\\.\\d+){3}$";
        String re2 = "^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$";
        try {
            String line;
            Process p = Runtime.getRuntime().exec("getprop");
            InputStream in = p.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                StringTokenizer t = new StringTokenizer(line, ":");
                String name = t.nextToken();
                if (name.indexOf("net.dns") > -1) {
                    String v = t.nextToken();
                    v = v.replaceAll("[ \\[\\]]", "");
                    if ((v.matches(re1) || v.matches(re2)))
                        return v;
                }
            }
        } catch (Exception e) {
            // ignore resolutely
        }
        return "";
    }

    public static String getHostIp(String hostname) {
        try {
            InetAddress IP = InetAddress.getByName(hostname);
            return IP.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getSettingKeyString(String key, Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, "");
    }

    public static void setSettingKeyString(String key, String value, Context context) {
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = setting.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getCurDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            System.out.println("xxxxx" + serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private static String getSdcardPath() {
        String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        return sdcardPath;
    }

    public static String getDefaultHome() {
        String sdcard = getSdcardPath();
        String path = sdcard + "/" + Global.APP_DIR + "/";
        if (mkdir(path))
            return path;
        else
            return Environment.getExternalStorageDirectory().getPath();
    }

    private static boolean mkdir(String path) {
        File file = new File(path);
        if (file.isDirectory())
            return true;
        else {
            boolean creadok = file.mkdirs();
            if (creadok) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean getWifiState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = connectivityManager.getActiveNetworkInfo();
        if (net != null && net.getTypeName().equals("WIFI"))
            return true;
        else
            return false;
    }

    public static boolean isAppOpen(Context context) {
        String state = KsyunUtils.getSettingKeyString(Global.SETTING_APP_OPEN, context);
        if (state != null && state.equals("true"))
            return true;
        else
            return false;
    }

    public static boolean download(String uri, String filePath) {
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            readStreamToFile(conn.getInputStream(), filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void readStreamToFile(InputStream inStream, String filePath) throws Exception {
        File file = new File(filePath + ".wei");
        RandomAccessFile outStream = new RandomAccessFile(file, "rw");
        outStream.seek(0);
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        file.renameTo(new File(filePath));
        return;
    }

    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    System.out.println(info[i].getState());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }


}
