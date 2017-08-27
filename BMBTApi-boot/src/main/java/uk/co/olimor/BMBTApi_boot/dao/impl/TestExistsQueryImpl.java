package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.dao.TestExistsQuery;
import uk.co.olimor.BMBTApi_boot.exception.ApiException;

@Service
@Log4j2
public class TestExistsQueryImpl extends AbstractQuery<Boolean> implements TestExistsQuery {

	private final String QUERY = "SELECT COUNT(*) FROM test WHERE id = %x";
	
	@Override
	public boolean testExists(final int testId) throws ApiException {
		log.entry(testId);
		
		try {
			return log.traceExit(query(String.format(QUERY, testId)).get(0));
		} catch (final ApiException e) {
			throw (e);
		}
	}

	@Override
	protected List<Boolean> buildResult(ResultSet result) throws SQLException {
		log.entry(result);
		
		final List<Boolean> results = new ArrayList<>();
		
		try {
			while(result.next())
				results.add(result.getInt(1) == 1);
		} catch (final SQLException e) {
			log.error("An exception occurred whilst attempting to fins a test.", e);
			throw e;
		}
		
		return log.traceExit(results);
	}

}
