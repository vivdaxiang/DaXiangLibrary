package com.daxiang.android.http;

import com.daxiang.android.bean.BaseResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Server返回的json字符串解析基类；
 * 
 * @author daxiang
 * @date 2015-6-18
 * 
 */
public class ResponseParserBase {

	public static BaseResponse getBaseResult(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<BaseResponse>() {
		}.getType());
	}

	/**
	 * 解股页面--》问题；
	 * 
	 * @param json
	 * @return
	 */
	/*
	 * public static List<JieGuQuestion> getJieGuQuestions(String json) { try {
	 * JSONObject object = new JSONObject(json); int flag =
	 * object.getInt("flag"); String questions = object.getString("list"); if
	 * (flag == 1 && !TextUtils.isEmpty(questions)) { Gson gson = new Gson();
	 * return gson.fromJson(questions, new TypeToken<List<JieGuQuestion>>() {
	 * }.getType()); } else { return null; } } catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * return null; }
	 */

}
