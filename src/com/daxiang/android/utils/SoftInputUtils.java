package com.daxiang.android.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具；
 * 
 * @author daxiang
 * @date 2015-7-3
 * 
 */
public class SoftInputUtils {

	public static void toggleSoftInput(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 指定的view调用软键盘；
	 * 
	 * @param activity
	 * @param view
	 */
	public static void showSoftInput(Activity activity, View view) {
		((InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE))
				.showSoftInput(view, 0);
	}

	/**
	 * 如果页面有软键盘显示，那么关闭它；
	 * 
	 * @param activity
	 */
	public static void hideSoftInput(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			((InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(activity.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

}
