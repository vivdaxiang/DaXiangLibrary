package com.daxiang.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper子类；
 * 
 * @author daxiang
 * @date 2015-6-5
 * 
 */
public class DBFactory extends SQLiteOpenHelper {

	private String TAG = "DBFactory";

	/**
	 * 数据库名字
	 */
	private static final String NAME = "DaXiangLibrary_DB";

	/**
	 * 数据库版本号
	 */
	private static final int VERSION = 1;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 */
	public DBFactory(Context context) {
		super(context, NAME, null, VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
