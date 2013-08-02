package querysheet;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import querysheet.db.DatabaseAPI;
import querysheet.db.Person;
import querysheet.google.GoogleAPI;

public class QuerySheetTest {

	private DatabaseAPI db;
	
	private GoogleAPI google;

	@Before
	public void before() {
		db = new DatabaseAPI();
		google = new GoogleAPI();		
		Person.createPersonTable(db);
		Person.populatePeople(db);		
	}

	@After
	public void after() {
		Person.dropPersonTable(db);
		db.close();
	}	
	
	@Test
	public void testLoadPeopleSheet() throws SQLException {		
		TableToSpreadsheetBatch table = loadSpreadsheetTable();		
		google.spreadsheet("0AsxNRtEKJEOadC10a3MtMDVabmRRc0dDY0lNQXNTQ3c").worksheet("people").batch(table);
	}

	private TableToSpreadsheetBatch loadSpreadsheetTable() throws SQLException {
		ResultSet rs = db.query("select id, name, age from people").resultSet();		
		return new TableToSpreadsheetBatch(rs);
	}
}
