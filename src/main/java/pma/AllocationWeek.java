package pma;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AllocationWeek {

	private Date weekStart;
	
	private Date weekEnd;

	private Date allocationStart;

	private Date allocationEnd;

	public AllocationWeek(Date start, Date end) {
		this.weekStart = start;
		this.weekEnd = end;
	}

	public AllocationWeek() {
		
	}

	public String getWeekLabel() {
		DateFormat dayMonthFormat = new SimpleDateFormat("dd/MM");		
		
		StringBuilder label = new StringBuilder();
		
		label.append(dayMonthFormat.format(WeekUtils.adjustToMonday(weekStart)));
		label.append(" - ");
		label.append(dayMonthFormat.format(WeekUtils.adjustToFriday(weekEnd)));
		
		return label.toString();
	}

	public Date getWeekStart() {
		return weekStart;
	}

	public void setWeekStart(Date start) {
		this.weekStart = start;
	}

	public Date getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd(Date end) {
		this.weekEnd = end;
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
}
