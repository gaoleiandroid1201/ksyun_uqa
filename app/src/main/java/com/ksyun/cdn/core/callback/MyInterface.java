package com.ksyun.cdn.core.callback;

import java.io.IOException;

/**
 * Created by Administrator on 2015/11/16.
 */
public class MyInterface {


    public  interface NetRequestIterface {
        void changeView(String result, String requestUrl);
        void exception(IOException e, String requestUrl);
//        void showProgressBar(int bytesWritten, int totalSize);
    }
   public interface OnGetSyncResultListener{
       void getSyncResult(String result);
   }
}
