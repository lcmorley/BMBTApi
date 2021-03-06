package uk.co.olimor.BMBTApi_boot.dao;

import java.util.List;

import uk.co.olimor.BMBTApi_Common.exception.ApiException;
import uk.co.olimor.BMBTApi_Common.model.Test;

/**
 * Interface for the Test Service.
 * 
 * @author leonmorley
 */
public interface TestQuery {

	/**
	 * @return a {@link List} of {@link Test} objects.
	 */
	List<Test> getTests() throws ApiException;
	
}
