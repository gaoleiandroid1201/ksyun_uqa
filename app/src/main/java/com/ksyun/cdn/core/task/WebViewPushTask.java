package com.ksyun.cdn.core.task;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.ksyun.cdn.core.callback.IPushTask;

import java.util.Timer;
import java.util.TimerTask;

public class WebViewPushTask implements IPushTask {
    private String url = "http://www.baidugaolei.com";
    Activity activity;
    WebView webView;
    long startTime, endTime;
    private String formatResult;

    public WebViewPushTask(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    public void doIt() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口 。默认为false。
//        settings.setUseWideViewPort(true);// 调整到适合webview大小
//        settings.setLoadWithOverviewMode(true);//概览模式显示网页 默认false
        settings.setAllowFileAccess(true);
        settings.setBlockNetworkImage(false);//是否阻止网络图片数据 默认false
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            public void onLoadResource(WebView view, String url) {
                // 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。 
                Log.d("gaolei", url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            //重写此方法可以让webview处理https请求。
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
                Log.d("gaolei", "onReceivedSslError------------" + error.toString());
            }

            // 在webview里面捕获404等
//            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//                super.onReceivedHttpError(view, request, errorResponse);
//                Log.d("gaolei", "onReceivedHttpError------------" + errorResponse.toString());
//            }

            // onReceivedError方法捕获的是 文件找不到，网络连不上，服务器找不到等问题，并不能捕获 http errors。
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.d("gaolei", "onReceivedError------------" + description);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                startTime = System.currentTimeMillis();
                Log.d("gaolei", "startTime------------" + startTime);
            }

            public void onPageFinished(WebView view, String url) {
                endTime = System.currentTimeMillis();
                Log.d("gaolei", "onPageFinished------costtime------" + (endTime - startTime));
                new Timer().schedule(new TimerTask() {
                    public void run() {
//                        ScreenShotUtil.savePic(ScreenShotUtil.takeScreenShot(activity), "sdcard/abc.png");
                    }
                }, 2000);

            }
        });
    }

    @Override
    public String getResult() {
        return null;
    }

    @Override
    public void setResult(String string) {
        formatResult = string;
    }
}
