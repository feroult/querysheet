package querysheet;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import querysheet.db.DatabaseAPI;
import querysheet.google.GoogleAPI;

public class QuerySheet {

	private GoogleAPI google = new GoogleAPI();

	private DatabaseAPI db;

	public static void main(String[] args) {
		QuerySheet querysheet = new QuerySheet();
		querysheet.process(args[0]);
	}

	private void process(String key) {
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
