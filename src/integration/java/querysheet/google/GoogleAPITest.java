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
}
