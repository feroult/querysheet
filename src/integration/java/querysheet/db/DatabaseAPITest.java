package querysheet.db;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseAPITest {

	private DatabaseAPI db;

	@Before
	public void before() {
		db = new DatabaseAPI();

		createSimpleTable();
		populateSimpleTable();
	}

	@After
	public void after() {
		dropSimpleTable();

		db.close();
	}

	@Test
	public void testSimpleQuery() throws SQLException {
		ResultSet rs = db.query("select age from simple limit 1");
		rs.next();
		assertEquals(21, rs.getInt(1));
	}

	@Test
	public void testMapQuery() {

	}

	private void createSimpleTable() {
		db.exec("create table simple (id integer primary key, name text, age integer)");
	}

	private void populateSimpleTable() {
		for (int i = 1; i <= 20; i++) {
			db.exec(String.format("insert into simple (id, name, age) values (%d, 'Person - %d', %d)", i, i, i + 20));
		}
	}

	private void dropSimpleTable() {
		db.exec("drop table simple");
	}
}
