package com.ksyun.cdn.core.callback;


public interface IPushTask {
	
	// 执行任务
	public void doIt();

	// 获取结果，返回json字符串
	public String getResult();
	
	// 打印结果
	public void setResult(String string);

}
