package com.daxiang.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Listview在ScrollView内部时使用；
 * 
 * @author daxiang
 * @date 2015-7-6
 * 
 */
public class InnerListView extends ListView {

	public InnerListView(Context context) {
		super(context);

	}

	public InnerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public InnerListView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
