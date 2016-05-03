package com.ksyun.cdn.core.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class TaskEntity {
    private String msg;
    private List<DataContent> data = new ArrayList<DataContent>();

    public static class DataContent {
        private String metric, arg1, arg2, ts, area, isp;
        private long tid, stid, task_step;

        public long getStid() {
            return stid;
        }

        public void setStid(long stid) {
            this.stid = stid;
        }

        public long getTid() {
            return tid;
        }

        public void setTid(long tid) {
            this.tid = tid;
        }

        public String getMetric() {
            return metric;
        }

        public void setMetric(String metric) {
            this.metric = metric;
        }

        public String getArg1() {
            return arg1;
        }

        public void setArg1(String arg1) {
            this.arg1 = arg1;
        }

        public String getArg2() {
            return arg2;
        }

        public void setArg2(String arg2) {
            this.arg2 = arg2;
        }

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public long getTask_step() {
            return task_step;
        }

        public void setTask_step(long task_step) {
            this.task_step = task_step;
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
