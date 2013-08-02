package querysheet;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import querysheet.db.DatabaseAPI;
import querysheet.google.GoogleAPI;
import querysheet.utils.Setup;

public class QuerySheet {

	private GoogleAPI google = new GoogleAPI();

	private DatabaseAPI db;

	public static void main(String[] args) {
		String key = Setup.getSetupSpreadsheetKey();
		
		if(args.length != 0) {
			key = args[0];
		}
		
		new QuerySheet().process(key);
	}

	public void process(String key) {
		db = new DatabaseAPI();

		try {
			List<Map<String, String>> queries = google.spreadsheet(key).worksheet("setup").asMap();

			for (Map<String, String> querySetup : queries) {
				processQuery(querySetup.get("query"), querySetup.get("spreadsheet"), querySetup.get("worksheet"));
			}

		} finally {
			db.close();
		}
	}

	private void processQuery(String query, String key, String worksheet) {
		google.spreadsheet(key).worksheet(worksheet).batch(createBatch(query));
	}

	private TableToSpreadsheetBatch createBatch(String query) {
		ResultSet rs = db.query(query).resultSet();
		return new TableToSpreadsheetBatch(rs);
	}

}
