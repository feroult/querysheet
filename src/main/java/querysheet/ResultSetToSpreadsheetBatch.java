package querysheet;

import gapi.google.SpreadsheetBatch;

import java.sql.ResultSet;

public interface ResultSetToSpreadsheetBatch extends SpreadsheetBatch {

	public void load(ResultSet rs);
}
