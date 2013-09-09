package pma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import querysheet.google.SpreadsheetBatch;

public class AllocationWeekBatch implements SpreadsheetBatch {

	protected static final String COLUMN_NAME_PERCENTAGE = "percentual";
	protected static final String COLUMN_NAME_START = "data_fim";
	protected static final String COLUMN_NAME_END = "data_inicio";
	protected static final String COLUMN_NAME_PERSON_ID = "id_colaborador";

	private static final String HEADER_PERSON = "colaborador";

	private static final int FIRST_COLUMN = 1;
	private static final int FIRST_ROW = 1;

	private List<AllocationWeek> weeks;

	private Map<String, Map<Date, List<AllocationWeek>>> allocation = new HashMap<String, Map<Date, List<AllocationWeek>>>();

	private Date firstStart;
	private Date lastEnd;

	public AllocationWeekBatch(ResultSet rs) {
		try {
			load(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void load(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String personId = rs.getString(COLUMN_NAME_PERSON_ID);
			Date start = rs.getDate(COLUMN_NAME_START);
			Date end = rs.getDate(COLUMN_NAME_END);
			Integer percentage = rs.getInt(COLUMN_NAME_PERCENTAGE);

			addAllocation(personId, start, end, percentage);
		}
		
		weeks = AllocationWeek.getWeeks(firstStart, lastEnd, 0);
	}

	private void addAllocation(String personId, Date start, Date end, int percentage) {
		checkAndSetFirstAndLastDates(start, end);
		mergePersonAllocation(personId, AllocationWeek.getWeeks(start, end, percentage));				
	}

	private void mergePersonAllocation(String personId, List<AllocationWeek> weeks) {
		if (!allocation.containsKey(personId)) {
			allocation.put(personId, new HashMap<Date, List<AllocationWeek>>());
		}

		Map<Date, List<AllocationWeek>> personAllocation = allocation.get(personId);

		for (AllocationWeek week : weeks) {			
			if(!personAllocation.containsKey(week.getWeekStart())) {
				personAllocation.put(week.getWeekStart(), new ArrayList<AllocationWeek>());
			}
			
			List<AllocationWeek> weekAllocation = personAllocation.get(week.getWeekStart());
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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int cols() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getValue(int i, int j) {
		if (i == FIRST_ROW) {
			return getHeader(j);
		}

		// TODO Auto-generated method stub
		return null;
	}

	private String getHeader(int j) {
		if (j == FIRST_COLUMN) {
			return HEADER_PERSON;
		}
		return weeks.get(j - 2).getLabel();
	}

}
