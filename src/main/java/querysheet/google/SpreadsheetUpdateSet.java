package querysheet.google;

public interface SpreadsheetUpdateSet {

	public int rows();

	public int cols();

	public String getValue(int i, int j);

}
