package com.rilin.lzy.mybase.net;

import java.io.File;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * �������󹤾�
 * 
 * @author sks
 * 
 */
public class NetTaskUtils {

	private static NetTaskUtils utils;

	private final String Default_DownPath = null;

	private HttpUtils client;
	{
		client = new HttpUtils();
		client.configRequestRetryCount(3);
		client.configRequestThreadPoolSize(8);
	}

	private NetTaskUtils() {

	}

	public static NetTaskUtils init() {
		if (utils == null)
			utils = new NetTaskUtils();
		return utils;

	}

	public HttpHandler<String> doGET(String url, RequestCallBack<String> request) {
		return client.send(HttpMethod.GET, url, request);
	}

	public HttpHandler<String> doGET(String baseurl, RequestParams params,
			RequestCallBack<String> request) {
		return client.send(HttpMethod.GET, baseurl, params, request);
	}

	public HttpHandler<String> doPOST(String url,
			RequestCallBack<String> request) {
		return client.send(HttpMethod.POST, url, request);
	}

	public HttpHandler<String> doPOST(String baseurl, RequestParams params,
			RequestCallBack<String> request) {
		return client.send(HttpMethod.POST, baseurl, params, request);
	}

	public HttpHandler<File> downFile(String baseurl, String fileName,
			RequestCallBack<File> request) {

		return client.download(baseurl, Default_DownPath + File.separator
				+ fileName, request);
	}

}
