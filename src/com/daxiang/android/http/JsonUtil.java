package com.daxiang.android.http;

import com.daxiang.android.http.core.HttpTool;
import com.daxiang.android.utils.Logger;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * 
 * @author daxiang
 * 
 *         2015-3-24
 */
public class JsonUtil {
	private static final String TAG = JsonUtil.class.getSimpleName();
	private static final String CACHE_JSON_SP = "cache_json_sp";

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

	// -------------------------------------------------------------------------------------------------

	public static String getJsonFromServer(HttpRequest httpRequest) {

		String json = HttpTool.sendRequest(httpRequest);

		Logger.i(TAG, "getJsonFromServer---" + json);

		if (!TextUtils.isEmpty(json)) {
			if (httpRequest.isCache()) {
				cacheJsonToFile(httpRequest.path, json, httpRequest.getContext());
			}
			return json;
		}
		return "";
	}

	public static String getJsonFromFile(HttpRequest httpRequest) {
		String result = ACache.get(httpRequest.getContext()).getAsString(httpRequest.path);
		if (!TextUtils.isEmpty(result)) {
			return result;
		}
		return "";
	}

	public static String getJsonFromSharedPref(HttpRequest httpRequest) {
		SharedPreferences sp = httpRequest.getContext().getSharedPreferences(CACHE_JSON_SP, Context.MODE_PRIVATE);
		String json = "";
		if (sp.contains(httpRequest.path)) {
			json = sp.getString(httpRequest.path, "");
		}
		return json;
	}
}
