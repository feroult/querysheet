package querysheet.db;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class QueryRunnerTest {

	@Test
	public void testSimpleQuery() throws SQLException {
		QueryRunner query = new QueryRunner();
		
		try {
		
			ResultSet rs = query.exec("select 1");		
			rs.next();
			assertEquals(1, rs.getInt(1));
			
		} finally {
			query.close();
		}		
	}
}
