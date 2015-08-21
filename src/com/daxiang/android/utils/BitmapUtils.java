package com.daxiang.android.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;

/**
 * 图片操作工具；
 * 
 * @author daxiang
 * @date 2015-5-27
 * 
 */
public class BitmapUtils {

	/**
	 * 保存图片到SdCard，图片保存格式为PNG；
	 * 
	 * @param context
	 * @param src
	 *            要保存的Bitmap对象；
	 * @param fileName
	 *            保存的文件名，如：photo.png；
	 * @return
	 */
	public static File saveAsFile(Context context, Bitmap src, String fileName) {
		if (context == null || src == null) {
			return null;
		}
		if (TextUtils.isEmpty(fileName)) {
			fileName = System.currentTimeMillis() + ".png";
		}
		OutputStream stream = null;
		File file = new File(FileUtils.getExternalFileDirs(context), fileName);
		try {
			stream = new BufferedOutputStream(new FileOutputStream(file));

			src.compress(CompressFormat.PNG, 80, stream);
			stream.flush();
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();

			return null;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			src.recycle();
		}

		return file;
	}

	/**
	 * 获取图片的存储路径；
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static String getRealFilePath(Context context, Uri uri) {
		if (null == uri)
			return null;
		String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	/**
	 * Bitmap转为字节数组；
	 * 
	 * @param bmp
	 *            Bitmap对象；
	 * @param needRecycle
	 *            是否需要回收Bitmap对象；true，回收；false，不回收；
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Bitmap createCircleBitmap(Bitmap bitmap, int width, int height) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		/**
		 * 产生一个同样大小的画布
		 */
		Canvas canvas = new Canvas(target);
		/**
		 * 首先绘制圆形
		 */
		canvas.drawCircle(width / 2, height / 2, width / 2, paint);
		/**
		 * 使用SRC_IN
		 */
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		/**
		 * 绘制图片
		 */
		canvas.drawBitmap(bitmap, 0, 0, paint);
		return target;
	}
}
