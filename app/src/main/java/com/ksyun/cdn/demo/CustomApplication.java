package com.ksyun.cdn.demo;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.ksyun.cdn.core.PushMsgReceiver;
import com.ksyun.cdn.core.utils.KsyunClient;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class CustomApplication extends Application {

    // user your appid the key.
    private static final String APP_ID = "2882303761517446980";
    // user your appid the key.
    private static final String APP_KEY = "5781744690980";

    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "KSYUN";

    private static PushMsgReceiver.DemoHandler handler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        // TODO 这里的Demo仅做参考，客户真实集成场景，需要做网络连接判断，如果没有网络，后续应监听网络状态改变广播，进行初始化
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        KsyunClient.init(getApplicationContext());

        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(this, newLogger);
        if (handler == null)
            handler = new PushMsgReceiver.DemoHandler(getApplicationContext());
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static PushMsgReceiver.DemoHandler getHandler() {
        return handler;
    }
}