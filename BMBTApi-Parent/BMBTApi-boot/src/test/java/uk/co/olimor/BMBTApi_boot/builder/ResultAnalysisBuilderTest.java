package uk.co.olimor.BMBTApi_boot.builder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import uk.co.olimor.BMBTApi_Common.model.TestAnalysis;
import uk.co.olimor.BMBTApi_Common.model.TestResult;

/**
 * Test class for the {@link ResultsAnalysisBuilder} class.
 * 
 * @author leonmorley
 *
 */
public class ResultAnalysisBuilderTest {

	@Test
	public void testBuildResultsAnalysis() {
		final List<TestResult> results = new ArrayList<>();
		
		//Test 1
		results.add(new TestResult("userId", 1, 5, 2, 10.0f, "Full"));
		results.add(new TestResult("userId", 1, 7, 3, 13.0f, "Full"));
		
		//Test 2
		results.add(new TestResult("userId", 2, 8, 1, 11.0f, "Full"));
		results.add(new TestResult("userId", 2, 9, 2, 12.0f, "Full"));
		results.add(new TestResult("userId", 2, 10, 0, 13.0f, "Full"));
		
		
		final List<TestAnalysis> analysis = new ResultsAnalysisBuilder().buildResultsAnalysis(results);
		
		Assert.assertEquals(2,  analysis.size());
		
		final TestAnalysis test1Analysis = analysis.get(0);
		Assert.assertEquals(1, test1Analysis.getTestId());
		Assert.assertEquals(2, test1Analysis.getTotalTests());
		Assert.assertEquals(7, test1Analysis.getTopCorrectAnswers());
		Assert.assertEquals(new Float(10.0), new Float(test1Analysis.getBestTime()));
		Assert.assertEquals(new Float(11.5), new Float(test1Analysis.getAverageTime()));
		Assert.assertEquals(8, test1Analysis.getAverageAttemptedQuestions());
		Assert.assertEquals(6, test1Analysis.getAverageCorrectAnswers());
		
		final TestAnalysis test2Analysis = analysis.get(1);
		Assert.assertEquals(2, test2Analysis.getTestId());
		Assert.assertEquals(3, test2Analysis.getTotalTests());
		Assert.assertEquals(10, test2Analysis.getTopCorrectAnswers());
		Assert.assertEquals(new Float(11.0), new Float(test2Analysis.getBestTime()));
		Assert.assertEquals(new Float(12.0), new Float(test2Analysis.getAverageTime()));
		Assert.assertEquals(10, test2Analysis.getAverageAttemptedQuestions());
		Assert.assertEquals(9, test2Analysis.getAverageCorrectAnswers());
	}
	
}
