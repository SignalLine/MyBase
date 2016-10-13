package com.rilin.lzy.mybase.util;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

/**
 * 类 描 述： Json操作类
 */
public class JsonUtil {
	/**
	 * 根据json字符串得到JSON对象
	 * 
	 * @param json
	 *            {'a':'1','b':'2','c':'3'}
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getJSONObjectFromJsonStr(String json)
			throws Exception {
		JSONObject jsonObj = new JSONObject(json);
		return jsonObj;
	}
	/**
	 * 是用此方法
	 * */
	@SuppressWarnings("unchecked")
	public static <T> T getJsonSource(String json, Context context, Class<?> t)
			throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		String mess = obj.getString("message");
		if (!TextUtils.isEmpty(mess))
			// Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
			if (code == 0) {
				return (T) JSON.parseObject(obj.getJSONObject("data")
						.toString(), t);
			} else if(code==-1){
				//context.startActivity(new Intent(context,LoginActivity.class));
			}else {
				Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
			}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getJsonNoSource(String json, Class<?> t)
			throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		if (code == 0) {
			return (T) JSON
					.parseObject(obj.getJSONObject("data").toString(), t);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getJsonNoSourceList(String json, Class<?> t)
			throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		if (code == 0) {
			return (ArrayList<T>) JSON.parseArray(obj.getJSONArray("data")
					.toString(), t);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> getJsonSourceList(String json, Class<?> t,Context context)
			throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		if (code == 0) {
			return (ArrayList<T>) JSON.parseArray(obj.getJSONArray("data")
					.toString(), t);
		}else if(code==-1){
			Toast.makeText(context,obj.getString("message"),Toast.LENGTH_LONG).show();
			//context.startActivity(new Intent(context,LoginActivity.class));
		}
		return null;
	}

	public static String getJsonArrNoSource(String json) throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		if (code == 0) {
			return obj.getJSONArray("data").toString();
		}
		return null;
	}

	public static int getJsonCode(String json, Context context)
			throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		Toast.makeText(context, "" + obj.getString("message"), Toast.LENGTH_SHORT)
				.show();
		return code;
	}

	public static boolean isCodeSuccess(String json, Context context)
			throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		
		if (code == 0) {
			return true;
		}

		return false;
	}
	
	public static boolean isCodeSuccess2(String json, Context context)
			throws Exception {
		JSONObject obj = new JSONObject(json);
		int code = obj.getInt("code");
		Toast.makeText(context, "" + obj.getString("data"), Toast.LENGTH_SHORT)
				.show();
		if (code == 0) {
			return true;
		}else if (code==-1) {
			//context.startActivity(new Intent(context,LoginActivity.class));
		}else {
			Toast.makeText(context, "" + obj.getString("message"), Toast.LENGTH_SHORT);
		}
		return false;
	}
	
	
}