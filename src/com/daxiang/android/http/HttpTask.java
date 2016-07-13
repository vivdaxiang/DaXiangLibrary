package com.daxiang.android.http;

import com.daxiang.android.http.request.HttpRequest;
import com.daxiang.android.utils.Logger;
import com.daxiang.android.utils.NetworkUtils;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * 
 * @author daxiang
 * 
 *         2015-3-24
 */
public class HttpTask implements Runnable {
	private static final String TAG = HttpTask.class.getSimpleName();
	private Handler handler;

	private boolean isCancel;

	// ---------------------------------------------------------------------------
	private HttpRequest httpRequest;

	public HttpTask(HttpRequest httpRequest) {
		this.httpRequest = httpRequest;
		handler = httpRequest.getResponseHandler();
	}

	@Override
	public void run() {
		Message msg = handler.obtainMessage();
		msg.arg1 = httpRequest.requestCode;
		String json = null;
		try {
			if (isCancel) {
				Logger.i(TAG, "cancel this task before run!");
				return;
			}
			switch (httpRequest.dataAccessMode) {
			// 访问网络，不做本地存储
			case HttpConstants.NetDataProtocol.DATA_FROM_NET_NO_CACHE:
				httpRequest.setCache(false);
				json = JsonUtil.getJsonFromServer(httpRequest);
				break;

			case HttpConstants.NetDataProtocol.DATA_FROM_NET_AND_CACHE:
				httpRequest.setCache(true);

				if (NetworkUtils.isAvailable(httpRequest.getContext())) {
					json = JsonUtil.getJsonFromServer(httpRequest);
				} else {
					json = JsonUtil.getJsonFromFile(httpRequest);
				}

				break;

			// 仅访问本地存储
			case HttpConstants.NetDataProtocol.DATA_ONLY_FROM_CACHE:
				json = JsonUtil.getJsonFromFile(httpRequest);
				break;

			// 先访问本地存储返回数据展示，再访问网络更新数据并刷新UI
			case HttpConstants.NetDataProtocol.DATA_FROM_CACHE_THEN_NET:
				json = JsonUtil.getJsonFromFile(httpRequest);
				if (!TextUtils.isEmpty(json)) {
					Message msg1 = handler.obtainMessage();
					msg1.arg1 = httpRequest.requestCode;
					msg1.what = HttpConstants.NetDataProtocol.LOAD_SUCCESS;
					msg1.obj = json;
					handlerMessage(msg1);
				}
				httpRequest.setCache(true);
				json = JsonUtil.getJsonFromServer(httpRequest);
				break;

			// 默认仅访问网络，并且不做本地缓存；
			default:
				httpRequest.setCache(false);
				json = JsonUtil.getJsonFromServer(httpRequest);
				break;
			}

			if (TextUtils.isEmpty(json)) {
				msg.what = HttpConstants.NetDataProtocol.LOAD_FAILED;
				handlerMessage(msg);
			} else {
				msg.what = HttpConstants.NetDataProtocol.LOAD_SUCCESS;
				msg.obj = json;
				handlerMessage(msg);
			}

		} catch (Exception e) {
			msg.what = HttpConstants.NetDataProtocol.LOAD_FAILED;
			handlerMessage(msg);
			e.printStackTrace();
		}
	}

	private void handlerMessage(Message msg) {
		if (isCancel) {
			Logger.i(TAG, "cancel this task after run!");
			return;
		}
		Logger.i(TAG, "handlerMessage callback");
		handler.sendMessage(msg);
	}

	public void cancel() {
		isCancel = true;
	}

}