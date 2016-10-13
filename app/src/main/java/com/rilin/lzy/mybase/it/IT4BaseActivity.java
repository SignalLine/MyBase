package com.rilin.lzy.mybase.it;

public interface IT4BaseActivity {

	/**
	 * 设置Viewid;
	 * 
	 * @return
	 */
	int setViewId();

	/**
	 * 初始化view
	 * 
	 * @return
	 */
	void initView();

	/**
	 * 初始化数�?
	 * 
	 * @return
	 */
	void initData();

	/**
	 * 请求数据
	 */
	void requestData();

	/**
	 * 释放资源
	 */
	void destroyResource();

}
