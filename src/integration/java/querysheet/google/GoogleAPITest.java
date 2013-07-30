package querysheet.google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class GoogleAPITest {

	private SpreadsheetService spreadsheetService;

	private GoogleAPI google;

	@Before
	public void before() {		
		google = new GoogleAPI();
		
		spreadsheetService = google.spreadsheetService();				
	}

	@Test
	public void testChangeSpreadSheet() throws GeneralSecurityException, IOException, ServiceException {
		String key = google.drive().createSpreadsheet();
		changeSpreadsheetX(key);
		google.drive().delete(key);
	}

	private void changeSpreadsheetX(String key) {
		google.spreadsheet(key).changeCell(1, 1, "xpto");
	}

	private void changeSpreadsheet(String key) throws MalformedURLException, IOException, ServiceException {
		String spreadsheetURL = "https://spreadsheets.google.com/feeds/spreadsheets/" + key;
		SpreadsheetEntry spreadsheet = spreadsheetService.getEntry(new URL(spreadsheetURL), SpreadsheetEntry.class);
		changeFirstWorksheet(spreadsheet);
	}

	private void changeFirstWorksheet(SpreadsheetEntry spreadsheet) throws IOException, ServiceException {
		WorksheetFeed worksheetFeed = spreadsheetService.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);

		URL cellFeedUrl = worksheet.getCellFeedUrl();
		CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrl, CellFeed.class);

		CellEntry cellEntry = new CellEntry(1, 1, "query sheet");
		cellFeed.insert(cellEntry);
	}
}
