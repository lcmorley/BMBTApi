package uk.co.olimor.BMBTApi_boot.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.dao.impl.AbstractQuery;
import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.Question;
import uk.co.olimor.BMBTApi_Common.model.Test;
import uk.co.olimor.BMBTApi_boot.dao.TestQuery;

/**
 * Implementation of {@link TestQuery}.
 * 
 * @author leonmorley
 *
 */
@Log4j2
@Service
public class TestQueryImpl extends AbstractQuery<Test> implements TestQuery {

	/**
	 * Query.
	 */
	private static final String QUERY = "SELECT test.id, testName, testTime, xValue, yValue, operatorValue\n" + 
			"FROM test\n" + 
			"JOIN testQuestion ON test.id = testQuestion.testId\n" + 
			"JOIN operator ON testQuestion.operatorId = operator.id\n" + 
			"ORDER BY test.id";
	
	@Override
	@Cacheable("tests")
	public List<Test> getTests() throws ApiException {
		log.traceEntry();
		return log.traceExit(query(QUERY));
	}

	@Override
	protected List<Test> buildResult(final ResultSet result) throws SQLException {
		log.entry(result);
		
		final List<Test> tests = new ArrayList<>();
		
		int currentTest = 0;
		Test currentTestObject = null;
		List<Question> questions = null;
		
		try {
			while (result.next()) {
				final int testId = result.getInt(1);
			
				if (currentTest != testId) {
					currentTest = testId;
					questions = new ArrayList<>();	
					currentTestObject = new Test(testId, result.getString(2), result.getInt(3), questions);
					tests.add(currentTestObject);
				}
				
				questions.add(new Question(result.getInt(4), result.getInt(5), result.getString(6)));
			}
		} catch (final SQLException e) {
			log.error("An exception occurred whilst attempting to retrieve a Test.",  e);
			throw e;
		}
		
		return log.traceExit(tests);
	}

}
