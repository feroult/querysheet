package querysheet.google;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class GoogleAPITest {

	private class MockPeopleBatch implements SpreadsheetBatch {
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
		google.spreadsheet(key).worksheet("xpto").batch(new MockPeopleBatch());
		assertEquals("John", google.spreadsheet(key).worksheet("xpto").getValue(2, 2));
		
		google.drive().delete(key);
	}

	@Test
	public void testLoadSpreadsheetHash() {
		String key = google.drive().createSpreadsheet();		
		google.spreadsheet(key).worksheet("xpto").batch(new MockPeopleBatch());
		
		List<Map<String, String>> records = google.spreadsheet(key).worksheet("xpto").asMap();
		
		Map<String, String> record = records.get(0);		
		assertEquals("1", record.get("id"));
		assertEquals("John", record.get("name"));
		assertEquals("10", record.get("age"));
				
		google.drive().delete(key);		
	}

}
