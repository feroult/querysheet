package querysheet.google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.api.services.drive.Drive;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class GoogleAPITest {

	private Drive driveService;

	private SpreadsheetService spreadsheetService;

	@Before
	public void before() throws GeneralSecurityException, IOException {		
		GoogleAPI google = new GoogleAPI();
		
		driveService = google.driveService();		
		spreadsheetService = google.spreadsheetService();				
	}

	@Test
	public void testChangeSpreadSheet() throws GeneralSecurityException, IOException, ServiceException {
		String key = createSpreadSheet();
		changeSpreadsheet(key);
		deleteSpreadSheet(key);
	}

	private void deleteSpreadSheet(String key) throws IOException {
		driveService.files().delete(key).execute();
	}

	private String createSpreadSheet() throws IOException {
		com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();

		file.setTitle("simple test");
		file.setMimeType("application/vnd.google-apps.spreadsheet");

		file = driveService.files().insert(file).execute();

		return file.getId();
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
