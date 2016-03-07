package com.daxiang.android.ui;

import com.daxiang.android.utils.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 
 * @author daxiang
 * 
 *         2015-3-25
 */
public class BaseActivity extends Activity {

	private static final String BASETAG = BaseActivity.class.getSimpleName();

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
		super.onDestroy();
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
