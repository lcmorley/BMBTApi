package uk.co.olimor.BMBTApi_boot.dao;

import java.util.List;

import uk.co.olimor.BMBTApi_boot.model.ResultsAnalysis;
import uk.co.olimor.BMBTApi_boot.model.TestResult;

/**
 * Contract for the {@link ResultHistoryQuery} interface.
 * 
 * @author leonmorley
 *
 */
public interface ResultHistoryQuery {

	/**
	 * Analyse results and return the findings.
	 * 
	 * @param userId - the userId to analyse.
	 * 
	 * @return - the {@link ResultsAnalysis} object containing the results.
	 */
	List<TestResult> getResultHistory(final int userId);
	
}
