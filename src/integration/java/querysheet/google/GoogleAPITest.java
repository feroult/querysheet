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
		google.spreadsheet(key).worksheet("first worksheet").setValue(1, 1, "xpto");		
		assertEquals("xpto", google.spreadsheet(key).worksheet("first worksheet").getValue(1, 1));		
		google.drive().delete(key);
	}

	@Test
	public void testSpreadSheetTableUpdate() {		
		//google.spreadsheet("0AsxNRtEKJEOadC10a3MtMDVabmRRc0dDY0lNQXNTQ3c").update(mockUpdateSet());		
		google.spreadsheet("0AsxNRtEKJEOadC10a3MtMDVabmRRc0dDY0lNQXNTQ3c").worksheet("xpto").setValue(1, 1, "lala");
	}

	private SpreadsheetUpdateSet mockUpdateSet() {
		// TODO Auto-generated method stub
		return null;
	}
}
