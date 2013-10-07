package querysheet;

import static org.junit.Assert.assertEquals;
import gapi.GoogleAPI;
import gapi.SpreadsheetBatch;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import querysheet.batch.TableToSpreadsheetBatch;
import querysheet.db.DatabaseAPI;
import querysheet.utils.Fixtures;

public class QuerySheetTest {

	private DatabaseAPI db;

	private GoogleAPI google;

	@Before
	public void before() {
		db = new DatabaseAPI();
		google = new GoogleAPI();
		Fixtures.createPersonTable(db);
		Fixtures.populatePeople(db);
	}

	@After
	public void after() {
		Fixtures.dropPersonTable(db);
		db.close();
	}

	@Test
	public void testLoadPeopleSpreadsheet() throws SQLException {
		String key = google.drive().createSpreadsheet();

		SpreadsheetBatch table = loadSpreadsheetTable();
		google.spreadsheet(key).worksheet("people").batch(table);

		google.drive().delete(key);
	}

	private SpreadsheetBatch loadSpreadsheetTable() throws SQLException {
		TableToSpreadsheetBatch batch = new TableToSpreadsheetBatch();
		batch.load(db.query("select id, name, age from people").resultSet());
		return batch;
	}

	@Test
	public void testCompleteQuerySheet() {
		String setupKey = google.drive().createSpreadsheet();
		String destKey1 = google.drive().createSpreadsheet();
		String destKey2 = google.drive().createSpreadsheet();

		try {
			String[][] setupTable = new String[][] { { "query", "spreadsheet", "worksheet" },
					{ "select id, name, age from people where age < 31 order by id", destKey1, "people" },
					{ "select id, name, age from people where age >= 31 order by id", destKey2, "people" } };

			google.spreadsheet(setupKey).worksheet("setup").batch(new MockTableBatch(setupTable));

			new QuerySheet().process(setupKey);

			List<Map<String, String>> records1 = google.spreadsheet(destKey1).worksheet("people").asMap();
			assertSpreadsheetRecord(destKey1, records1, 0, "1", "Person - 1", "21");
			assertSpreadsheetRecord(destKey1, records1, 1, "2", "Person - 2", "22");

			List<Map<String, String>> records2 = google.spreadsheet(destKey2).worksheet("people").asMap();
			assertSpreadsheetRecord(destKey2, records2, 0, "11", "Person - 11", "31");
			assertSpreadsheetRecord(destKey2, records2, 1, "12", "Person - 12", "32");
			
		} finally {
			google.drive().delete(setupKey);
			google.drive().delete(destKey1);
			google.drive().delete(destKey2);
		}
	}

	
	@Test
	public void testSimpleTransformer() {
		String setupKey = google.drive().createSpreadsheet();
		String destKey = google.drive().createSpreadsheet();

		try {
			String[][] setupTable = new String[][] { { "query", "spreadsheet", "worksheet", "batch" },
					{ "select id, name, age from people where age < 31 order by id", destKey, "people", "querysheet.ThousandYearBatch" }};

			google.spreadsheet(setupKey).worksheet("setup").batch(new MockTableBatch(setupTable));
					
			new QuerySheet().process(setupKey);

			List<Map<String, String>> records = google.spreadsheet(destKey).worksheet("people").asMap();
			assertSpreadsheetRecord(destKey, records, 0, "1", "Person - 1", "1021");
			assertSpreadsheetRecord(destKey, records, 1, "2", "Person - 2", "1022");			
			
		} finally {
			google.drive().delete(setupKey);
			google.drive().delete(destKey);
		}
	}
	
	private void assertSpreadsheetRecord(String destKey1, List<Map<String, String>> records, int row, String id,
			String name, String age) {
		Map<String, String> record = records.get(row);
		assertEquals(id, record.get("id"));
		assertEquals(name, record.get("name"));
		assertEquals(age, record.get("age"));
	}
}
