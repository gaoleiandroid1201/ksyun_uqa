package com.ksyun.cdn.core.task;

import android.util.Log;

import com.google.gson.Gson;
import com.ksyun.cdn.demo.CustomApplication;
import com.ksyun.cdn.core.callback.ITask;
import com.ksyun.cdn.core.callback.MyInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PingTask implements ITask {

    private String url = null;
    private String formatResult;

    public PingTask(String url) {
        this.url = url;
    }

    public MyInterface.OnGetSyncResultListener getSyncResultListener;

    public void setGetSyncResult(MyInterface.OnGetSyncResultListener getSyncResultListener) {
        this.getSyncResultListener = getSyncResultListener;
    }

    public void doIt() {


                StringBuilder builder = new StringBuilder();
                try {
                    long startTime=System.currentTimeMillis();
                    Log.d(CustomApplication.TAG, "PingTask--------startTime-----" + startTime);
                    Process p = Runtime.getRuntime().exec("/system/bin/ping -c 8 -W 1 -i 0.5 " + "www.baidu.com"); // 10.83.50.111  m_strForNetAddress
//                   int statusCode = p.waitFor();
//                    String status;

                    BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
//                    if (statusCode == 0) {
//                        status = "success";
//                    } else {
//                        status = "failed";
//                    }
//                    Log.d(CustomApplication.TAG, "result---ping-----" + status);

                    Log.d(CustomApplication.TAG, "pingInfo--------" + builder.toString());
                    String result = builder.toString();
                    long endTime=System.currentTimeMillis();
                    Log.d(CustomApplication.TAG, "PingTask--------endTime-----" + endTime);
                    Log.d(CustomApplication.TAG, "PingTask--------continue-----" + (endTime-startTime));
                    if (result.indexOf("min/avg/max") > 0) {
                        String result1 = result.split("min/avg/max")[1].trim().split("=")[1];
                        Log.d(CustomApplication.TAG, "result1----------------" + result1);
                        String rtt[] = result1.split("/");
                        String DestIp = result.substring(result.indexOf("(") + 1, result.indexOf(")"));
                        String max = rtt[2];
                        String min = rtt[0];
                        String avg = rtt[1];
                        String mdev = rtt[3].trim().split("ms")[0];
                        Map<String,String> map=new HashMap<String,String>();
                        map.put("DestIp",DestIp);
                        map.put("Max",max);
                        map.put("Min",min);
                        map.put("Avg",avg);
                        map.put("Stdmdev",mdev);
//                        String formatStr = String.format("{"+"DestIp:%s,Max:%s, Min:%s, Avg: %s, Stdmdev:%s", DestIp, max, min, avg, mdev+"}");
                        String formatStr=new Gson().toJson(map).toString();
                        setResult(formatStr);
                        getSyncResultListener.getSyncResult(getResult());
//                        Log.d(CustomApplication.TAG, "min----------------" + min);
//                        Log.d(CustomApplication.TAG, "avg----------------" + avg);
//                        Log.d(CustomApplication.TAG, "max----------------" + max);
//                        Log.d(CustomApplication.TAG, "mdev----------------" + mdev);
//                        Log.d(CustomApplication.TAG, "formatStr----------------" + formatStr);

                    }
                    reader.close();
                    p.destroy();

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
