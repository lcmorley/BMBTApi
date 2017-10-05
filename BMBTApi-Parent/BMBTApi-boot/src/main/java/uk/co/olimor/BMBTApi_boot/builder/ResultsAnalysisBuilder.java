package uk.co.olimor.BMBTApi_boot.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_Common.model.TestAnalysis;
import uk.co.olimor.BMBTApi_Common.model.TestResult;

/**
 * Builder to build and return a {@link ResultsAnalysis} object.
 * 
 * @author leonmorley
 *
 */
@Log4j2
@Service
public final class ResultsAnalysisBuilder {

	/**
	 * Build and return a {@link ResultsAnalysis} object.
	 * 
	 * @param results - the results used to build the {@link ResultsAnalysis} object.
	 * 
	 * @return - the newly built {@link ResultsAnalysis} object.
	 */
	public List<TestAnalysis> buildResultsAnalysis(final List<TestResult> results) {
		log.entry(results);
		return log.traceExit(buildAverageResults(generateTotals(results)));
	}

	/**
	 * Given the test result totals, calculate the averages.
	 * 
	 * @param resultTotals - the result totals.
	 * 
	 * @return - the calculated averages.
	 */
	private List<TestAnalysis> buildAverageResults(final Map<Integer, TestResultTotals> resultTotals) {
		log.entry(resultTotals);
		
		final List<TestAnalysis> testAnalysis = new ArrayList<>();
		
		for (final Integer testId : resultTotals.keySet()) {
			final TestResultTotals totals = resultTotals.get(testId);
			
			final TestAnalysis analysis = new TestAnalysis(testId);
			
			final int totalTests = totals.getTotalTestAttempts();
			
			analysis.setAverageCorrectAnswers(totals.getTotalCorrectAnswers()/totalTests);
			analysis.setAverageTime(totals.getTotalTime()/totalTests);
			analysis.setAverageAttemptedQuestions(totals.getTotalAttemptedAnswers()/totalTests);
			analysis.setBestTime(totals.getBestTime());
			analysis.setTopCorrectAnswers(totals.getTopCorrectAnswers());
			analysis.setTotalTests(totalTests);
			
			testAnalysis.add(analysis);
		}
		
		return log.traceExit(testAnalysis);
	}

	/**
	 * Given the DB results, build up the totals from the tests.
	 * 
	 * @param results - the results from the DB.
	 * @param resultTotals - the result totals built up.
	 */
	private Map<Integer, TestResultTotals> generateTotals(final List<TestResult> results) {
		log.entry(results);
		
		final Map<Integer, TestResultTotals> resultTotals = new HashMap<>();
		
		for (final TestResult result : results) {
			TestResultTotals totals = resultTotals.get(result.getTestId());
			
			if (totals == null) {
				totals = new TestResultTotals();
				resultTotals.put(result.getTestId(), totals);
			}
					
			totals.incrementTotalTestAttempts();
			
			totals.addTotalTime(result.getElapsedTime());
			
			if (totals.getBestTime() == 0.0f || totals.getBestTime() > result.getElapsedTime())
				totals.setBestTime(result.getElapsedTime());
			
			if (totals.getTopCorrectAnswers() < result.getCorrectAnswers()) 
				totals.setTopCorrectAnswers(result.getCorrectAnswers());
			
			totals.addTotalAttemptedAnswers(result.getCorrectAnswers());
			totals.addTotalAttemptedAnswers(result.getIncorrectAnswers());
			
			totals.addTotalCorrectAnswers(result.getCorrectAnswers());
		}
		
		return log.traceExit(resultTotals);
	}
	
	/**
	 * Private class for building Total Result Totals.
	 * 
	 * @author leonmorley
	 *
	 */
	@Data
	private class TestResultTotals {
		
		private int totalTestAttempts;
		
		private float totalTime;
		
		private float bestTime;
		
		private int totalAttemptedAnswers;

		private int totalCorrectAnswers;
		
		private int topCorrectAnswers;
		
		/**
		 * Add to the totalTime.
		 * 
		 * @param totalTime
		 */
		public void addTotalTime(final float totalTime) {
			this.totalTime += totalTime;
		}
		 	
		/**
		 * Add to totalAttemptedAnswers.
		 * 
		 * @param totalAttemptedAnswers
		 */
		public void addTotalAttemptedAnswers(final int totalAttemptedAnswers) {
			this.totalAttemptedAnswers += totalAttemptedAnswers;
		}
		
		/**
		 * Add to totalCorrectAnswers.
		 * 
		 * @param totalCorrectAnswers
		 */
		public void addTotalCorrectAnswers(final int totalCorrectAnswers) {
			this.totalCorrectAnswers += totalCorrectAnswers;
		}
		
		/**
		 * Increment totalTestAttempts by 1.
		 */
		public void incrementTotalTestAttempts() {
			this.totalTestAttempts++;
		}
		
	}
	
}
