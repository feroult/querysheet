package querysheet.google;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.batch.BatchInterruptedException;
import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.Link;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
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
		if (spreadsheet != null && spreadsheet.getKey().equals(key)) {
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

	public void setValue(int i, int j, String value) {
		try {
			WorksheetEntry worksheet = firstWorksheet();

			CellFeed cellFeed = spreadsheetService.getFeed(worksheet.getCellFeedUrl(), CellFeed.class);

			CellEntry cellEntry = new CellEntry(i, j, value);
			cellFeed.insert(cellEntry);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private WorksheetEntry firstWorksheet() {
		try {
			WorksheetFeed worksheetFeed = spreadsheetService.getFeed(spreadsheet.getWorksheetFeedUrl(),
					WorksheetFeed.class);
			List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
			WorksheetEntry worksheet = worksheets.get(0);
			return worksheet;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public SpreadsheetAPI worksheet(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(SpreadsheetUpdateSet updateSet) {
		try {
			WorksheetEntry worksheet = firstWorksheet();

			CellFeed batchResponse = updateCells(worksheet);

			printStatus(batchResponse);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Map<String, CellEntry> getCellEntryMap(WorksheetEntry worksheet) {
		
		try {		
			CellFeed batchRequest = new CellFeed();
	
			for (int i = 1; i <= 10; i++) {
				for (int j = 1; j <= 10; j++) {
					String id = createId(i, j);
					CellEntry batchEntry = new CellEntry(i, j, id);
					batchEntry.setId(id);
					BatchUtils.setBatchId(batchEntry, id);
					BatchUtils.setBatchOperationType(batchEntry, BatchOperationType.QUERY);
					batchRequest.getEntries().add(batchEntry);
				}
			}
			
			CellFeed cellFeed = spreadsheetService.getFeed(worksheet.getCellFeedUrl(), CellFeed.class);
	
			Link batchLink = cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
			CellFeed batchResponse = spreadsheetService.batch(new URL(batchLink.getHref()), batchRequest);
			
	
			Map<String, CellEntry> cellEntryMap = new HashMap<String, CellEntry>();
			for (CellEntry entry : batchResponse.getEntries()) {
				cellEntryMap.put(BatchUtils.getBatchId(entry), entry);
//				System.out.printf("batch %s {CellEntry: id=%s editLink=%s inputValue=%s\n", BatchUtils
//						.getBatchId(entry), entry.getId(), entry.getEditLink().getHref(), entry.getCell()
//						.getInputValue());
			}
	
			return cellEntryMap;
			
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private CellFeed updateCells(WorksheetEntry worksheet) throws IOException, ServiceException,
			BatchInterruptedException, MalformedURLException {
		
		//Map<String, CellEntry> cellEntryMap = getCellEntryMap(worksheet);
		
		CellQuery query = new CellQuery(worksheet.getCellFeedUrl());
		query.setRange("A1:J10");
		query.setReturnEmpty(true);
		CellFeed cellFeed = spreadsheetService.getFeed(query, CellFeed.class);		
		List<CellEntry> cellEntries = cellFeed.getEntries();
				
		CellFeed batchRequest = new CellFeed();

		int count = 0;
		
		for (int i = 1; i <= 10; i++) {
			for (int j = 1; j <= 10; j++) {
				BatchOperationType batchOperationType = BatchOperationType.UPDATE;
				//String id = createId(i, j);
				CellEntry batchEntry = new CellEntry(cellEntries.get(count));
				batchEntry.changeInputValueLocal(createId(i, j));
				BatchUtils.setBatchId(batchEntry, String.valueOf(count));
				BatchUtils.setBatchOperationType(batchEntry, batchOperationType);
				batchRequest.getEntries().add(batchEntry);
				
				count++;
			}

		}

		// Submit the update
		//CellFeed cellFeed = spreadsheetService.getFeed(worksheet.getCellFeedUrl(), CellFeed.class);

		Link batchLink = cellFeed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
		
		spreadsheetService.setHeader("If-Match", "*");
		CellFeed batchResponse = spreadsheetService.batch(new URL(batchLink.getHref()), batchRequest);
		spreadsheetService.setHeader("If-Match", null);
		
		return batchResponse;
	}

	private String createId(int i, int j) {
		return "i=" + i + ", j=" + j;
	}

	private void printStatus(CellFeed batchResponse) {
		boolean isSuccess = true;
		for (CellEntry entry : batchResponse.getEntries()) {
			String batchId = BatchUtils.getBatchId(entry);
			if (!BatchUtils.isSuccess(entry)) {
				isSuccess = false;
				BatchStatus status = BatchUtils.getBatchStatus(entry);
				System.out.printf("%s failed (%s) %s\n", batchId, status.getReason(), status.getContent());
			}
		}

		System.out.println(isSuccess ? "\nBatch operations successful." : "\nBatch operations failed");
	}
}
