package com.ksyun.cdn.core.utils;

public class Global {
	// app相关变量
    public static final String APP_ID = "2882303761517446980";
    public static final String APP_KEY = "5781744690980";
	public static final String SID = "uaq-app";
	public static final String AID = "com.xiaomi";
    public static final String TAG = "com.ksyun.cdn";

	// 设置存储
	public static final String SETTING_APP_OPEN = "app_open";    //采集开关
	public static final String SETTING_WIFT_TYPE = "s_wifi_type";
	public static final String SETTING_TAG_AREA = "tag_area";
	public static final String SETTING_TAG_ISP = "tag_isp";
	public static final String SETTING_TAG_A_I = "tag_a_i";
	public static final String SETTING_SYS_INFO = "sys_info";
	public static final String SETTING_TASK_STATUS = "task_status";
	public static final String SETTING_UPDATE = "updatetime";

	// 流量套餐
	public static final int SUIT_0M = 0; // 关闭
	public static final int SUIT_10M = 10 * 1024 * 1024; // 10M
	public static final int SUIT_50M = 50 * 1024 * 1024; // 50M
	public static final int SUIT_100M = 100 * 1024 * 1024; // 100M
	public static final int SUIT_NA = 1000 * 1024 * 1024; // 无限制

	// APP目录
	public static final String APP_DIR = "/holmes";

	public static String IMEI = "";
	public static String MODEL = "";
	public static String OS = "";
	public static String HOLMES_VERSION = "";
	public static int RSSI = 0;
	public static int UID = -1;
	public static String USER = "";
	public static String TOKEN = "";
	public static boolean SERVICE_START = false;
	


		
}
