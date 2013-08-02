package querysheet;

import java.util.ArrayList;
import java.util.List;

import querysheet.google.SpreadsheetBatch;

public class SpreadsheetTable implements SpreadsheetBatch{

	private int cols = 0;
	
	List<Object[]> rows = new ArrayList<Object[]>();
	
	public void addRow(Object[] row) {
		if(cols == 0) {
			cols = row.length; 
		}
		rows.add(row);
	}

	@Override
	public int rows() {
		return rows.size();
	}

	@Override
	public int cols() {
		return cols;
	}

	@Override
	public String getValue(int i, int j) {
		Object value = rows.get(i-1)[j-1];
		return value == null ? "null" : value.toString();
	}

}
