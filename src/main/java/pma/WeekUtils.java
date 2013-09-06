package pma;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekUtils {

	private static int adjustDayOfWeekToMonday(int dayOfWeek) {
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return 1;
		case Calendar.SATURDAY:
			return 2;
		default:
			return Calendar.MONDAY - dayOfWeek;
		}
	}

	public static Date adjustToMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, adjustDayOfWeekToMonday(dayOfWeek));
		return calendar.getTime();
	}

	private static int adjustDayOfWeekToFriday(int dayOfWeek) {
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return 5;
		case Calendar.SATURDAY:
			return 6;
		default:
			return Calendar.FRIDAY - dayOfWeek;
		}
	}

	public static Date adjustToFriday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		calendar.add(Calendar.DATE, adjustDayOfWeekToFriday(dayOfWeek));
		return calendar.getTime();
	}

	public static List<Week> getWeeks(Date start, Date end) {
		Calendar calendar = Calendar.getInstance();

		List<Week> weeks = new ArrayList<Week>();

		Date weekMonday = adjustToMonday(start);
		Date weekFriday = adjustToFriday(start);

		while (weekMonday.before(end)) {
			Week week = new Week();

			if (weekMonday.before(start)) {
				week.setStart(start);
			} else {
				week.setStart(weekMonday);
			}

			if (weekFriday.after(end)) {
				week.setEnd(end);
			} else {
				week.setEnd(weekFriday);
			}

			weeks.add(week);

			weekMonday = advanceWeek(calendar, weekMonday);
			weekFriday = advanceWeek(calendar, weekFriday);
		}

		return weeks;
	}

	private static Date advanceWeek(Calendar calendar, Date weekMonday) {
		calendar.setTime(weekMonday);
		calendar.add(Calendar.DATE, 7);
		weekMonday = calendar.getTime();
		return weekMonday;
	}
}
