package com.ksyun.cdn.core.task;

import android.util.Log;


import com.ksyun.cdn.core.callback.ITask;
import com.ksyun.cdn.core.callback.MyInterface;
import com.ksyun.cdn.core.entity.TracerouteEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TraceRouteTask implements ITask {
    // 目的IP
    public static String destIp = "";
    // 目的url
    private String url = "";
    // ping耗时
    private float elapsedTime;
    //    public static String pingHost = "220.181.112.244";
    // 都是一些字符串 用于parse 用的
    private static final int maxTtl = 30;
    private static final String FROM_PING = "From";
    private static final String SMALL_FROM_PING = "from";
    private String routeIp;
    private String formatResult;
    // 存放结果集的tarces
    private List<TracerouteEntity> traces = new ArrayList<TracerouteEntity>();

    public TraceRouteTask(String url) {

    }
    public MyInterface.OnGetSyncResultListener getSyncResultListener;

    public void setGetSyncResult(MyInterface.OnGetSyncResultListener getSyncResultListener) {
        this.getSyncResultListener = getSyncResultListener;
    }
    public void doIt() {

        try {
            InetAddress address = InetAddress.getByName("www.baidu.com");
            destIp = address.getHostAddress();
        } catch (Exception e) {
            System.out.print(e.toString());
        }

        boolean endTrace = false;
        String ret = null;
        for (int ttl = 1; ttl < maxTtl; ttl++) {
            final int index = ttl;
            if (!endTrace) {
                ret = doTraceOne(index);
                Log.d("KSYUN", "ttl-----------------" + index);
                Log.d("KSYUN", "doTraceOne(ttl)-------ip----------" + ret);
                Log.d("KSYUN", "doTraceOne(ttl)-------routeIp----------" + routeIp);
                if (ret.equals(routeIp) && !ret.equals("") && ret != null) {
                    endTrace = true;
                    traces.remove(traces.size() - 1);
                    setResult("");
                    getSyncResultListener.getSyncResult(getResult());
                }
                routeIp = ret;
            }
        }
    }
    private String
    doTraceOne(int ttl) {
        String res = "";
        try {
            res = launchPing(ttl);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        TracerouteEntity trace;
        String ip = parseTraceIP(res);
        trace = new TracerouteEntity(ip, elapsedTime, ttl);
        if(ip.length()>1) {
            traces.add(trace);
        }
        return ip;
    }

    private String launchPing(int ttl) throws IOException {
        String result = "";
        try {
            long startTime = System.currentTimeMillis();
            // 采用ping方法, -t对应的 ttl的值初始为1，然后不断加1来获取对应经过的路由IP
            String format = "ping -c 1  -t %d %s";
            String command = String.format(format, ttl, destIp);
            Process p = Runtime.getRuntime().exec(command);
            long begin = System.currentTimeMillis();
            //等任务执行完成
            int status = p.waitFor();
            long end = System.currentTimeMillis();
//            Log.d("KSYUN", "p.waitFor()------cost-time----------" + (end-begin));

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
//                Log.d("KSYUN", "line-----------------" + line);
                // 可能返回 From|from
                if (line.contains(FROM_PING) || line.contains(SMALL_FROM_PING)) {
                    result = line;
                }
            }

            elapsedTime = (System.currentTimeMillis() - startTime);
//            Log.d("KSYUN", "result-----------------" + result);
//            Log.d("KSYUN", "elapsedTime-----------------" + elapsedTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String parseTraceIP(String s) {
        String ip = "";
        if (s.contains(FROM_PING) || s.contains(SMALL_FROM_PING)) {
            ip = matchIp(s);
        }

        return ip;
    }

    private String matchIp(String s) {
        String ip = "(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(s);

        if (matcher.find())
            return matcher.group();
        return "";
    }
    public String getResult() {
        return formatResult;
    }

    public void setResult(String string) {
        Log.d("KSYUN", "traces.size()-----------------" + traces.size());
        String s = "[";
        for (TracerouteEntity trace : traces) {
            s += trace.toJson() + ",";
        }
        formatResult=s + "]";
    }
}