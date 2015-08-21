package com.daxiang.android.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类；
 * 
 * @author daxiang
 * @date 2015-7-31
 * 
 */
public class DateUtils {
	public static SimpleDateFormat sdfyyyy_MM_dd = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static SimpleDateFormat sdfyyyy_MM_dd_HH_mm_ss = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdfyyyy_MM_dd_HH_mm = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm");
	public static SimpleDateFormat sdfyyyy_MM_dd_HH = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:00");

	public static SimpleDateFormat sdfHH_mm = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat sdfMM_dd = new SimpleDateFormat("MM-dd");
	public static SimpleDateFormat sdfMM_dd_1 = new SimpleDateFormat("MM月dd日");
	public static SimpleDateFormat GMT = new SimpleDateFormat(
			"EEE, dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);

	/**
	 * return yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getDate_yyyy_MM_dd() {
		return sdfyyyy_MM_dd.format(new Date());
	}

	public static String getDate_HH_mm() {
		return sdfHH_mm.format(new Date());
	}

	/**
	 * return yyyy_MM_dd_HH_mm_ss
	 * 
	 * @return
	 */
	public static String getDate_yyyy_MM_dd_HH_mm_ss() {
		return sdfyyyy_MM_dd_HH_mm_ss.format(new Date());
	}

	/**
	 * 
	 * @param dateString
	 * @return 距1970年的秒数
	 */
	public static long getDate(String dateString, SimpleDateFormat format) {
		long time = 0;
		Date date = null;
		try {
			date = format.parse(dateString);
			time = (date.getTime() - format.parse("1970-1-1 0:0:0").getTime()) / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return time;
	}

	/**
	 * 获取格林威治时间毫秒数
	 * 
	 * @param dateString
	 * @param format
	 * @return
	 */
	public static long getGMTDate(String dateString, SimpleDateFormat format) {
		long time = 0;
		Date date = null;
		try {
			date = format.parse(dateString);
			time = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 由返回回来的秒数算出真正的时间
	 * 
	 * @param s
	 * @return
	 */
	public static String getDateString(long s, SimpleDateFormat format) {
		Calendar c = Calendar.getInstance();
		// int offset = c.getTimeZone().getRawOffset();
		// c.setTime(new Date(s * 1000 - offset));
		c.setTime(new Date(s));
		Date dt = c.getTime();
		String str = format.format(dt);
		return str;
	}

	/**
	 * 由毫秒数，算出真正的时间
	 * 
	 * @param s
	 * @param format
	 * @return
	 */
	public static String getDateString(long ms) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(ms));
		Date dt = c.getTime();
		String str = sdfyyyy_MM_dd_HH_mm.format(dt);
		return str;
	}

	/**
	 * 有返回回来的秒数，算出真正的秒数
	 * 
	 * @param second
	 * @return
	 */
	public static long getRealSecond(long s) {
		Calendar c = Calendar.getInstance();
		// int offset = c.getTimeZone().getRawOffset();
		// c.setTime(new Date(s * 1000 - offset));
		c.setTime(new Date(s * 1000));
		long timeInMillis = c.getTimeInMillis();
		return timeInMillis / 1000;
	}

	/**
	 * 计算和某一好友距上次聊天时间
	 * 
	 * @param s
	 * @return
	 */
	public static String distanceTime(long second) {
		long realSecond = getRealSecond(second); // 服务器返回回来的真实时间
		long currentSecond = System.currentTimeMillis() / 1000; // 当期时间
		long disSecond = currentSecond - realSecond; // 距现在的时间
		String result = null;

		int oneDay = 86400; // 一天
		int oneHour = 3600; // 一小时
		int oneMonth = 2592000; // 一个月

		if (disSecond < oneHour) { // 几分钟前
			if (disSecond < 60) {
				result = "1分钟前";
			} else {
				result = disSecond / 60 + "分钟前";
			}
		} else if (disSecond >= oneHour && disSecond < oneDay) { // 几小时前
			result = disSecond / oneHour + "小时前";
		} else if (disSecond >= oneDay && disSecond < oneMonth) { // 几天前
			result = disSecond / oneDay + "天前";
		} else {
			result = "一个月前";
		}
		return result;
	}

	/**
	 * 初始化广材圈时间
	 * 
	 * @param milliseconds
	 *            毫秒
	 * @return
	 */
	public static String initCircleDate(long milliseconds) {
		Calendar currentDate = Calendar.getInstance();
		Calendar systemDate = Calendar.getInstance();
		currentDate.setTime(new Date());
		systemDate.setTime(new Date(milliseconds));
		long realSecond = milliseconds / 1000; // 服务器返回回来的真实时间
		long currentSecond = System.currentTimeMillis() / 1000; // 当期时间
		int oneHour = 3600; // 一小时

		if (currentDate.get(Calendar.YEAR) == systemDate.get(Calendar.YEAR)) {
			if (currentDate.get(Calendar.MONTH) == systemDate
					.get(Calendar.MONTH)
					&& currentDate.get(Calendar.DAY_OF_MONTH) == systemDate
							.get(Calendar.DAY_OF_MONTH)) {
				long disSecond = currentSecond - realSecond; // 距现在的时间
				if (disSecond < oneHour) { // 几分钟前
					if (disSecond < 60) {
						return "刚刚";
					} else {
						return disSecond / 60 + "分钟前";
					}
				} else { // 几小时前
					return disSecond / oneHour + "小时前";
				}
			} else if (currentDate.get(Calendar.MONTH) == systemDate
					.get(Calendar.MONTH)
					&& currentDate.get(Calendar.DAY_OF_MONTH) == systemDate
							.get(Calendar.DAY_OF_MONTH) + 1) {
				return "昨天 "
						+ new SimpleDateFormat("HH点mm分").format(new Date(
								milliseconds));
			} else if (currentDate.get(Calendar.MONTH) == systemDate
					.get(Calendar.MONTH)
					&& currentDate.get(Calendar.DAY_OF_MONTH) == systemDate
							.get(Calendar.DAY_OF_MONTH) + 2) {
				return "前天 "
						+ new SimpleDateFormat("HH点mm分").format(new Date(
								milliseconds));
			} else {
				return new SimpleDateFormat("MM月dd日 HH点mm分").format(new Date(
						milliseconds));
			}
		} else {
			return new SimpleDateFormat("yyyy年MM月dd日 HH点mm分").format(new Date(
					milliseconds));
		}
	}

	/**
	 * 得到距1970年的秒数
	 * 
	 * @param s
	 * @return
	 */
	public static long getSecond(long s) {
		Date date = new Date(s);
		String dateString = sdfyyyy_MM_dd_HH_mm_ss.format(date);
		return getDate(dateString, sdfyyyy_MM_dd_HH_mm_ss);
	}

	/**
	 * 给定日期的秒数
	 */
	public static long getSecond(String date) {
		int year = Integer.parseInt(date.split("-")[0]);
		int month = Integer.parseInt(date.split("-")[1]) - 1;
		int day = Integer.parseInt(date.split("-")[2]);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTimeInMillis() / 1000;
	}

	/**
	 * 由返回的秒数算出年龄
	 * 
	 */
	public static String getBirthDay(long s) {
		Calendar c = Calendar.getInstance();
		int currentYear = c.get(Calendar.YEAR);
		int offset = c.getTimeZone().getRawOffset();
		c.setTime(new Date(s * 1000 - offset));
		return currentYear - c.get(Calendar.YEAR) + "岁";
	}

	/**
	 * 得到生日的的月份和天数
	 * 
	 * @param s
	 * @return
	 */
	public static int[] getBirth(long s) {
		int[] birthArray = new int[2];
		Calendar c = Calendar.getInstance();
		int offset = c.getTimeZone().getRawOffset();
		c.setTime(new Date(s * 1000 - offset));

		birthArray[0] = c.get(Calendar.MONDAY);
		birthArray[1] = c.get(Calendar.DAY_OF_MONTH);
		return birthArray;
	}

	/**
	 * 得到某一天的开始时间(type=1明天 type == 2后天 type ==3 大后天)
	 */
	public static long getStartTime(int type) {

		Calendar calendar = Calendar.getInstance();

		switch (type) {
		case 1: // 明天
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + 1);
			break;
		case 2: // 后天
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + 2);
			break;
		case 3:// 大后天
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.get(Calendar.DAY_OF_MONTH) + 3);
			break;
		}
		calendar.set(Calendar.HOUR_OF_DAY, 0); // 小时
		calendar.set(Calendar.MINUTE, 0); // 分
		calendar.set(Calendar.SECOND, 0); // 秒
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTimeInMillis() / 1000;
	}

	/**
	 * 拿传入的目标时间和当前的系统时间比较，得出两个时间之间相差的月数；比如2013-12月和2014年3月之间相差4个月；2014年3月28日10:
	 * 39:19；
	 * 
	 * @param dateStr
	 *            传入的目标时间；
	 * @return 相差几个月；
	 */
	public static int monthCompare(String dateStr) {// date的格式“2014-02-23”
		int differMonth = 0;// 间隔的月数；
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = dateFormat.parse(dateStr);
			// 目标日历；
			Calendar objCalendarDate = Calendar.getInstance();
			objCalendarDate.setTime(date);
			// 当前日历；
			Calendar currentCalendarDate = Calendar.getInstance();

			if (currentCalendarDate.after(objCalendarDate)) {
				differMonth = (currentCalendarDate.get(Calendar.YEAR) - objCalendarDate
						.get(Calendar.YEAR))
						* 12
						+ currentCalendarDate.get(Calendar.MONTH)
						- objCalendarDate.get(Calendar.MONTH);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
		return differMonth;
	}
	/***
	 * 拿到的小时时间作比较
	 * 大：true; 小：false；
	 * *****/
	
	public static boolean isHourBiger(){
	
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			Date date = dateFormat.parse("15:00");
			// 目标日历；
			Calendar objCalendarDate = Calendar.getInstance();
			objCalendarDate.setTime(date);
			// 当前日历；
			Calendar currentCalendarDate = Calendar.getInstance();
			if (currentCalendarDate.after(date)) {
				return true;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
