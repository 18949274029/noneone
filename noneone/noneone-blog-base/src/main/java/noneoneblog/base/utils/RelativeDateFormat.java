package noneoneblog.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author leisure
 */
public class RelativeDateFormat {
	private static final long ONE_MINUTE = 60000L;
	private static final long ONE_HOUR = 3600000L;
	private static final long ONE_DAY = 86400000L;
	private static final long ONE_WEEK = 604800000L;

	private static final String ONE_SECOND_AGO = "秒前";
	private static final String ONE_MINUTE_AGO = "分钟前";
	private static final String ONE_HOUR_AGO = "小时前";
	private static final String ONE_DAY_AGO = "天前";
	private static final String ONE_MONTH_AGO = "月前";
	private static final String ONE_YEAR_AGO = "年前";
	private static final String ONE_UNKNOWN = "未知";

	public static String format(Date date) {
		if (null == date) {
			return ONE_UNKNOWN;
		}
		long delta = new Date().getTime() - date.getTime();
		if (delta < 1L * ONE_MINUTE) {
			long seconds = toSeconds(delta);
			return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
		}
		if (delta < 45L * ONE_MINUTE) {
			long minutes = toMinutes(delta);
			return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
		}
		if (delta < 24L * ONE_HOUR) {
			long hours = toHours(delta);
			if (getDaysToday(date) == 0) {
				return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
			}
			return "昨天";
		}
		if (getDaysToday(date) == 1) {
			return "昨天";
		}
		int days = getDaysToday(date);
		if (days < 30) {
			return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
		}
		int months = days / 30;
		if (months < 12) {
			return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
		} else {

			long years = days / 365;
			return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
		}
	}

	/**
	 * 验证距今有多少天
	 * 
	 * @param date
	 * @return
	 */
	private static int getDaysToday(Date date) {
		// int days = (int) ((new Date().getTime() - date.getTime()) / (1000 *
		// 3600 * 24));
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(new Date());
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) // 同一年
		{
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) // 闰年
				{
					timeDistance += 366;
				} else // 不是闰年
				{
					timeDistance += 365;
				}
			}

			return timeDistance + (day2 - day1);
		} else // 不同年
		{
			return day2 - day1;
		}
	}

	private static long toSeconds(long date) {
		return date / 1000L;
	}

	private static long toMinutes(long date) {
		return toSeconds(date) / 60L;
	}

	private static long toHours(long date) {
		return toMinutes(date) / 60L;
	}

	private static long toDays(long date) {
		return toHours(date) / 24L;
	}

	private static long toMonths(long date) {
		return toDays(date) / 30L;
	}

	private static long toYears(long date) {
		return toMonths(date) / 365L;
	}
}
