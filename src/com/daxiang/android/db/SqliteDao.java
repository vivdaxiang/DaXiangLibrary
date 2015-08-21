package com.daxiang.android.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * 数据库DAO类；
 * 
 * @author daxiang
 * @date 2015-6-5
 * 
 */
public class SqliteDao {
	protected DBFactory dbFactory;
	protected SQLiteDatabase db;
	protected Context context;

	public SqliteDao(Context context) {
		dbFactory = new DBFactory(context);
		this.context = context;
	}

	public SqliteDao(Context context, DBFactory dbFactory, SQLiteDatabase db) {
		this.dbFactory = dbFactory;
		this.context = context;
		this.db = db;
	}

	/**
	 * 字符串转换成整型
	 * 
	 * @param arg
	 * @return
	 */
	protected Integer stringToInteger(String arg) {
		return !isEmpty(arg) ? Integer.valueOf(arg) : 0;
	}

	/**
	 * 插入或更新数据
	 * 
	 * @param sql
	 *            SQL语句
	 * @param obj
	 *            参数
	 */
	protected void insertOrUpdateData(String sql, Object[] obj) {
		db = dbFactory.getWritableDatabase();
		db.beginTransaction();
		db.execSQL(sql, obj);
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDB();
	}

	/**
	 * 增加数据
	 * 
	 * @author long
	 * @param table
	 *            表名
	 * @param nullColumnHack
	 * @param values
	 *            值
	 * @return 数据ID
	 */
	protected Integer insertData(String table, String nullColumnHack,
			ContentValues values) {
		db = dbFactory.getWritableDatabase();
		db.beginTransaction();
		Integer id = (int) db.insert(table, nullColumnHack, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDB();
		return id;
	}

	/**
	 * 设置Transaction
	 * 
	 * @return 设置过Transaction的SQLiteDatabase对象
	 */
	protected SQLiteDatabase beginTransaction() {
		db = dbFactory.getWritableDatabase();
		db.beginTransaction();
		return db;
	}

	/**
	 * 设置事物处理成功
	 * 
	 */
	protected void setTransactionSuccessful() {
		db.setTransactionSuccessful();
	}

	/**
	 * 结束Transaction
	 * 
	 */
	protected void endTransaction() {
		db.endTransaction();
		dbFactory.close();
	}

	/**
	 * 增加数据
	 * 
	 * @author long
	 * @param db
	 *            设置Transaction的SQLiteDatabase
	 * @param table
	 *            表名
	 * @param nullColumnHack
	 * @param values
	 *            值
	 * @return 数据ID
	 */
	protected Integer insertDataNoTransaction(SQLiteDatabase db, String table,
			String nullColumnHack, ContentValues values) {
		Integer id = (int) db.insert(table, nullColumnHack, values);
		return id;
	}

	/**
	 * 修改数据
	 * 
	 * @author long
	 * @param table
	 *            表名
	 * @param values
	 *            值
	 * @param whereClause
	 *            条件
	 * @param whereArgs
	 *            要更新的属性
	 * @return 修改的数量
	 */
	protected int updateData(String table, ContentValues values,
			String whereClause, String[] whereArgs) {
		db = dbFactory.getWritableDatabase();
		db.beginTransaction();
		int num = db.update(table, values, whereClause, whereArgs);
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDB();
		return num;
	}

	/**
	 * 修改数据
	 * 
	 * @author long
	 * @param db
	 *            设置Transaction的SQLiteDatabase
	 * @param table
	 *            表名
	 * @param values
	 *            值
	 * @param whereClause
	 *            条件
	 * @param whereArgs
	 *            要更新的属性
	 * @return 修改的数量
	 */
	protected int updateDataNoTransaction(SQLiteDatabase db, String table,
			ContentValues values, String whereClause, String[] whereArgs) {
		int num = db.update(table, values, whereClause, whereArgs);
		return num;
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String table, String[] columns) {
		return queryData(cls, false, table, columns, null, null, null, null,
				null, null);
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @param selection
	 *            条件
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String table,
			String[] columns, String selection) {
		return queryData(cls, false, table, columns, selection, null, null,
				null, null, null);
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @param selection
	 *            条件
	 * @param groupBy
	 *            分组
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String table,
			String[] columns, String selection, String groupBy) {
		return queryData(cls, false, table, columns, selection, null, groupBy,
				null, null, null);
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @param selection
	 *            条件
	 * @param groupBy
	 *            分组
	 * @param orderBy
	 *            排序
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String table,
			String[] columns, String selection, String groupBy, String orderBy) {
		return queryData(cls, false, table, columns, selection, null, groupBy,
				null, orderBy, null);
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @param selection
	 *            条件
	 * @param selectionArgs
	 *            可能的选择条件
	 * @param groupBy
	 *            分组
	 * @param orderBy
	 *            排序
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String orderBy) {
		return queryData(cls, false, table, columns, selection, selectionArgs,
				groupBy, null, orderBy, null);
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @param selection
	 *            条件
	 * @param selectionArgs
	 *            可能的选择条件
	 * @param groupBy
	 *            分组
	 * @param having
	 *            有无行组
	 * @param orderBy
	 *            排序
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy) {
		return queryData(cls, false, table, columns, selection, selectionArgs,
				groupBy, having, orderBy, null);
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @param selection
	 *            条件
	 * @param selectionArgs
	 *            可能的选择条件
	 * @param groupBy
	 *            分组
	 * @param having
	 *            有无行组
	 * @param orderBy
	 *            排序
	 * @param limit
	 *            限制
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String table,
			String[] columns, String selection, String[] selectionArgs,
			String groupBy, String having, String orderBy, String limit) {
		return queryData(cls, false, table, columns, selection, selectionArgs,
				groupBy, having, orderBy, limit);
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param distinct
	 *            是否独特
	 * @param table
	 *            表明
	 * @param columns
	 *            要查询的数据
	 * @param selection
	 *            条件
	 * @param selectionArgs
	 *            可能的选择条件
	 * @param groupBy
	 *            分组
	 * @param having
	 *            有无行组
	 * @param orderBy
	 *            排序
	 * @param limit
	 *            限制
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, boolean distinct,
			String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {
		List<T> data = new ArrayList<T>();
		db = dbFactory.getReadableDatabase();
		Cursor cursor = db.query(distinct, table, columns, selection,
				selectionArgs, groupBy, having, orderBy, limit);
		parserData(cursor, cls, data);
		cursor.close();
		closeDB();
		return data;
	}

	/**
	 * 查询数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cls
	 *            类对象
	 * @param sql
	 *            SQL语句
	 * @return List<T> 集合
	 */
	protected <T> List<T> queryData(Class<T> cls, String sql) {
		List<T> data = new ArrayList<T>();
		db = dbFactory.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		parserData(cursor, cls, data);
		cursor.close();
		closeDB();
		return data;
	}

	/**
	 * 解析数据
	 * 
	 * @param <T>
	 *            对象
	 * @param cursor
	 *            游标
	 * @param cls
	 *            类对象
	 * @param columns
	 *            查询参数
	 * @param data
	 *            数据
	 */
	private <T> void parserData(Cursor cursor, Class<T> cls, List<T> data) {
		while (cursor.moveToNext()) {
			T t = null;
			try {
				t = cls.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			if (t != null) {
				for (int i = 0; i < cursor.getColumnNames().length; i++) {
					try {
						Field field = cls
								.getField(trim(cursor.getColumnNames()[i]));

						Object obj = field.get(t);
						if (obj instanceof String) {
							field.set(t, cursor.getString(cursor
									.getColumnIndexOrThrow(cursor
											.getColumnNames()[i])));
						} else if (obj instanceof Integer) {
							field.set(t, cursor.getInt(cursor
									.getColumnIndexOrThrow(cursor
											.getColumnNames()[i])));
						} else if (obj instanceof Double) {
							field.set(t, cursor.getDouble(cursor
									.getColumnIndexOrThrow(cursor
											.getColumnNames()[i])));
						} else if (obj instanceof Long) {
							field.set(t, cursor.getLong(cursor
									.getColumnIndexOrThrow(cursor
											.getColumnName(i))));
						} else if (obj instanceof Boolean) {
							field.set(t, cursor.getInt(cursor
									.getColumnIndexOrThrow(cursor
											.getColumnNames()[i])) == 0 ? false
									: true);
						} else {
							field.set(t, cursor.getString(cursor
									.getColumnIndexOrThrow(cursor
											.getColumnNames()[i])));
						}

					} catch (NoSuchFieldException e) {
						e.printStackTrace();
						continue;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						continue;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						continue;
					}
				}
				data.add(t);
			}
		}
	}

	/**
	 * 删除数据
	 * 
	 * @author long
	 * @param table
	 *            表名
	 * @param whereClause
	 *            具体条件
	 * @param whereArgs
	 *            要删除的属性
	 */
	protected int deleteData(String table, String whereClause,
			String[] whereArgs) {
		db = dbFactory.getWritableDatabase();
		db.beginTransaction();
		int num = db.delete(table, whereClause, whereArgs);
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDB();
		return num;
	}

	/**
	 * 删除数据
	 * 
	 * @param sql
	 *            SQL语句
	 * @param args
	 *            参数
	 */
	protected void deleteData(String sql, String[] args) {
		db = dbFactory.getWritableDatabase();
		db.beginTransaction();
		db.execSQL(sql, args);
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDB();
	}

	/**
	 * 判断表中是否有这个字段
	 * 
	 * @param table
	 *            表名
	 * @param field
	 *            字段名
	 * @return
	 */
	protected boolean fieldIsExist(String table, String field) {
		List<String> feilds = getTableFields(table);
		return feilds.contains(field) ? true : false;
	}

	/**
	 * 往表中添加一个字段
	 * 
	 * @param table
	 *            表名
	 * @param field
	 *            字段名
	 */
	protected void alterFieldToTable(String table, String field) {
		db = dbFactory.getWritableDatabase();
		db.beginTransaction();
		String sql = "ALTER TABLE " + table + " ADD " + field + " TEXT NULL";
		db.execSQL(sql, null);
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDB();
	}

	/**
	 * 获取表的所有字段
	 * 
	 * @param table
	 *            表名
	 * @return
	 */
	private List<String> getTableFields(String table) {
		List<String> list = new ArrayList<String>();
		String sql = "PRAGMA table_info(" + table + ")";
		db = dbFactory.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				list.add(cursor.getString(i));
			}
		}
		cursor.close();
		closeDB();
		return list;
	}

	/**
	 * 关闭数据库
	 */
	protected void closeDB() {
		dbFactory.close();
	}

	public static boolean isEmpty(String text) {
		return TextUtils.isEmpty(text);
	}

	public static String trim(String text) {
		return isEmpty(text) ? "" : text.trim();
	}
}
