package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.ResultHistoryQuery;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;
import uk.co.olimor.BMBTApi_boot.model.TestResult;

/**
 * Class which performs the query to get all test results for a given user.
 * 
 * @author leonmorley
 *
 */
@Log4j2
@Service
public class ResultsHistoryQueryImpl extends AbstractQuery<TestResult> implements ResultHistoryQuery {

	/**
	 * Query.
	 */
	private static final String QUERY = "SELECT * FROM testResult WHERE userId = %x";

	@Override
	public List<TestResult> getResultHistory(final int userId) throws ApiException {
		log.entry(userId);
		
		final List<TestResult> results = query(String.format(QUERY, userId));
		
		if (results.size() == 0)
			throw new ApiException("No results found for user id", HttpStatus.NOT_FOUND);
		
		return log.traceExit(results);
	}

	@Override
	protected List<TestResult> buildResult(ResultSet result) throws SQLException {
		final List<TestResult> results = new ArrayList<>();

		try {
			while (result.next()) 
				results.add(new TestResult(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), 
						result.getInt(5), result.getFloat(6)));			
		} catch (final SQLException e) {
			log.error("An error occurred whilst attempting to build the results.", e);
			throw e;			
		}
		
		return log.traceExit(results);
	}

}
