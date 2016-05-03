package com.ksyun.cdn.core.task;

import android.text.TextUtils;

import com.ksyun.cdn.core.callback.ITask;
import com.ksyun.cdn.core.callback.MyInterface;


public class DownloadTask implements ITask {
    private String mUrl;
    private String formatResult;
    public MyInterface.OnGetSyncResultListener getSyncResultListener;
    public static final String KEY_TOTAL_TIME = "total_time";
    public static final String KEY_NAME_LOOK_UP_TIME = "name_look_up_time";
    public static final String KEY_CONNECT_TIME = "connect_time";
    public static final String KEY_APP_CONNECT_TIME = "app_connect_time";
    public static final String KEY_PRE_TRANSFER_TIME = "pre_transfer_time";
    public static final String KEY_REDIRECT_TIME = "redirect_time";
    public static final String KEY_START_TRANSFER_TIME = "statr_transfer_time";
    public static final String KEY_REDIRECT_COUNT = "redirect_count";
    public static final String KEY_SSL_VERIFY_RESULT = "ssl_verify_result";
    public static final String KEY_RESPONSE_CODE = "response_code";
    public static final String KEY_SPEED_DOWNLOAD = "speed_download";
    public static final String KEY_DOWNLOAD_SIZE = "download_size";
    public static final String KEY_EFFECTIVE_URL = "effective_url";

    static {
        System.loadLibrary("testlibrary");
    }

    public void setGetSyncResult(MyInterface.OnGetSyncResultListener getSyncResultListener) {
        this.getSyncResultListener = getSyncResultListener;
    }

    public DownloadTask(String url) {
        this.mUrl = url;
    }

    @Override
    public void doIt() {
        String contentString = downloadUrl(mUrl);
        if (!TextUtils.isEmpty(contentString)) {
            setResult(contentString);
            getSyncResultListener.getSyncResult(getResult());
        }
    }

    @Override
    public String getResult() {
        return formatResult;
    }

    @Override
    public void setResult(String string) {
        formatResult = string;
    }

    public native String downloadUrl(String url);
}
