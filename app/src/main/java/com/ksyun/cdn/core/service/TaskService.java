package com.ksyun.cdn.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ksyun.cdn.core.utils.Global;
import com.ksyun.cdn.core.utils.KsyunUtils;
import com.ksyun.cdn.demo.CustomApplication;
import com.ksyun.cdn.core.callback.PushServiceInterface;
import com.ksyun.cdn.core.callback.NetRequest;
import com.ksyun.cdn.core.entity.TaskEntity;
import com.ksyun.cdn.core.task.TaskManager;
import com.ksyun.cdn.core.utils.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskService extends IntentService implements PushServiceInterface.NetRequestInterface {
    private NetRequest netRequest;
    private List<TaskEntity.DataContent> dataContentList = new ArrayList<TaskEntity.DataContent>();

    public TaskService() {
        super("TaskService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if (KsyunUtils.isConnectingToInternet(getApplicationContext())) {
            netRequest = new NetRequest(this, this);
            netRequest.httpRequest(null, CommonUrl.test_url);
        }
//        new TaskManager().executeTask("dns", "", "", "http://sfvbook.bj.bcebos.com/abc.txt", getApplicationContext());
    }

    @Override
    public void changeView(String result, String requestUrl) {
        switch (requestUrl) {
            case CommonUrl.test_url:
                try {
                    JSONObject object = new JSONObject(result);
                    Log.d(CustomApplication.TAG, "result--------task_get_url--------" + result);
                    String returnStatus = object.getString("msg");
                    dataContentList = new Gson().fromJson(
                            object.getString("data"),
                            new TypeToken<List<TaskEntity.DataContent>>() {
                            }.getType());
//                    Log.d(CustomApplication.TAG, "dataContentList.size()--------" + dataContentList.size());
                    Log.d(CustomApplication.TAG, "dataContentList.get(0).getMetric()--------" + dataContentList.get(0).getMetric());
                    Log.d(CustomApplication.TAG, "dataContentList.get(0).getArg2()--------" + dataContentList.get(0).getArg2());
//                    Log.d(CustomApplication.TAG, "Global.USER----------------------" + Global.USER);
//                    Log.d(CustomApplication.TAG, "Global.IMEI----------------------" + Global.IMEI);
//                    String metric = dataContentList.get(0).getMetric();
                    //TODO 这里写死了返回结果，实际需要测试返回的类型
                    String metric = "download";
//                    String url = dataContentList.get(0).getArg2();
                    //TODO 目前临时写下载地址，方便测试
//                    String url = "http://sfvbook.bj.bcebos.com/movie/E3%202015%20-%20Street%20Fighter%20V%20-%205%20-%20Justin%20Wong%20Vs%20ComboFiend%20Exhibition%20%231.mp4";
                    String url = "http://www.baidu.com";
                    String tid = dataContentList.get(0).getTid() + "";
                    String stid = dataContentList.get(0).getStid() + "";
                    new TaskManager().executeTask(metric, tid, stid, url, getApplicationContext());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
        Log.e(Global.TAG, "KSYUN Exception : " + e.toString() + ", request url = " + requestUrl);
    }


}
