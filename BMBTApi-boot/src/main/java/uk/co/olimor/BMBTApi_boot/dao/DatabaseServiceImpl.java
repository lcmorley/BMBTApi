package uk.co.olimor.BMBTApi_boot.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import uk.co.olimor.BMBTApi_boot.model.User;

/**
 * Service which implements performs Database CRUD operation.
 * 
 * @author leonmorley
 *
 */
@Service
public class DatabaseServiceImpl implements DatabaseService {

	/**
	 * Datasource object.
	 */
	@Autowired
	private MysqlDataSource datasource;

	/**
	 * 
	 */
	public List<User> getUsers() {
		final List<User> users = new ArrayList<>();
		ResultSet result = null;
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = datasource.getConnection();
			stmt = conn.createStatement();

			result = stmt.executeQuery("SELECT * FROM users");
			while (result.next())
				users.add(new User(result.getInt(1), result.getString(2), "lcmorley"));
		} catch (final SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (result != null)
					result.close();
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
		}

		return users;
	}
}
