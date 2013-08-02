package querysheet;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
		SpreadsheetTable table = loadSpreadsheetTable();		
		google.spreadsheet("0AsxNRtEKJEOadC10a3MtMDVabmRRc0dDY0lNQXNTQ3c").worksheet("people").batch(table);
	}

	private SpreadsheetTable loadSpreadsheetTable() throws SQLException {
		ResultSet rs = db.query("select id, name, age from people").resultSet();
		
		SpreadsheetTable table = new SpreadsheetTable();		
		loadHeaders(table, rs);		
		loadRows(table, rs);
		return table;
	}

	private void loadHeaders(SpreadsheetTable table, ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		Object[] cols = new Object[metaData.getColumnCount()];
		
		for(int i = 0; i < metaData.getColumnCount(); i++) {
			cols[i] = metaData.getColumnLabel(i+1);
		}
		
		table.addRow(cols);
	}
	
	private void loadRows(SpreadsheetTable table, ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		
		while(rs.next()) {
			
			Object[] cols = new Object[metaData.getColumnCount()];
		
			for(int i = 0; i < metaData.getColumnCount(); i++) {
				cols[i] = rs.getObject(i+1);
			}
			
			table.addRow(cols);
		}				
	}
}
