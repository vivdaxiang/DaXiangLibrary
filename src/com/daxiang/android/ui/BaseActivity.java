package com.daxiang.android.ui;

import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.daxiang.android.http.HttpConstants;
import com.daxiang.android.http.HttpConstants.HttpMethod;
import com.daxiang.android.http.JsonTask;
import com.daxiang.android.http.executor.TaskExecutor;
import com.daxiang.android.util.Logger;

/**
 * 
 * @author daxiang
 * 
 *         2015-3-25
 */
public class BaseActivity extends Activity {

	private static final String BASETAG = BaseActivity.class.getSimpleName();

	private HashMap<Integer, JsonTask> mTaskQueue = new HashMap<Integer, JsonTask>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Logger.d(BASETAG, this.getClass().getSimpleName()
				+ " onCreate() invoked!!");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onStart() {
		Logger.d(BASETAG, this.getClass().getSimpleName()
				+ " onStart() invoked!!");
		super.onStart();
	}

	@Override
	public void onDestroy() {
		Logger.d(BASETAG, this.getClass().getSimpleName()
				+ " onDestroy() invoked!!");

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
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

	/**
	 * 
	 * @param path
	 *            请求url
	 * @param requestCode
	 *            请求码
	 */
	public void requestHttpData(String path, int requestCode) {
		requestHttpData(path, requestCode, 0);
	}

	/**
	 * 
	 * @param path
	 *            请求URL；
	 * @param requestCode
	 *            请求码；
	 * @param dataAccessMode
	 *            缓存方式；also see: {@link HttpConstants.NetDataProtocol}
	 */
	public void requestHttpData(String path, int requestCode, int dataAccessMode) {
		requestHttpData(path, requestCode, dataAccessMode, HttpMethod.GET, null);
	}

	/**
	 * 
	 * 
	 * @param path
	 *            请求url
	 * @param requestCode
	 *            请求码
	 * @param method
	 *            请求方式
	 * @param postParameters
	 *            post方式参数
	 */

	public void requestHttpData(String path, int requestCode,
			HttpMethod method, List<NameValuePair> postParameters) {
		requestHttpData(path, requestCode, 0, method, postParameters);
	}

	/**
	 * 
	 * @param path
	 *            请求url
	 * @param requestCode
	 *            请求码
	 * @param dataAccessMode
	 *            缓存方式 默认只从网络取数据不做缓存；also see:
	 *            {@link HttpConstants.NetDataProtocol}
	 * @param method
	 *            请求方式
	 * @param postParameters
	 *            post方式参数
	 */
	public void requestHttpData(String path, int requestCode,
			int dataAccessMode, HttpMethod method,
			List<NameValuePair> postParameters) {

		JsonTask jsonTask = new JsonTask(this, $responseHandler, path,
				requestCode, method, postParameters);
		if (dataAccessMode >= 1) {
			jsonTask.setDataAccessMode(dataAccessMode);
		}
		if (mTaskQueue.containsKey(requestCode)) {
			Logger.i(BASETAG, "remove repeated task, id: " + requestCode);
			mTaskQueue.get(requestCode).cancel();
		}
		Logger.i(BASETAG, "add task to queue, id: " + requestCode);
		mTaskQueue.put(requestCode, jsonTask);
		TaskExecutor.getInstance().submit(jsonTask);
	}

	protected Handler $responseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HttpConstants.NetDataProtocol.LOAD_SUCCESS:
				try {
					resultDataSuccess(msg.arg1, (String) msg.obj);
				} catch (Exception e) {
					Logger.e(
							BASETAG,
							"handleMessage LOAD_SUCCESS --> error-->  "
									+ e.toString());
				}
				break;
			case HttpConstants.NetDataProtocol.LOAD_MISTAKE:
				try {
					resultDataMistake(msg.arg1, (String) msg.obj);
				} catch (Exception e) {
					Logger.e(
							BASETAG,
							"handleMessage LOAD_MISTAKE --> error-->  "
									+ e.toString());
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
	public void resultDataSuccess(int requestCode, String data) {
		Logger.i(BASETAG, "resultDataSuccess = " + requestCode);
	}

	/**
	 * 加载数据失败的回调，子类须重写该方法；
	 * 
	 * @param requestCode
	 *            请求码；
	 * @param errorMessage
	 *            错误信息；
	 */
	public void resultDataMistake(int requestCode, String errorMessage) {
		Logger.e(BASETAG, requestCode + "resultDataMistake = " + errorMessage);
	}

	protected ProgressDialog progressDialog;// 加载对话框

	/**
	 * 关闭提示框
	 */
	public void closeProgressDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}

	/**
	 * 显示提示框
	 */
	public void showProgressDialog() {
		if (!this.isFinishing() && (this.progressDialog == null)) {
			this.progressDialog = new ProgressDialog(this);
		}
		// this.progressDialog.setMessage(getString(R.string.load_content));
		this.progressDialog.show();
	}

	public void recommandToYourFriend(String url, String shareTitle) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, shareTitle + "   " + url);

		Intent itn = Intent.createChooser(intent, "分享");
		startActivity(itn);
	}

	public void finish() {
		super.finish();
//		 overridePendingTransition(R.anim.push_right_in,
//		 R.anim.push_right_out);
	}

	@Override
	protected void onRestart() {
		Logger.d(BASETAG, this.getClass().getSimpleName()
				+ " onRestart() invoked!!");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Logger.d(BASETAG, this.getClass().getSimpleName()
				+ " onResume() invoked!!");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Logger.d(BASETAG, this.getClass().getSimpleName()
				+ " onPause() invoked!!");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Logger.d(BASETAG, this.getClass().getSimpleName()
				+ " onStop() invoked!!");
		super.onStop();
	}

}
