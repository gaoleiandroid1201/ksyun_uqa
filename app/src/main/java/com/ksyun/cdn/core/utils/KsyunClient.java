package com.ksyun.cdn.core.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ksyun.cdn.demo.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class KsyunClient {

    private static final String OS_ANDROID = "android";
    private static final String SDK_VERSION = "1.0.0";

    public static void init(Context context) {
        long currentTime = System.currentTimeMillis();
        PhoneInfoUtils.PhoneInfo info = PhoneInfoUtils.getPhoneInfo(context);
        String platform = getOS();
        String os_version = info.getBuild_version();
        String package_name = info.getPkg();
        String manufacturer = info.getManufacturer();
        String dev_model = info.getModel();
        String date = getCurrentDate();
        String imei = info.getImei();
        String net_type = info.getConnect_type();
        String net_des = info.getOperation_type();
        String sdk_ver = getSdkVersion();

//        Global.IMEI = getIMEI(context);
//        Global.OS = getAndroidVersion();
//        Global.MODEL = getModel();
//        Global.HOLMES_VERSION = getVersionName(context);
////        Global.UID = getUid(context);
//        Log.d("gaolei", "Global.UID---------------------" + Global.UID);
        //TODO 不应该依赖MainActivity,需要解耦,这里为什么要取Token?
//        KsyunClient.getToken(context, MainActivity.sMainActivity);
        Log.d(Global.TAG, "init cost time = " + String.valueOf(System.currentTimeMillis() - currentTime));
        Log.d(Global.TAG, "basic value :  " + "platform = "+platform +",os_version = "+os_version+",package_name = "+package_name+",manufacturer = "+manufacturer+",dev_model = "+dev_model+",date = "+date+",imei = "+imei+",net_type = "+net_type +",net_des = "+net_des+",sdk_ver = "+sdk_ver);
    }

    private static String getSdkVersion() {
        return SDK_VERSION;
    }

    private static String getCurrentDate() {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA).format(new Date());
    }

    private static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    private static String getOS() {
        return OS_ANDROID;
    }

    private static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    private static String getModel() {
        return Build.MODEL;
    }

    private static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release + ")";
    }

    private static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo;
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static Account getXiaomiAccount(Context context) {
        Account account = null;
        AccountManager am = AccountManager.get(context);
        Account[] accounts = am.getAccountsByType(Global.AID);
        if (accounts.length > 0) {
            account = accounts[0];
        }
        return account;
    }

    private static void getToken(Context context, Activity activity) {
        Account account = KsyunClient.getXiaomiAccount(context);
        if (account == null) {
            AccountManager.get(context).addAccount(Global.AID, Global.SID, null, null, activity, null, null);
        } else {

            class TokenTask implements Runnable {
                private Context mContext;
                private Activity mActivity;

                public TokenTask(Context context, Activity activity) {
                    mContext = context;
                    mActivity = activity;
                }

                public void run() {
                    String token = null;
                    Account account = null;
                    try {
                        account = KsyunClient.getXiaomiAccount(mContext);
                        AccountManager am = AccountManager.get(mContext);
                        AccountManagerFuture<Bundle> future = am.getAuthToken(account, Global.SID, null, mActivity, null, null);
                        token = future.getResult().getString(AccountManager.KEY_AUTHTOKEN);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.i(Global.TAG, "Name:" + account.name);
                    Log.i(Global.TAG, "Access token retrieved:" + token);
                    Global.USER = account.name;
                    Global.TOKEN = token;
                }
            }

            TokenTask tokenTask = new TokenTask(context, activity);
            new Thread(tokenTask).start();
        }
    }

}
