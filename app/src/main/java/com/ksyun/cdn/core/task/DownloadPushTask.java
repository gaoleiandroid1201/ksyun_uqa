package com.ksyun.cdn.core.task;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.ksyun.cdn.core.callback.IPushTask;
import com.ksyun.cdn.core.callback.PushServiceInterface;
import com.ksyun.cdn.core.utils.Global;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


public class DownloadPushTask implements IPushTask {
    private String mUrl;
    private String formatResult;
    private PushServiceInterface.OnGetSyncResultListener getSyncResultListener;
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
    private HashMap<String, String> mHeaderMaps = new HashMap<>();

    static {
//        System.loadLibrary("ksyunpushtask");
        System.loadLibrary("testlibrary");
    }

    public void setGetSyncResult(PushServiceInterface.OnGetSyncResultListener getSyncResultListener) {
        this.getSyncResultListener = getSyncResultListener;
    }

    public DownloadPushTask(String url) {
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

    public void postEventFromNative(int what, int arg1, int arg2, byte[] msg) {
        try {
            if (msg != null) {
                String result = new String(msg, "UTF-8");
                String[] resultArray = result.split("\\r\\n\\r\\n");
                String mHeaderStringWithState = resultArray[0];
                String[] headerWithStateArray = mHeaderStringWithState.split("\\r\\n");
                if (mHeaderMaps.size() > 0) {
                    mHeaderMaps.clear();
                }
                // Dismiss state info
                for (int i = 1; i < headerWithStateArray.length; i++) {
                    String headerWithValue = headerWithStateArray[i];
                    String[] headerWithValueArray = headerWithValue.split(":");
                    mHeaderMaps.put(headerWithValueArray[0], headerWithValueArray[1]);
                }
                String formatStr = new Gson().toJson(mHeaderMaps);
                Log.i(Global.TAG, "postEventFromNative, what = " + what + ",arg1 = " + arg1 + ",arg2 = " + arg2 + ",msg = " + result);
            }
//            FileUtils.writeSDFile(msg, "downloadtask.txt", false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
