package querysheet;

import java.sql.ResultSet;

import querysheet.google.SpreadsheetBatch;

public interface ResultSetToSpreadsheetBatch extends SpreadsheetBatch {

	public void load(ResultSet rs);
}
