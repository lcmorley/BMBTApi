package uk.co.olimor.BMBTApi_boot.builder;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uk.co.olimor.BMBTApi_boot.model.ResultsAnalysis;
import uk.co.olimor.BMBTApi_boot.model.TestResult;

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
	public ResultsAnalysis buildResultsAnalysis(final List<TestResult> results) {
		log.entry(results);
		
		final ResultsAnalysis analysis = new ResultsAnalysis();
		final int totalResults = results.size();
		
		analysis.setTotalTests(results.size());
		
		float totalTime = 0.0f;
		float bestTime = 0.0f;
		int totalAttemptedAnswers = 0;
		int totalCorrectAnswers = 0;
		int topCorrectAnswers = 0;
		
		for (final TestResult result : results) {
			totalTime += result.getElapsedTime();
			
			if (bestTime < result.getElapsedTime())
				bestTime = result.getElapsedTime();
			
			if (topCorrectAnswers < result.getCorrectAnswers()) 
				topCorrectAnswers = result.getCorrectAnswers();
			
			totalAttemptedAnswers += result.getCorrectAnswers();
			totalAttemptedAnswers += result.getIncorrectAnswers();
			
			totalCorrectAnswers += result.getCorrectAnswers();
		}
		
		analysis.setAverageCorrectAnswers(totalCorrectAnswers/totalResults);
		analysis.setAverageTime(totalTime/totalResults);
		analysis.setAverageAttemptedQuestions(totalAttemptedAnswers/totalResults);
		analysis.setBestTime(bestTime);
		analysis.setTopCorrectAnswers(topCorrectAnswers);
		
		return log.traceExit(analysis);
	}
	
}
