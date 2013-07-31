package querysheet.google;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

public class SpreadsheetAPI {

	private SpreadsheetService spreadsheetService;

	private SpreadsheetEntry spreadsheet;

	public SpreadsheetAPI(SpreadsheetService spreadsheetService) {
		this.spreadsheetService = spreadsheetService;
	}

	public SpreadsheetAPI key(String key) {
		if(spreadsheet != null && spreadsheet.getKey().equals(key)) {
			return this;
		}
		
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
			WorksheetEntry worksheet = firstWorksheet();
	
			CellFeed cellFeed = spreadsheetService.getFeed(worksheet.getCellFeedUrl(), CellFeed.class);
	
			CellEntry cellEntry = new CellEntry(i, j, value);
			cellFeed.insert(cellEntry);
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private WorksheetEntry firstWorksheet() throws IOException, ServiceException {
		WorksheetFeed worksheetFeed = spreadsheetService.getFeed(spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		WorksheetEntry worksheet = worksheets.get(0);
		return worksheet;
	}

	public String getValue(int i, int j) {
		try {
			WorksheetEntry worksheet = firstWorksheet();
			
			CellQuery query = new CellQuery(worksheet.getCellFeedUrl());
			
			query.setMinimumRow(i);
			query.setMaximumRow(i);			
			query.setMinimumCol(j);
			query.setMaximumCol(j);
			query.setMaxResults(1);
			
			CellFeed cellFeed = spreadsheetService.getFeed(query, CellFeed.class);
			return cellFeed.getEntries().get(0).getCell().getValue();
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
