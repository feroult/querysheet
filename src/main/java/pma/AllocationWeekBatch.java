package pma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import querysheet.ResultSetToSpreadsheetBatch;

public class AllocationWeekBatch implements ResultSetToSpreadsheetBatch {

	private static final int MAX_ALLOCATION_MONTHS = 4;

	private static final int ROW_OFFSET = 2;

	private static final int COLUMN_OFFSET = 2;

	protected static final String COLUMN_NAME_PERCENTAGE = "percentual";
	protected static final String COLUMN_NAME_START = "data_inicio";
	protected static final String COLUMN_NAME_END = "data_fim";
	protected static final String COLUMN_NAME_PERSON = "colaborador";

	private static final String HEADER_PERSON = "colaborador";

	private static final int FIRST_COLUMN = 1;
	private static final int FIRST_ROW = 1;

	private List<AllocationWeek> weeks;

	private List<String> persons = new ArrayList<String>();

	private Map<String, Map<String, List<AllocationWeek>>> allocation = new HashMap<String, Map<String, List<AllocationWeek>>>();

	private Date firstStart;
	private Date lastEnd;

	public void load(ResultSet rs)  {
		try {
			while (rs.next()) {				
				String person = addPerson(rs.getString(COLUMN_NAME_PERSON));
								
				Date start = adjustStart(rs.getDate(COLUMN_NAME_START));

				if(!validStart(start)) { 
					continue;
				}
				
				Date end = adjustEnd(rs.getDate(COLUMN_NAME_END));
				Integer percentage = rs.getInt(COLUMN_NAME_PERCENTAGE);					
				
				addAllocation(person, start, end, percentage);
			}
	
			weeks = AllocationWeek.getWeeks(firstStart, lastEnd, 0);
			Collections.sort(persons);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}			
	}

	private String addPerson(String person) {
		if (!persons.contains(person)) {
			persons.add(person);
			allocation.put(person, new HashMap<String, List<AllocationWeek>>());
		}
		return person;
	}

	protected boolean validStart(Date date) {
		if(date == null) {
			return false;
		}
		
		Calendar calendar = Calendar.getInstance();		
		calendar.setTime(today());		
		calendar.add(Calendar.MONTH, MAX_ALLOCATION_MONTHS);
		
		if(date.after(calendar.getTime())) {
			return false;
		}
		
		return true;
	}

	protected Date adjustEnd(Date date) {
		Calendar calendar = Calendar.getInstance();		
		calendar.setTime(today());		
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		calendar.add(Calendar.MONTH, MAX_ALLOCATION_MONTHS);
				
		if(date.after(calendar.getTime())) {
			return calendar.getTime();
		}
		
		return date;
	}

	protected Date adjustStart(Date date) {
		if(date == null) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();		
		calendar.setTime(today());		
		//calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
				
		if(date.before(calendar.getTime())) {
			return calendar.getTime();
		}
		
		return date;
	}
	
	protected Date today() {
		return new Date();
	}

	private void addAllocation(String person, Date start, Date end, int percentage) {
		checkAndSetFirstAndLastDates(start, end);
		mergePersonAllocation(person, AllocationWeek.getWeeks(start, end, percentage));
	}

	private void mergePersonAllocation(String person, List<AllocationWeek> weeks) {
		Map<String, List<AllocationWeek>> personAllocation = allocation.get(person);

		for (AllocationWeek week : weeks) {
			if (!personAllocation.containsKey(week.getKey())) {
				personAllocation.put(week.getKey(), new ArrayList<AllocationWeek>());
			}

			List<AllocationWeek> weekAllocation = personAllocation.get(week.getKey());
			weekAllocation.add(week);
		}
	}

	private void checkAndSetFirstAndLastDates(Date start, Date end) {
		if (firstStart == null || start.before(firstStart)) {
			firstStart = start;
		}

		if (lastEnd == null || end.after(lastEnd)) {
			lastEnd = end;
		}
	}

	@Override
	public int rows() {
		return persons.size() + 1;
	}

	@Override
	public int cols() {
		return weeks.size() + 1;
	}

	@Override
	public String getValue(int row, int column) {
		if (row == FIRST_ROW) {
			return getHeader(column);
		}
		
		return getValueInTable(row, column);
	}

	private String getValueInTable(int row, int column) {
		String person = persons.get(row - ROW_OFFSET);
		
		if(column == FIRST_COLUMN) {
			return person;
		}

		Integer totalAllocation = getAllocation(person, weeks.get(column - COLUMN_OFFSET).getKey());
		return totalAllocation.toString();
	}

	private Integer getAllocation(String person, String key) {
			
		Map<String, List<AllocationWeek>> personAllocation = allocation.get(person);
		
		if(!personAllocation.containsKey(key)) {
			return 0;
		}
		
		List<AllocationWeek> weeks = personAllocation.get(key);
		
		int total = 0;
		
		for(AllocationWeek week : weeks) {
			total += week.getAllocation();
		}
		
		return total;
	}

	private String getHeader(int column) {
		if (column == FIRST_COLUMN) {
			return HEADER_PERSON;
		}
		return weeks.get(column - COLUMN_OFFSET).getLabel();
	}

}
