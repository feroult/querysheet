package querysheet.google;

import org.junit.Before;
import org.junit.Test;

public class GoogleAPITest {

	private GoogleAPI google;

	@Before
	public void before() {		
		google = new GoogleAPI();
		
		google.spreadsheetService();				
	}

	@Test
	public void testChangeSpreadSheet() {
		String key = google.drive().createSpreadsheet();
		google.spreadsheet(key).changeCell(1, 1, "xpto");
		google.drive().delete(key);
	}
}
