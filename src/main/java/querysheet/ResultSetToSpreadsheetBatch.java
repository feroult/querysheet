package querysheet;

import gapi.SpreadsheetBatch;

import java.sql.ResultSet;

public interface ResultSetToSpreadsheetBatch extends SpreadsheetBatch {

	public void load(ResultSet rs);
}
