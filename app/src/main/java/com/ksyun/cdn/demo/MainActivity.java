package com.ksyun.cdn.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.ksyun.cdn.R;
import com.ksyun.cdn.core.callback.PushServiceInterface;
import com.ksyun.cdn.core.service.TaskService;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class MainActivity extends Activity implements PushServiceInterface.OnGetSyncResultListener {

    public static List<String> logList = new CopyOnWriteArrayList<String>();

    public static MainActivity sMainActivity = null;
    public TextView logView = null;
    private String traceRouteUrl = "www.baidu.com";

    @Override
    public void getSyncResult(String result) {
        Log.d(CustomApplication.TAG, "MainActivity---------result-------" + result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sMainActivity = this;
        logView = (TextView) findViewById(R.id.log);
//        WebView webView = new WebView(this);
        // 设置别名
        findViewById(R.id.set_alias).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.set_alias)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.setAlias(MainActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();*/
                //For test
              /*  Intent service = new Intent(MainActivity.this, TaskService.class);
                startService(service);*/
            }
        });
        // 撤销别名
        findViewById(R.id.unset_alias).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unset_alias)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String alias = editText.getText().toString();
                                MiPushClient.unsetAlias(MainActivity.this, alias, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 设置帐号
        findViewById(R.id.set_account).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.set_account)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.setUserAccount(MainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 撤销帐号
        findViewById(R.id.unset_account).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unset_account)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String account = editText.getText().toString();
                                MiPushClient.unsetUserAccount(MainActivity.this, account, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 设置标签
        findViewById(R.id.subscribe_topic).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.subscribe_topic)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.subscribe(MainActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 撤销标签
        findViewById(R.id.unsubscribe_topic).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.unsubscribe_topic)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String topic = editText.getText().toString();
                                MiPushClient.unsubscribe(MainActivity.this, topic, null);
                            }

                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        // 设置接收消息时间
        findViewById(R.id.set_accept_time).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new TimeIntervalDialog(MainActivity.this, new TimeIntervalDialog.TimeIntervalInterface() {

                    @Override
                    public void apply(int startHour, int startMin, int endHour,
                                      int endMin) {
                        MiPushClient.setAcceptTime(MainActivity.this, startHour, startMin, endHour, endMin, null);
                    }

                    @Override
                    public void cancel() {
                        //ignore
                    }

                })
                        .show();
            }
        });
        // 暂停推送
        findViewById(R.id.pause_push).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MiPushClient.pausePush(MainActivity.this, null);
            }
        });

        findViewById(R.id.resume_push).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MiPushClient.resumePush(MainActivity.this, null);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLogInfo();
        Intent service = new Intent(MainActivity.this, TaskService.class);
        startService(service);

    }

    public void refreshLogInfo() {
        String AllLog = "";
        for (String log : logList) {
            AllLog = AllLog + log + "\n\n";
        }
        logView.setText(AllLog);
    }
    private String getImsi(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSubscriberId();
    }


}
