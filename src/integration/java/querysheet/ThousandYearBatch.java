package querysheet;

public class ThousandYearBatch extends TableToSpreadsheetBatch {

	public String getValue(int row, int column) {
		String value = super.getValue(row, column);
		
		if(column == 3 && row != 1) {
			Integer age = new Integer(value);
			return String.valueOf(age + 1000);
		}
		
		return value;
	}
	
}
