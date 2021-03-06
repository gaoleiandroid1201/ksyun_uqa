package com.ksyun.cdn.core.task;

import android.util.Log;

import com.google.gson.Gson;
import com.ksyun.cdn.demo.CustomApplication;
import com.ksyun.cdn.core.callback.IPushTask;
import com.ksyun.cdn.core.callback.PushServiceInterface;


import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class DNSParsePushTask implements IPushTask {

    private String formatResult;
    private PushServiceInterface.OnGetSyncResultListener getSyncResultListener;
    private String url;
    public void setGetSyncResult(PushServiceInterface.OnGetSyncResultListener getSyncResultListener) {
        this.getSyncResultListener = getSyncResultListener;
    }

    Map<String, Object> map = new HashMap<String, Object>();

    public DNSParsePushTask(String url) {
        this.url=url;
    }

    public void doIt() {

                try {

                    InetAddress ia = InetAddress.getByName("localhost");
                    map.put("Server", ia);
                    System.out.println("Host Address = " + ia.getHostAddress());
                    Lookup lookup = new Lookup(url, Type.A);
                    lookup.run();
                    if (lookup.getResult() != Lookup.SUCCESSFUL) {
                        Log.d(CustomApplication.TAG, "ERROR----------------" + lookup.getErrorString());
                        return;
                    }
                    Record[] answers = lookup.getAnswers();
                    int i = 1;
                    for (Record rec : answers) {
                        map.put("Address" + i, rec.toString().split("A")[1].trim());
                        Log.d(CustomApplication.TAG, "rec.toString()----------------" + rec.toString().split("A")[1]);
                        i++;
                    }
                    String formattedStr = map.toString();
                    Log.d(CustomApplication.TAG, "map.toString()----------------" + formattedStr);
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(map);

                    setResult(jsonStr);
                    getSyncResultListener.getSyncResult(getResult());
                } catch (Exception e) {
                    e.printStackTrace();
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

}
