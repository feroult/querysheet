package querysheet.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryRunner implements Closeable {

	private Connection conn;

	private ResultSet rs;

	public QueryRunner() {
		loadDriver();
		connect();
	}

	private void connect() {
		String url = "jdbc:postgresql://127.0.0.1/querysheet";

		try {
			conn = DriverManager.getConnection(url, "qs", "qs");
		} catch (SQLException e) {

		}
	}

	private void loadDriver() {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public ResultSet exec(String sql) {
		try {
			closeResultSet();

			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			return rs;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		closeResultSet();
		closeConnection();
	}

	private void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}

	private void closeResultSet() {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
