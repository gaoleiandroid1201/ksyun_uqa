package com.ksyun.cdn.core.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ksyun.cdn.demo.CustomApplication;
import com.ksyun.cdn.core.callback.MyInterface;
import com.ksyun.cdn.core.callback.NetRequest;
import com.ksyun.cdn.core.entity.TaskEntity;
import com.ksyun.cdn.core.task.TaskManager;
import com.ksyun.cdn.core.utils.CommonUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskService extends IntentService implements MyInterface.NetRequestIterface {
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
        netRequest = new NetRequest(this, this);
        netRequest.httpRequest(null, CommonUrl.test_url);
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
                    String metric = "download";
                    String url = dataContentList.get(0).getArg2();
                    String tid = dataContentList.get(0).getTid() + "";
                    String stid = dataContentList.get(0).getStid() + "";
                    new TaskManager().executeTask(metric, tid, stid, url, getApplicationContext());


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void exception(IOException e, String requestUrl) {
    }


}
