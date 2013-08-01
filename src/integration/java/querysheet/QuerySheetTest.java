package querysheet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import querysheet.db.DatabaseAPI;

public class QuerySheetTest {

	private DatabaseAPI db;

	@Before
	public void before() {
		db = new DatabaseAPI();
	}

	@After
	public void after() {
		db.close();
	}	
	
	@Test
	public void testLoadPeopleSheet() {
		
		
		
		
	}
	
}
