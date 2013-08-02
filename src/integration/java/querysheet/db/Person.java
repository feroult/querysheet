package querysheet.db;

public class Person {
	public static final int MAX_PERSON = 10;
	
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
	
	public static void createPersonTable(DatabaseAPI db) {
		try {
			db.exec("drop table people");
		} catch(RuntimeException e) {			
		}
		
		db.exec("create table people (id integer primary key, name text, age integer)");
	}

	public static void populatePeople(DatabaseAPI db) {
		for (int i = 1; i <= MAX_PERSON; i++) {
			db.exec(String.format("insert into people (id, name, age) values (%d, 'Person - %d', %d)", i, i, i + 20));
		}
	}

	public static void dropPersonTable(DatabaseAPI db) {
		db.exec("drop table people");
	}	
}
