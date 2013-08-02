package querysheet;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import querysheet.db.DatabaseAPI;
import querysheet.google.GoogleAPI;
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
		
		TableToSpreadsheetBatch table = loadSpreadsheetTable();		
		google.spreadsheet(key).worksheet("people").batch(table);
				
		google.drive().delete(key);
	}

	private TableToSpreadsheetBatch loadSpreadsheetTable() throws SQLException {
		ResultSet rs = db.query("select id, name, age from people").resultSet();		
		return new TableToSpreadsheetBatch(rs);
	}
}
