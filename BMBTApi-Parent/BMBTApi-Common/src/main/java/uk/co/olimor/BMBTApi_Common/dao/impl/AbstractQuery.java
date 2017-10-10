package uk.co.olimor.BMBTApi_Common.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.dao.AbstractDAO;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;

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
	protected DataSource datasource;

	/**
	 * Run query and return T as a result.
	 * 
	 * @param query
	 *            - the query to run.
	 * 
	 * @return the result {@link List}.
	 */
	public List<T> query(final String query) throws ApiException {
		log.entry(query);

		try (final Connection conn = datasource.getConnection();
			final Statement stmt = conn.createStatement();
			final ResultSet result = stmt.executeQuery(query)) {
			return log.traceExit(buildResult(result));
		} catch (final Exception e) {
			logError(log, "An error occurred whilst attempting to run the query: " + query, e,
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return log.traceExit(new ArrayList<T>());
	}

	/**
	 * Build results object.
	 * 
	 * @param result
	 *            - the result from the database query.
	 * 
	 * @return results converted to a {@link List} of T.
	 */
	protected abstract List<T> buildResult(final ResultSet result) throws Exception;

}
