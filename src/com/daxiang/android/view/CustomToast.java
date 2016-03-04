package com.daxiang.android.view;

import com.daxiang.android.R;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义的吐司；
 * 
 * @author daxiang
 * @date 2015-5-25
 * 
 */
public class CustomToast extends Toast {

	public CustomToast(Context context) {
		super(context);
	}

	public static Toast makeText(Context context, CharSequence text, int duration) {
		Toast result = new Toast(context);

		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(R.layout.view_toast, null);
		TextView tv = (TextView) v.findViewById(R.id.tv_toast);
		tv.setText(text);

		result.setView(v);
		result.setDuration(duration);
		// result.setGravity(Gravity.CENTER, 0, 0);//吐司居中；

		return result;
	}

	public static Toast makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId), duration);
	}
}
