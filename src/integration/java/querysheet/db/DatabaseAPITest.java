package querysheet.db;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatabaseAPITest {

	private static final int MAX_PERSON = 20;

	public class Person {
		private int id;
		private String name;
		private int age;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

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

		createPersonTable();
		populatePeople();
	}

	@After
	public void after() {
		dropPersonTable();

		db.close();
	}

	@Test
	public void testSimpleQuery() throws SQLException {
		ResultSet rs = db.query("select age from person limit 1").resultSet();
		rs.next();
		assertEquals(21, rs.getInt(1));
	}

	@Test
	public void testQueryMap() {
		List<Person> people = db.query("select id, name, age from person order by id").map(new PersonMapper(),
				Person.class);

		for (int i = 1; i <= MAX_PERSON; i++) {
			Person person = people.get(i-1);
			
			assertEquals(i, person.getId());
			assertEquals("Person - " + i, person.getName());
			assertEquals(i + 20, person.getAge());
		}		
	}

	private void createPersonTable() {
		try {
			db.exec("drop table person");
		} catch(RuntimeException e) {			
		}
		
		db.exec("create table person (id integer primary key, name text, age integer)");
	}

	private void populatePeople() {
		for (int i = 1; i <= MAX_PERSON; i++) {
			db.exec(String.format("insert into person (id, name, age) values (%d, 'Person - %d', %d)", i, i, i + 20));
		}
	}

	private void dropPersonTable() {
		db.exec("drop table person");
	}
}
