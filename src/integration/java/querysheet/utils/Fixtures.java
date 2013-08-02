package querysheet.utils;

import querysheet.db.DatabaseAPI;

public class Fixtures {
	public static final int MAX_PERSON = 10;	
	
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
