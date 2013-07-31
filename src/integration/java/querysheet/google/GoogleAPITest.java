package querysheet.google;

import static org.junit.Assert.assertEquals;

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
		google.spreadsheet(key).setValue(1, 1, "xpto");		
		assertEquals("xpto", google.spreadsheet(key).getValue(1, 1));		
		google.drive().delete(key);
	}

	@Test
	public void testSpreadSheetTableUpdate() {		
		google.spreadsheet("0AsxNRtEKJEOadC10a3MtMDVabmRRc0dDY0lNQXNTQ3c").update(mockUpdateSet());		
		//google.spreadsheet("0AsxNRtEKJEOadC10a3MtMDVabmRRc0dDY0lNQXNTQ3c").setValue(1, 1, "xpto");
	}

	private SpreadsheetUpdateSet mockUpdateSet() {
		// TODO Auto-generated method stub
		return null;
	}
}
