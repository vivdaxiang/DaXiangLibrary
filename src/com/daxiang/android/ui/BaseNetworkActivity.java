package com.daxiang.android.ui;

import java.util.HashMap;

import com.daxiang.android.http.HttpConstants;
import com.daxiang.android.http.HttpRequest;
import com.daxiang.android.http.HttpTask;
import com.daxiang.android.http.executor.TaskExecutor;
import com.daxiang.android.utils.Logger;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public abstract class BaseNetworkActivity extends BaseActivity {
	private static final String BASETAG = BaseNetworkActivity.class.getSimpleName();

	private HashMap<Integer, HttpTask> mTaskQueue = new HashMap<Integer, HttpTask>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.d(BASETAG, this.getClass().getSimpleName() + " onCreate() invoked!!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStart() {
		Logger.d(BASETAG, this.getClass().getSimpleName() + " onStart() invoked!!");
		super.onStart();
	}

	@Override
	public void onDestroy() {
		Logger.d(BASETAG, this.getClass().getSimpleName() + " onDestroy() invoked!!");

		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		// 结束已开启的所有线程
		if (mTaskQueue.size() > 0) {
			for (Integer taskId : mTaskQueue.keySet()) {
				mTaskQueue.get(taskId).cancel();
			}
			mTaskQueue.clear();
		}
		// 移除所有响应的线程
		$responseHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	protected void sendRequest(HttpRequest httpRequest) {

		httpRequest.setResponseHandler($responseHandler);

		HttpTask jsonTask = new HttpTask(httpRequest);

		if (mTaskQueue.containsKey(httpRequest.requestCode)) {
			Logger.i(BASETAG, "remove repeated task, id: " + httpRequest.requestCode);
			mTaskQueue.get(httpRequest.requestCode).cancel();
		}

		Logger.i(BASETAG, "add task to queue, id: " + httpRequest.requestCode);
		mTaskQueue.put(httpRequest.requestCode, jsonTask);
		TaskExecutor.getInstance().submit(jsonTask);
	}

	private Handler $responseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case HttpConstants.NetDataProtocol.LOAD_SUCCESS:
				try {
					onRequestSuccess(msg.arg1, (String) msg.obj);
				} catch (Exception e) {
					Logger.e(BASETAG, "handleMessage LOAD_SUCCESS --> error-->  " + e.toString());
				}
				break;

			case HttpConstants.NetDataProtocol.LOAD_FAILED:
				try {
					onRequestFailed(msg.arg1, (String) msg.obj);
				} catch (Exception e) {
					Logger.e(BASETAG, "handleMessage LOAD_FAILED --> error-->  " + e.toString());
				}
				break;
			}
		}
	};

	/**
	 * 成功加载数据的回调，子类须重写该方法；
	 * 
	 * @param requestCode
	 *            请求码；
	 * @param data
	 *            返回的数据；
	 */
	protected abstract void onRequestSuccess(int requestCode, String data);

	/**
	 * 加载数据失败的回调，子类须重写该方法；
	 * 
	 * @param requestCode
	 *            请求码；
	 * @param errorMessage
	 *            错误信息；
	 */
	protected abstract void onRequestFailed(int requestCode, String errorMessage);

	protected ProgressDialog mProgressDialog;// 加载对话框

	/**
	 * 关闭提示框
	 */
	public void dismissProgressDialog() {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}

	/**
	 * 显示提示框
	 */
	public void showProgressDialog() {
		if (!this.isFinishing() && (mProgressDialog == null)) {
			mProgressDialog = new ProgressDialog(this);
		}
		mProgressDialog.show();
	}

	public void finish() {
		super.finish();
	}

	@Override
	protected void onRestart() {
		Logger.d(BASETAG, this.getClass().getSimpleName() + " onRestart() invoked!!");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Logger.d(BASETAG, this.getClass().getSimpleName() + " onResume() invoked!!");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Logger.d(BASETAG, this.getClass().getSimpleName() + " onPause() invoked!!");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Logger.d(BASETAG, this.getClass().getSimpleName() + " onStop() invoked!!");
		super.onStop();
	}
}
