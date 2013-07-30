package querysheet.google;

import java.net.URL;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;

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

	public void changeCell(int i, int j, String string) {
		// TODO Auto-generated method stub

	}

}
