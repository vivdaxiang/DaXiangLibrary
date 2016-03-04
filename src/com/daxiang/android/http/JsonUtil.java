package com.daxiang.android.http;

import java.util.List;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.daxiang.android.bean.BaseRequest;
import com.daxiang.android.http.HttpConstants.HttpMethod;
import com.daxiang.android.http.core.HttpTool;
import com.daxiang.android.utils.Logger;

/**
 * 
 * @author daxiang
 * 
 *         2015-3-24
 */
public class JsonUtil {
	private static final String TAG = JsonUtil.class.getSimpleName();
	public static final String CACHE_JSON_SP = "cache_json_sp";

	/**
	 * 从server取数据，不做本地缓存；
	 * 
	 * @param url
	 * @param context
	 * @param method
	 *            请求方式
	 * @param postParameters
	 *            post请求参数
	 * @return
	 */
	public static String getJsonFromServer(String url, Context context, HttpMethod method,
			List<NameValuePair> postParameters) {

		return getJsonFromServer(url, false, context, method, postParameters);
	}

	/**
	 * 从server取数据，可设置是否进行本地存储；
	 * 
	 * @param url
	 * @param isCache
	 *            true，从服务器取数据并且进行本地存储；false，没有本地存储；
	 * @param context
	 * @param method
	 *            请求方式
	 * @param postParameters
	 *            post请求参数
	 * @return
	 */
	public static String getJsonFromServer(String url, boolean isCache, Context context, HttpMethod method,
			List<NameValuePair> postParameters) {

		Logger.i(TAG, "getJsonFromServer---" + url);
		String json = "";
		if (method == HttpConstants.HttpMethod.GET) {
			json = HttpTool.httpGet(url);
		} else {
			json = HttpTool.httpPost(url, postParameters);
		}
		Logger.i(TAG, "getJsonFromServer---" + json);
		if (!TextUtils.isEmpty(json)) {
			if (isCache) {
				cacheJsonToFile(url, json, context);
			}
			return json;
		}
		return "";

	}

	public static String getJsonFromServer(String url, Context context, HttpMethod method, BaseRequest bean) {

		return getJsonFromServer(url, false, context, method, bean);
	}

	public static String getJsonFromServer(String url, boolean isCache, Context context, HttpMethod method,
			BaseRequest bean) {

		Logger.i(TAG, "getJsonFromServer---" + url);
		// 按照约定当state == 200时说明数据正确

		String json = "";
		if (method == HttpConstants.HttpMethod.GET) {
			json = HttpTool.httpGet(url);
		} else if (method == HttpConstants.HttpMethod.POST) {
			json = HttpTool.httpPost(url, bean);
		} else if (method == HttpConstants.HttpMethod.DELETE) {
			json = HttpTool.httpDelete(url, null, bean);
		}

		Logger.i(TAG, "getJsonFromServer---" + json);

		if (!TextUtils.isEmpty(json)) {
			if (isCache) {
				cacheJsonToFile(url, json, context);
			}
			return json;
		}
		return "";

	}

	/**
	 * 从本地缓存文件中取数据；
	 * 
	 * @param url
	 * @param context
	 * @return 如果缓存存在且不过期，返回缓存数据，否则返回null；
	 */
	public static String getJsonFromFile(String url, Context context) {
		String result = ACache.get(context).getAsString(url);
		if (!TextUtils.isEmpty(result)) {
			return result;
		}
		return null;
	}

	/**
	 * 从SharedPreferences中取缓存数据；
	 * 
	 * @param url
	 * @param context
	 * @return
	 */
	public static String getJsonFromSharedPref(String url, Context context) {
		Logger.i(TAG, "getJsonFromSharedPref---" + url);
		SharedPreferences sp = context.getSharedPreferences(CACHE_JSON_SP, Context.MODE_PRIVATE);
		String json = "";
		if (sp.contains(url)) {
			json = sp.getString(url, "");
		}
		return json;
	}

	/**
	 * 将Server返回的json字符串缓存到文件；
	 * 
	 * @param url
	 * @param json
	 * @param context
	 */
	private static void cacheJsonToFile(String url, String json, Context context) {
		ACache.get(context).put(url, json, ACache.TIME_HOUR);
	}

	/**
	 * 将Server返回的Json字符串缓存到本地SharedPreferences中；
	 * 
	 * @param url
	 * @param json
	 * @param context
	 */
	private static void cacheJsonToSharedPref(String url, String json, Context context) {
		Logger.i(TAG, "cacheJsonToSharedPref---" + url);
		SharedPreferences sp = context.getSharedPreferences(CACHE_JSON_SP, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(url, json);
		editor.commit();
		Logger.i(TAG, "---cache Json to SharedPref end---");
	}
}
