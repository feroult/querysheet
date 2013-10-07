package querysheet;

import gapi.GoogleAPI;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import querysheet.batch.ResultSetToSpreadsheetBatch;
import querysheet.batch.TableToSpreadsheetBatch;
import querysheet.db.DatabaseAPI;
import querysheet.utils.Setup;

public class QuerySheet {

	private static final int TRUNCATE_LENGTH = 10;

	private static Logger logger = LoggerFactory.getLogger(QuerySheet.class);

	private GoogleAPI google = new GoogleAPI();

	private DatabaseAPI db;

	public static void main(String[] args) {
		String key = Setup.getSetupSpreadsheetKey();

		if (args.length != 0) {
			key = args[0];
		}

		new QuerySheet().process(key);
	}

	public void process(String key) {
		logger.info("setup key: " + key);

		db = new DatabaseAPI();

		try {
			int time = 0;
			
			List<Map<String, String>> queries = google.spreadsheet(key).worksheet("setup").asMap();

			for (Map<String, String> querySetup : queries) {
				time += processQuery(querySetup.get("query"), querySetup.get("spreadsheet"), querySetup.get("worksheet"), querySetup.get("batch"));
			}
			
			logger.info(String.format("total=%d ms", time));

		} finally {
			db.close();
		}
	}

	private long processQuery(String query, String key, String worksheet, String batchClass) {
		long time = System.currentTimeMillis();
		google.spreadsheet(key).worksheet(worksheet).batch(createBatch(query, batchClass));
		time = System.currentTimeMillis() - time;

		logger.info(String.format("elapsed=%d ms, query=%s, spreadsheet=%s, worksheet=%s", time, truncate(query), key, worksheet));
		
		return time;
	}

	private Object truncate(String s) {
		if (s.length() > TRUNCATE_LENGTH) {
			return s.substring(0, TRUNCATE_LENGTH) + "...";
		}
		return s;
	}

	private ResultSetToSpreadsheetBatch createBatch(String query, String batchClass) {
		ResultSetToSpreadsheetBatch batch = createBatchInstance(batchClass);		
		batch.load(db.query(query).resultSet());
		return batch;
	}

	private ResultSetToSpreadsheetBatch createBatchInstance(String batchClass) {
		if(batchClass == null || batchClass.equals("")) {
			return new TableToSpreadsheetBatch();
		}
		
		try {
			return (ResultSetToSpreadsheetBatch) Class.forName(batchClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
