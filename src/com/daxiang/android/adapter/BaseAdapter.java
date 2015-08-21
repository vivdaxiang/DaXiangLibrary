package com.daxiang.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * Adapter的父类；
 * 
 * @author daxiang
 * @date 2015-6-26
 * 
 */
public abstract class BaseAdapter<T> extends ArrayAdapter<T> {
	protected List<T> mDatas = new ArrayList<T>();
	protected Context mContext;
	private int page = 1;

	public BaseAdapter(Context context, List<T> mDatas) {
		super(context, 0);
		this.mContext = context;
		this.mDatas = mDatas;
		page = 1;
	}

	public void setDatas(List<T> datas) {
		this.mDatas = datas;
		notifyDataSetChanged();
	}

	/**
	 * 把数据添加到集合的尾部；
	 * 
	 * @param datas
	 */
	public void addDatas(List<T> datas) {
		this.mDatas.addAll(datas);
		page++;
		notifyDataSetChanged();
	}

	public void addData(T data) {
		this.mDatas.add(data);
		notifyDataSetChanged();
	}

	/**
	 * 下拉刷新的时候，把最新的数据添加到集合的头部；
	 * 
	 * @param datas
	 */
	public void addDatasToFirst(List<T> datas) {
		// List<T> tempList = new ArrayList<T>();
		// tempList.addAll(datas);
		// tempList.addAll(this.mDatas);

		this.mDatas.clear();
		this.mDatas.addAll(datas);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mDatas == null) {
			return 0;
		}
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		return this.getView(position, convertView);
	}

	protected View getView(final int position, View convertView) {
		if (convertView == null) {
			convertView = View.inflate(mContext, getResourceId(position), null);
		}
		setViewData(convertView, position);
		return convertView;
	}

	public void removeItem(int index) {
		this.mDatas.remove(index);
		notifyDataSetChanged();
	}

	/**
	 * 子类重写，返回ListView的item的布局文件ID，如：R.layout.xxxx；
	 * 
	 * @param Position
	 * @return
	 */
	protected abstract int getResourceId(int Position);

	/**
	 * 子类重写；
	 * 
	 * @param convertView
	 * @param position
	 */
	protected abstract void setViewData(View convertView, int position);

	public void cleanViewMap() {
		this.mDatas.clear();
		page = 1;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
