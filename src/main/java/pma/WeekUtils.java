package pma;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekUtils {

	private static final int DAYS_FROM_MONDAY_TO_FRIDAY = 4;

	private static DateFormat dayMonthFormat = new SimpleDateFormat("dd/MM");

	public static String getWeekLabel(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		calendar.add(Calendar.DATE, adjustDayOfWeekToMonday(dayOfWeek));

		StringBuilder label = new StringBuilder();

		label.append(dayMonthFormat.format(calendar.getTime()));
		label.append(" - ");

		calendar.add(Calendar.DATE, DAYS_FROM_MONDAY_TO_FRIDAY);
		label.append(dayMonthFormat.format(calendar.getTime()));

		return label.toString();

	}

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
		
		while(weekMonday.before(end)) {
			Week week = new Week();
		
			if(weekMonday.before(start)) {
				week.setStart(start);
			} else {
				week.setStart(weekMonday);
			}
		
			if(weekFriday.after(end)) {
				week.setEnd(end);
			} else {
				week.setEnd(weekFriday);
			}
			
			weeks.add(week);
						
			calendar.setTime(weekMonday);
			calendar.add(Calendar.DATE, 7);
			weekMonday = calendar.getTime();
			
			calendar.setTime(weekFriday);
			calendar.add(Calendar.DATE, 7);
			weekFriday = calendar.getTime();			
		} 
				
		return weeks;
	}

}
