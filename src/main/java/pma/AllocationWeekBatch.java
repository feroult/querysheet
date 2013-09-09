package pma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import querysheet.google.SpreadsheetBatch;

public class AllocationWeekBatch implements SpreadsheetBatch {

	private static final String COLUMN_NAME_PERCENTAGE = "percentual";
	private static final String COLUMN_NAME_START = "data_fim";
	private static final String COLUMN_NAME_END = "data_inicio";
	private static final String COLUMN_NAME_PERSON_ID = "id_colaborador";

	private static final String HEADER_PERSON = "colaborador";

	private static final int FIRST_COLUMN = 1;
	private static final int FIRST_ROW = 1;

	private List<AllocationWeek> weeks = new ArrayList<AllocationWeek>();

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
			Date start = rs.getDate(COLUMN_NAME_END);
			Date end = rs.getDate(COLUMN_NAME_START);
			Integer percentage = rs.getInt(COLUMN_NAME_PERCENTAGE);

			addAllocation(personId, start, end, percentage);
		}
	}

	private void addAllocation(String personId, Date start, Date end, int percentage) {
		
		List<AllocationWeek> weeks = AllocationWeek.getWeeks(start, end, percentage);
		
		addWeeks(weeks);
		
	}

	private void addWeeks(List<AllocationWeek> weeks) {
		
		
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
