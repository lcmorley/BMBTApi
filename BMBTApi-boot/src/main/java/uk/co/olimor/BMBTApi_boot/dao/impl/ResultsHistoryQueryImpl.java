package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.ResultHistoryQuery;
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
	public List<TestResult> getResultHistory(final int userId) {
		log.entry(userId);
		return log.traceExit(query(String.format(QUERY, userId)));
	}

	@Override
	protected List<TestResult> buildResult(ResultSet result) {
		final List<TestResult> results = new ArrayList<>();

		try {
			while (result.next()) 
				results.add(new TestResult(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4), 
						result.getInt(5), result.getFloat(6)));			
		} catch (SQLException e) {
			log.error("An error occurred whilst attempting to build the results.", e);
		}
		
		return log.traceExit(results);
	}

}
