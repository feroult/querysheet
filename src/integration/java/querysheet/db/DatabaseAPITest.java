package querysheet.db;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import querysheet.utils.Fixtures;

public class DatabaseAPITest {

	private DatabaseAPI db;

	@Before
	public void before() {
		db = new DatabaseAPI();

		Fixtures.createPersonTable(db);
		Fixtures.populatePeople(db);
	}

	@After
	public void after() {
		Fixtures.dropPersonTable(db);

		db.close();
	}

	@Test
	public void testSimpleQuery() throws SQLException {
		ResultSet rs = db.query("select age from people limit 1").resultSet();
		rs.next();
		assertEquals(21, rs.getInt(1));
	}

}
