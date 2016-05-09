package com.ksyun.cdn.core.callback;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ksyun.cdn.demo.CustomApplication;
import com.ksyun.cdn.core.utils.KsyunUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class NetRequest {
    private PushServiceInterface.NetRequestInterface netRequestInterface;
    private Context context;

    public NetRequest(PushServiceInterface.NetRequestInterface netRequestInterface, Context context) {
        this.netRequestInterface = netRequestInterface;
        this.context = context;
    }

    public void httpRequest(Map<String, Object> map, final String requestUrl) {
		if (!KsyunUtils.isConnectingToInternet(context)) {
			return;
		}
        final long startTime = System.currentTimeMillis();
        Log.d(CustomApplication.TAG, "NetRequest--------startTime-----" + startTime);
        try {
            OkHttpClient mOkHttpClient = new OkHttpClient();
//            FormEncodingBuilder builder = new FormEncodingBuilder();

            Request request;
            if (map != null) {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Log.d(CustomApplication.TAG, "post----------------" + new Gson().toJson(map));
                RequestBody  body = RequestBody.create(JSON, new Gson().toJson(map));
                request = new Request.Builder()
                        .url(requestUrl)
                        .post(body)
                        .build();
            } else {
                request = new Request.Builder()
                        .url(requestUrl)
                        .build();
            }


            mOkHttpClient.setConnectTimeout(5000, TimeUnit.MILLISECONDS);
            setCertificates(mOkHttpClient, context.getAssets().open("uaq.crt"));

            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    netRequestInterface.exception(e, requestUrl);
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String result = response.body().string();
                    netRequestInterface.changeView(result, requestUrl);
                    long endTime = System.currentTimeMillis();
                    Log.d(CustomApplication.TAG, "NetRequest--------result-----" + result);
                    Log.d(CustomApplication.TAG, "NetRequest--------endTime-----" + endTime);
                    Log.d(CustomApplication.TAG, "NetRequest--------cost-----" + (endTime - startTime));
                }
            });
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    private void setCertificates(OkHttpClient mOkHttpClient, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
