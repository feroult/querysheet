package querysheet.batch;

import gapi.SpreadsheetBatch;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public abstract class ResultSetToSpreadsheetBatch implements SpreadsheetBatch {

	public abstract void load(ResultSet rs);

	protected String formatString(Object value) {
		if(!Number.class.isInstance(value)) {
			return value.toString();
		}
		
		Locale locale  = new Locale("pt", "BR");
		String pattern = "###.##";
	
		DecimalFormat decimalFormat = (DecimalFormat)
		        NumberFormat.getNumberInstance(locale);
		decimalFormat.applyPattern(pattern);
	
		return decimalFormat.format(value);
	}
}
