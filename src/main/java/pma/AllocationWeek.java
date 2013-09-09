package pma;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllocationWeek {

	private Date weekStart;
	
	private Date weekEnd;

	private Date allocationStart;

	private Date allocationEnd;

	private int percentage;

	public AllocationWeek(Date start, Date end, int percentage) {
		this.weekStart = start;
		this.weekEnd = end;
		this.percentage = percentage;
	}

	public String getLabel() {
		DateFormat dayMonthFormat = new SimpleDateFormat("dd/MM");		
		
		StringBuilder label = new StringBuilder();
		
		label.append(dayMonthFormat.format(weekStart));
		label.append(" - ");
		label.append(dayMonthFormat.format(weekEnd));
		
		return label.toString();
	}

	public Date getWeekStart() {
		return weekStart;
	}

	public Date getWeekEnd() {
		return weekEnd;
	}
	public void setAllocationStart(Date allocationStart) {
		this.allocationStart = allocationStart;
	}
	
	public Date getAllocationStart() {
		return allocationStart == null ? weekStart : allocationStart;
	}

	public void setAllocationEnd(Date allocationEnd) {
		this.allocationEnd = allocationEnd;
	}

	public Date getAllocationEnd() {
		return allocationEnd == null ? weekEnd : allocationEnd;
	}
	
	public int getPercentage() {
		return percentage;
	}

	public int getAllocation() {
		int daysWithoutAllocation = calculateStartDelay() + calculateEndAdvance();				
		return (int)(percentage * 0.2 * (5 - daysWithoutAllocation));
	}

	private int calculateStartDelay() {
		int startDelay = offsetToMonday(getAllocationStart());
		if(startDelay > 0) {
			startDelay = 0;
		} else {
			startDelay *= -1;
		}
		return startDelay;
	}	
		
	private int calculateEndAdvance() {
		int endAdvance = offsetToFriday(getAllocationEnd());
		if(endAdvance < 0) {
			endAdvance = 0;
		} 
		return endAdvance;
	}	
			
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((weekEnd == null) ? 0 : weekEnd.hashCode());
		result = prime * result + ((weekStart == null) ? 0 : weekStart.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AllocationWeek other = (AllocationWeek) obj;
		if (weekEnd == null) {
			if (other.weekEnd != null)
				return false;
		} else if (!weekEnd.equals(other.weekEnd))
			return false;
		if (weekStart == null) {
			if (other.weekStart != null)
				return false;
		} else if (!weekStart.equals(other.weekStart))
			return false;
		return true;
	}	
	
	private static int offsetToMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return 1;
		case Calendar.SATURDAY:
			return 2;
		default:
			return Calendar.MONDAY - dayOfWeek;
		}
	}

	protected static Date adjustToMonday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, offsetToMonday(date));
		return calendar.getTime();
	}

	private static int offsetToFriday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);		
		
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return -2;
		case Calendar.SATURDAY:
			return -1;
		default:
			return Calendar.FRIDAY - dayOfWeek;
		}
	}

	protected static Date adjustToFriday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, offsetToFriday(date));
		return calendar.getTime();
	}

	public static List<AllocationWeek> getWeeks(Date start, Date end, int percentage) {
		Calendar calendar = Calendar.getInstance();

		List<AllocationWeek> weeks = new ArrayList<AllocationWeek>();

		Date weekMonday = adjustToMonday(start);
		Date weekFriday = adjustToFriday(start);

		while (weekMonday.before(end)) {
			AllocationWeek week = new AllocationWeek(weekMonday, weekFriday, 100);

			if (weekMonday.before(start)) {
				week.setAllocationStart(start);
			} 
			
			if (weekFriday.after(end)) {
				week.setAllocationEnd(end);
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
