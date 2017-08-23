package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.AbstractDAO;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;

/**
 * Abstract class containing common functionality.
 * 
 * @author leonmorley
 *
 */
@Log4j2
public abstract class AbstractQuery<T> extends AbstractDAO {

	/**
	 * Datasource object.
	 */
	@Autowired
	protected MysqlDataSource datasource;
	
	/**
	 * Run query and return T as a result.
	 * 
	 * @param query - the query to run.
	 * 
	 * @return the result {@link List}.
	 */
	public List<T> query(final String query) throws ApiException {
		log.entry(query);
		
		ResultSet result = null;
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = datasource.getConnection();
			stmt = conn.createStatement();

			result = stmt.executeQuery(query);
			return log.traceExit(buildResult(result));		
		} catch (final SQLException e) {
			logError(log, "An error occurred whilst attempting to run the query: " + query, e, 
					HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (result != null)
					result.close();
				if (conn != null)
					conn.close();
				if (stmt != null)
					stmt.close();
			} catch (final SQLException e) {
				logError(log, "An error occurred whilst attempting to run the query: " + query, e, 
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		return log.traceExit(new ArrayList<T>());
	}
	
	/**
	 * Build results object.
	 * 
	 * @param result - the result from the database query.
	 * 
	 * @return results converted to a {@link List} of T.
	 */
	protected abstract List<T> buildResult(final ResultSet result) throws ApiException;

}
