package com.example.mvcidemo;

import android.widget.TextView;

public class MainModel {

	public void loadData(TextView tv_view) {
		tv_view.setText("MVC模式在Android中的应用，Model获取数据");
	}

	/***
	 * 网络请求
	 * 
	 * @return 返回请求数据
	 */
	public Object httpData() {
		return "网络请求返回数据";
	}
}
