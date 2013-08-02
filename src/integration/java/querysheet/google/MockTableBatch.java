package querysheet.google;

public class MockTableBatch implements SpreadsheetBatch {

	private String[][] table;

	public MockTableBatch(String[][] table) {
		this.table = table;
	}

	@Override
	public int rows() {
		return table.length;
	}

	@Override
	public String getValue(int i, int j) {
		return table[i - 1][j - 1];
	}

	@Override
	public int cols() {
		return table[0].length;
	}
}
