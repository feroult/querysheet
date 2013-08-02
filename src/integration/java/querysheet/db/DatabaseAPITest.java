package querysheet.db;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseAPITest {

	public class PersonMapper implements ResultSetMapper<Person> {
		@Override
		public Person map(ResultSet rs) throws SQLException {
			Person person = new Person();
						
			person.setId(rs.getInt("id"));
			person.setName(rs.getString("name"));
			person.setAge(rs.getInt("age"));
			
			return person;
		}
	}

	private DatabaseAPI db;

	@Before
	public void before() {
		db = new DatabaseAPI();

		Person.createPersonTable(db);
		Person.populatePeople(db);
	}

	@After
	public void after() {
		Person.dropPersonTable(db);

		db.close();
	}

	@Test
	public void testSimpleQuery() throws SQLException {
		ResultSet rs = db.query("select age from people limit 1").resultSet();
		rs.next();
		assertEquals(21, rs.getInt(1));
	}

	@Test
	public void testQueryMap() {
		List<Person> people = db.query("select id, name, age from people order by id").map(new PersonMapper(),
				Person.class);

		for (int i = 1; i <= Person.MAX_PERSON; i++) {
			Person person = people.get(i-1);
			
			assertEquals(i, person.getId());
			assertEquals("Person - " + i, person.getName());
			assertEquals(i + 20, person.getAge());
		}		
	}
}
