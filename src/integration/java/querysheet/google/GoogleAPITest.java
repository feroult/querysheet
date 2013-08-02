package querysheet.google;

import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;

public class GoogleAPITest {

	private class MockBatch implements SpreadsheetBatch {
		private String[][] table = new String[][] { { "id", "name", "age" }, { "1", "John", "10" },
				{ "2", "Anne", "15" } };

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

	private GoogleAPI google;

	@Before
	public void before() {
		google = new GoogleAPI();
	}

	@Test
	public void testChangeSpreadSheet() {
		String key = google.drive().createSpreadsheet();
		google.spreadsheet(key).worksheet("first worksheet").setValue(1, 1, "xpto");
		assertEquals("xpto", google.spreadsheet(key).worksheet("first worksheet").getValue(1, 1));
		google.drive().delete(key);
	}

	@Test
	public void testSpreadSheetBatchUpdate() {
		String key = google.drive().createSpreadsheet();
		google.spreadsheet(key).worksheet("xpto").batch(new MockBatch());
		assertEquals("John", google.spreadsheet(key).worksheet("xpto").getValue(2, 2));
		google.drive().delete(key);
	}

	@Test
	public void testLoadSpreadsheetHash() {

	}

}
