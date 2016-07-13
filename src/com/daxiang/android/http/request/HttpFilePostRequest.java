package com.daxiang.android.http.request;

import java.io.File;

import android.content.Context;

/**
 * http的文件上传；
 * 
 * @author daxiang
 * @date 2016年4月6日
 * @time 下午7:52:49
 */
public class HttpFilePostRequest extends HttpPostRequest {

	public HttpFilePostRequest(Context context) {
		super(context);
	}

	public File file;
	public String fileBodyName;
}
