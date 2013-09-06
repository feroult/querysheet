package pma;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Week {

	private Date start;
	
	private Date end;

	public String getWeekLabel() {
		DateFormat dayMonthFormat = new SimpleDateFormat("dd/MM");		
		
		StringBuilder label = new StringBuilder();
		
		label.append(dayMonthFormat.format(WeekUtils.adjustToMonday(start)));
		label.append(" - ");
		label.append(dayMonthFormat.format(WeekUtils.adjustToFriday(end)));
		
		return label.toString();
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}		
}
