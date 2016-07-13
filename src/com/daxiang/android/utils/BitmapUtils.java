package com.daxiang.android.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.TextUtils;
import android.view.View;

/**
 * 图片操作工具；
 * 
 * @author daxiang
 * @date 2015-5-27
 * 
 */
public class BitmapUtils {

	private static final String PNG = ".png";
	private static final String JPEG = ".jpeg";
	private static final String BMP = ".bmp";
	private static final String GIF = ".gif";

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
			fileName = System.currentTimeMillis() + PNG;
		}
		OutputStream stream = null;
		File file = new File(FileUtils.getExternalFileDirs(context), fileName);
		try {
			stream = new BufferedOutputStream(new FileOutputStream(file));

			src.compress(CompressFormat.PNG, 100, stream);
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
			Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null,
					null);
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
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
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

	/**
	 * 从本地读取图片，解析压缩为Bitmap对象；
	 * 
	 * @param file
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static Bitmap decode(File file, int targetWidth, int targetHeight) {

		if (file == null || !file.exists() || file.length() == 0) {

			return null;
		}

		String pathName = file.getPath();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);

		/*
		 * If set to a value > 1, requests the decoder to subsample the original
		 * image, returning a smaller image to save memory. The sample size is
		 * the number of pixels in either dimension that correspond to a single
		 * pixel in the decoded bitmap. For example, inSampleSize == 4 returns
		 * an image that is 1/4 the width/height of the original, and 1/16 the
		 * number of pixels. Any value <= 1 is treated the same as 1. Note: the
		 * decoder uses a final value based on powers of 2, any other value will
		 * be rounded down to the nearest power of 2.
		 */
		options.inSampleSize = computeImageSampleSize(options, targetWidth, targetHeight);

		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(pathName, options);

	}

	private static int computeImageSampleSize(Options options, int targetWidth, int targetHeight) {
		return Math.max(options.outWidth / targetWidth, options.outHeight / targetHeight);

	}

	/**
	 * 截取UI中的某个View；
	 * 
	 * @param width
	 *            保存的宽度；
	 * @param height
	 *            保存的高度；
	 * @param view
	 *            要截取的View对象；
	 * @return
	 */
	public static Bitmap screenShot(int width, int height, View view) {
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}

	/**
	 * 将图片文件存入到系统图库；
	 * 
	 * @param context
	 * @param file
	 *            图片文件；
	 * @param imageName
	 *            存储的名字；
	 * @return 成功，返回true；否则，返回false；
	 */
	public static boolean insertMediaStore(Context context, File file, String imageName) {
		if (context == null || file == null || !file.exists()) {
			return false;
		}

		if (TextUtils.isEmpty(imageName)) {
			imageName = SystemClock.currentThreadTimeMillis() + PNG;
		}
		try {

			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), imageName, null);
			// String imagePath =
			// MediaStore.Images.Media.insertImage(getContentResolver(),
			// bitmap, "", "");

			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

}
