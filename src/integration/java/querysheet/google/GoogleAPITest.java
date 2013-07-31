package querysheet.google;

import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;

import org.junit.Before;
import org.junit.Test;

public class GoogleAPITest {

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
		google.spreadsheet(key).worksheet("xpto").batch(mockBatch());
		assertEquals("i=1, j=1", google.spreadsheet(key).worksheet("xpto").getValue(1, 1));
		google.drive().delete(key);
	}

	private SpreadsheetBatch mockBatch() {
		return new SpreadsheetBatch() {			
			@Override
			public int rows() {				
				return 5;
			}
			
			@Override
			public String getValue(int i, int j) {				
				return MessageFormat.format("i={0}, j={1}", i, j);
			}
			
			@Override
			public int cols() {				
				return 5;
			}
		}; 
		
	}
}
