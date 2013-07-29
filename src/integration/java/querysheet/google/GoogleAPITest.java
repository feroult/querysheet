package querysheet.google;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import querysheet.utils.Setup;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class GoogleAPITest {

	private GoogleCredential credential;

	private Drive drive;

	private SpreadsheetService service;

	@Before
	public void before() throws GeneralSecurityException, IOException {
		final HttpTransport TRANSPORT = new NetHttpTransport();

		final JsonFactory JSON_FACTORY = new GsonFactory();

		credential = new GoogleCredential.Builder().setTransport(TRANSPORT).setJsonFactory(JSON_FACTORY)
				.setServiceAccountId(Setup.getServiceAccountEmail())
				.setServiceAccountScopes(Arrays.asList("https://spreadsheets.google.com/feeds", "https://docs.google.com/feeds"))
				.setServiceAccountPrivateKeyFromP12File(new File(Setup.getServiceAccountKeyPath())).build();

		drive = new Drive.Builder(TRANSPORT, JSON_FACTORY, credential).build();

		service = new SpreadsheetService("Query Sheet");
		service.setOAuth2Credentials(credential);
	}

	@Test
	public void testChangeSpreadSheet() throws GeneralSecurityException, IOException, ServiceException {
		String key = createSpreadSheet();
		changeSpreadsheet(key);
		deleteSpreadSheet(key);
	}

	private void deleteSpreadSheet(String key) throws IOException {
		drive.files().delete(key).execute();	
	}

	private String createSpreadSheet() throws IOException {
		com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();

		file.setTitle("simple test");
		file.setMimeType("application/vnd.google-apps.spreadsheet");

		file = drive.files().insert(file).execute();

		return file.getId();
	}

	private void changeSpreadsheet(String key) throws MalformedURLException, IOException, ServiceException {
		String spreadsheetURL = "https://spreadsheets.google.com/feeds/spreadsheets/" + key;
		SpreadsheetEntry spreadsheet = service.getEntry(new URL(spreadsheetURL), SpreadsheetEntry.class);
		changeFirstWorksheet(spreadsheet);
	}

	private void changeFirstWorksheet(SpreadsheetEntry spreadsheet) throws IOException, ServiceException {
		WorksheetFeed worksheetFeed = service.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);

		URL cellFeedUrl = worksheet.getCellFeedUrl();
		CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);

		CellEntry cellEntry = new CellEntry(1, 1, "aa");
		cellFeed.insert(cellEntry);
	}
}
