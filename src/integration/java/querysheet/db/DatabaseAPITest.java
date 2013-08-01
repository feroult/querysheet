package querysheet.db;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseAPITest {

	@Before
	public void before() {
		createSimpleTable();
		populateSimpleTable();
	}

	@After
	public void after() {
		dropSimpleTable();
	}

	@Test
	public void testSimpleQuery() throws SQLException {
		DatabaseAPI db = new DatabaseAPI();

		try {

			ResultSet rs = db.query("select age from simple limit 1");
			rs.next();
			assertEquals(21, rs.getInt(1));

		} finally {
			db.close();
		}
	}

	@Test
	public void testMapQuery() {

	}

	private void createSimpleTable() {
		DatabaseAPI.singleExec("create table simple (id integer primary key, name text, age integer)");
	}

	private void populateSimpleTable() {
		DatabaseAPI db = new DatabaseAPI();

		try {

			for (int i = 1; i <= 20; i++) {
				db.exec(String.format("insert into simple (id, name, age) values (%d, 'Person - %d', %d)", i, i, i+20));
			}

		} finally {
			db.close();
		}
	}

	private void dropSimpleTable() {
		DatabaseAPI.singleExec("drop table simple");
	}
}
