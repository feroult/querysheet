package querysheet.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAPI implements Closeable {

	private Connection conn;

	private ResultSet rs;

	public DatabaseAPI() {
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

	public <T> List<T> map(ResultSetMapper<T> mapper, Class<T> clzz) {
		try {
		
			List<T> result = new ArrayList<T>();
			
			while(rs.next()) {
				result.add(mapper.map(rs));
			}
			
			return result;
		} catch(Exception e) {
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

	public void exec(String sql) {
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.execute();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
