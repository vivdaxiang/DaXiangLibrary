package com.daxiang.android.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.daxiang.android.http.HttpConstants;
import com.daxiang.android.http.okhttp.OkHttpManager;
import com.daxiang.android.http.okhttp.OkHttpRequest;
import com.daxiang.android.http.okhttp.OkHttpResponse;
import com.daxiang.android.utils.Logger;

/**
 * 
 * 
 * @author daxiang
 * @date 2016年8月6日
 * @time 上午11:14:28
 */
public abstract class BaseOkHttpActivity extends BaseActivity {
	private static final String TAG = BaseOkHttpActivity.class.getSimpleName();
	protected List<Call> callList = new ArrayList<Call>();

	protected void sendRequest(OkHttpRequest request) {
		// request.okHttpCallback = this;
		request.responseHandler = this.$responseHandler;
		Logger.i(TAG, "发起请求==" + request.url());
		Call call = OkHttpManager.sendRequest(request);
		callList.add(call);
	}

	@SuppressLint("HandlerLeak")
	private Handler $responseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			OkHttpResponse result = (OkHttpResponse) msg.obj;
			removeSingleCall(result.getCall());
			switch (msg.what) {
			case HttpConstants.NetDataProtocol.LOAD_SUCCESS:
				// Logger.i(TAG,
				// result.getCall().request().url().url().getPath());
				Logger.i(TAG, "返回响应==" + result.getResponseStr());
				onRequestSuccess(result.getCall(), result, msg.arg1);
				break;

			case HttpConstants.NetDataProtocol.LOAD_FAILED:
				onRequestFailed(result.getCall(), result.getException(),
						msg.arg1);
				break;
			}

		}
	};

	/*
	 * @Override public void onFailed(Call call, IOException e, int requestCode)
	 * { removeSingleCall(call); onRequestFailed(call, e, requestCode); }
	 * 
	 * @Override public void onSuccess(Call call, Response response, int
	 * requestCode) { removeSingleCall(call); onRequestSuccess(call, response,
	 * requestCode); }
	 */

	protected abstract void onRequestFailed(Call call, IOException e,
			int requestCode);

	protected abstract void onRequestSuccess(Call call,
			OkHttpResponse response, int requestCode);

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消所有的Call；
		clearCalls();
	}

	protected void clearCalls() {
		for (Call call : callList) {
			if (call != null) {
				call.cancel();
			}
		}
		callList.clear();
	}

	protected void removeSingleCall(Call call) {
		callList.remove(call);
	}

}
