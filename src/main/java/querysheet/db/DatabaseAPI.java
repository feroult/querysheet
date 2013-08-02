package querysheet.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import querysheet.utils.Setup;

public class DatabaseAPI implements Closeable {

	private Connection conn;

	private ResultSet rs;

	public DatabaseAPI() {
		try {
			loadDriver();
			connect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void connect() throws SQLException {
		conn = DriverManager.getConnection(Setup.getJdbcUrl(), Setup.getUser(), Setup.getPassword());
	}

	private void loadDriver() throws ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
	}

	public DatabaseAPI query(String sql) {
		try {
			closeResultSet();

			PreparedStatement ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			return this;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ResultSet resultSet() {
		return rs;
	}

	@Override
	public void close() {
		try {
			closeResultSet();
			closeConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void closeConnection() throws SQLException {
		conn.close();
	}

	private void closeResultSet() throws SQLException {
		if (rs == null) {
			return;
		}
		rs.close();
	}

	public void exec(String sql) {
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
