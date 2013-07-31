package querysheet.google;

public interface SpreadsheetBatch {

	public int rows();

	public int cols();

	public String getValue(int i, int j);

}
