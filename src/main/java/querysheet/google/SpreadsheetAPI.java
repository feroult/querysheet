package querysheet.google;

import java.net.URL;
import java.util.List;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;

public class SpreadsheetAPI {

	private SpreadsheetService spreadsheetService;

	private SpreadsheetEntry spreadsheet;

	public SpreadsheetAPI(SpreadsheetService spreadsheetService) {
		this.spreadsheetService = spreadsheetService;
	}

	public SpreadsheetAPI key(String key) {
		try {
			String spreadsheetURL = "https://spreadsheets.google.com/feeds/spreadsheets/" + key;
			spreadsheet = spreadsheetService.getEntry(new URL(spreadsheetURL), SpreadsheetEntry.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	public void changeCell(int i, int j, String value) {
		try {
			WorksheetFeed worksheetFeed = spreadsheetService.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
			List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
			WorksheetEntry worksheet = worksheets.get(0);
	
			URL cellFeedUrl = worksheet.getCellFeedUrl();
			CellFeed cellFeed = spreadsheetService.getFeed(cellFeedUrl, CellFeed.class);
	
			CellEntry cellEntry = new CellEntry(i, j, value);
			cellFeed.insert(cellEntry);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
