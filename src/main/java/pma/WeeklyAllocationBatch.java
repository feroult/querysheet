package pma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import querysheet.google.SpreadsheetBatch;

public class WeeklyAllocationBatch implements SpreadsheetBatch {

	private static final String COLUMN_NAME_PERCENTAGE = "percentual";
	private static final String COLUMN_NAME_START = "data_fim";
	private static final String COLUMN_NAME_END = "data_inicio";
	private static final String COLUMN_NAME_PERSON_ID = "id_colaborador";

	private static final String HEADER_TEAM_MEMBER = "colaborador";

	private static final int FIRST_COLUMN = 1;
	private static final int FIRST_ROW = 1;

	private List<String> weeks = new ArrayList<String>();

	public WeeklyAllocationBatch(ResultSet rs) {
		try {
			load(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private void load(ResultSet rs) throws SQLException {
		while (rs.next()) {
			String personId = rs.getString(COLUMN_NAME_PERSON_ID);
			Date start = rs.getDate(COLUMN_NAME_END);
			Date end = rs.getDate(COLUMN_NAME_START);
			Integer percentage = rs.getInt(COLUMN_NAME_PERCENTAGE);

			addAllocation(personId, start, end, percentage);
		}
	}

	private void addAllocation(String personId, Date start, Date end, Integer percentage) {
		
		
		Map<String, Integer> weekAllocation = getWeekAllocation(start, end, percentage);
	}

	private Map<String, Integer> getWeekAllocation(Date start, Date end, Integer percentage) {
		Date weekStart = WeekUtils.adjustToMonday(start);
		Date weekEnd = WeekUtils.adjustToFriday(end);
		

		
		return null;
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
			return HEADER_TEAM_MEMBER;
		}
		return weeks.get(j - 2);
	}

}
