package com.ksyun.cdn.core.task;

import android.util.Log;


import com.ksyun.cdn.demo.CustomApplication;
import com.ksyun.cdn.core.callback.IPushTask;
import com.ksyun.cdn.core.callback.PushServiceInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpPushTask implements IPushTask {


    private long time_total;
    private long time_namelookup;
    private long time_connect;
    private int ssl_verify_result = -1;
    private int http_code = -1;
    private String url_effective;
    private String remote_ip, remote_port = "80";
    private URLConnection conn = null;
    private boolean isHttps = false;
    private int statTcp = 1;
    private String formatResult;

    private PushServiceInterface.OnGetSyncResultListener getSyncResultListener;

    public void setGetSyncResult(PushServiceInterface.OnGetSyncResultListener getSyncResultListener) {
        this.getSyncResultListener = getSyncResultListener;
    }

    public HttpPushTask(String url) {
        url_effective = url;
        if (url.startsWith("https")) {
            isHttps = true;
            ssl_verify_result = 1;
        }
    }

    public void doIt() {

                openRequestConn();
                parseDNS();
                setResult("");
                getSyncResultListener.getSyncResult(getResult());

    }

    public String getResult() {

        return formatResult;
    }
    @Override
    public void setResult(String string) {
        String str = "{'time_total':'%s', 'time_namelookup':'%s', 'time_connect':'%s', 'ssl_verify_result':'%s', 'http_code':'%s', 'url_effective':'%s', 'remote_ip':'%s', 'remote_port':'%s'}";
        formatResult = String.format(str, time_total, time_namelookup, time_connect, ssl_verify_result, http_code, url_effective, remote_ip, remote_port);
    }

    private void parseDNS() {
        try {
            long parseDNSBegin = System.currentTimeMillis();
            InetAddress address = InetAddress.getByName(url_effective);
            remote_ip = address.getHostAddress();
            long parseDNSFinish = System.currentTimeMillis();
            time_namelookup = parseDNSFinish - parseDNSBegin;
        } catch (UnknownHostException e) {
            Log.d(CustomApplication.TAG, e.getMessage());
        }
    }

    private void openRequestConn() {
        try {
            long connectBegin = System.currentTimeMillis();
            URL url = new URL("http://www.baidu.com");
            conn = url.openConnection();
            if (isHttps) {
                SSLContext sslcontext;
                try {
                    sslcontext = SSLContext.getInstance("TLS");
                    sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);
                    ((HttpsURLConnection) conn).setSSLSocketFactory(sslcontext.getSocketFactory());
                    ssl_verify_result = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    ssl_verify_result = 0;
                }
            }
            http_code = ((HttpURLConnection) conn).getResponseCode();
            long connectFinish = System.currentTimeMillis();
            time_connect = connectFinish - connectBegin;

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                // System.out.println(lines);
            }
            long requestFinish = System.currentTimeMillis();
            time_total = requestFinish - connectBegin;
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getUrl() {
        return url_effective;
    }

    private static TrustManager myX509TrustManager = new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    };

}
