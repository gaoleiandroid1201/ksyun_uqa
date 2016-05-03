package com.ksyun.cdn.core.task;

import android.content.Context;
import android.util.Log;


import com.ksyun.cdn.demo.CustomApplication;
import com.ksyun.cdn.core.callback.MyInterface;
import com.ksyun.cdn.core.callback.NetRequest;
import com.ksyun.cdn.core.utils.CommonUrl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/20.
 */
public class TaskManager implements MyInterface.NetRequestIterface, MyInterface.OnGetSyncResultListener {

    public static final String TASK_TYPE_PING = "ping";
    public static final String TASK_TYPE_TRACEROUTE = "traceroute";
    public static final String TASK_TYPE_HTTP = "http";
    public static final String TASK_TYPE_DNS = "dns";
    public static final String TASK_TYPE_DOWNLOAD = "download";
    private NetRequest netRequest = null;
    private Map<String, Object> map = new HashMap<String, Object>();
    private String tid, stid, metric;

    @Override
    public void getSyncResult(String result) {
        Log.d(CustomApplication.TAG, "TaskManager---------result-------" + result);
        map.put("tid", tid);
        map.put("stid", stid);
        map.put("metric", metric);
        map.put("data", result);
        netRequest.httpRequest(map, CommonUrl.task_post_url);

    }

    public void executeTask(String metric, String tid, String stid, String url, Context context) {
        if (netRequest == null) {
            netRequest = new NetRequest(this, context);
        }
        this.metric = metric;
        this.tid = tid;
        this.stid = stid;
        switch (metric.toLowerCase()) {

            case TASK_TYPE_PING:
                PingTask pingTask = new PingTask(url);
                pingTask.setGetSyncResult(this);
                pingTask.doIt();
                break;
            case TASK_TYPE_TRACEROUTE:
                TraceRouteTask traceRouteTask = new TraceRouteTask(url);
                traceRouteTask.setGetSyncResult(this);
                traceRouteTask.doIt();
                break;
            case TASK_TYPE_HTTP:
                HttpTask httpTask = new HttpTask(url);
                httpTask.setGetSyncResult(this);
                httpTask.doIt();
                break;
            case TASK_TYPE_DNS:
                DNSParseTask dnsParseTask = new DNSParseTask(url);
                dnsParseTask.setGetSyncResult(this);
                dnsParseTask.doIt();
                break;
            case TASK_TYPE_DOWNLOAD:
                DownloadTask downloadTask = new DownloadTask(url);
                downloadTask.setGetSyncResult(this);
                downloadTask.doIt();
                break;
        }

    }

    @Override
    public void changeView(String result, String requestUrl) {
        switch (requestUrl) {

            case CommonUrl.task_post_url:
                Log.d(CustomApplication.TAG, "result--------task_post_url--------" + result);
                break;
        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
    }
}
